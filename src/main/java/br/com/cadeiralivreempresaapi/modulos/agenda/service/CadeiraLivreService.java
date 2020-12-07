package br.com.cadeiralivreempresaapi.modulos.agenda.service;

import br.com.cadeiralivreempresaapi.modulos.agenda.dto.agenda.CadeiraLivreRequest;
import br.com.cadeiralivreempresaapi.modulos.agenda.dto.agenda.CadeiraLivreResponse;
import br.com.cadeiralivreempresaapi.modulos.agenda.enums.ESituacaoAgenda;
import br.com.cadeiralivreempresaapi.modulos.agenda.enums.ETipoAgenda;
import br.com.cadeiralivreempresaapi.modulos.agenda.model.Agenda;
import br.com.cadeiralivreempresaapi.modulos.agenda.repository.AgendaRepository;
import br.com.cadeiralivreempresaapi.modulos.comum.response.SuccessResponseDetails;
import br.com.cadeiralivreempresaapi.modulos.empresa.service.EmpresaService;
import br.com.cadeiralivreempresaapi.modulos.notificacao.dto.NotificacaoCorpoRequest;
import br.com.cadeiralivreempresaapi.modulos.notificacao.service.NotificacaoService;
import br.com.cadeiralivreempresaapi.modulos.usuario.service.AutenticacaoService;
import br.com.cadeiralivreempresaapi.modulos.usuario.service.UsuarioAcessoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static br.com.cadeiralivreempresaapi.modulos.agenda.messages.AgendaHorarioMessages.*;
import static br.com.cadeiralivreempresaapi.modulos.comum.util.Constantes.NOVA_CADEIRA_LIVRE_NOTIFICACAO;
import static br.com.cadeiralivreempresaapi.modulos.comum.util.Constantes.TOKEN_CADEIRA_LIVRE;
import static org.springframework.util.ObjectUtils.isEmpty;

@Service
public class CadeiraLivreService {

    @Autowired
    private AgendaRepository agendaRepository;
    @Autowired
    private AgendaService agendaService;
    @Autowired
    private AutenticacaoService autenticacaoService;
    @Autowired
    private NotificacaoService notificacaoService;
    @Autowired
    private ServicoService servicoService;
    @Autowired
    private EmpresaService empresaService;
    @Autowired
    private HorarioService horarioService;
    @Autowired
    private UsuarioAcessoService acessoService;

    @Transactional
    public CadeiraLivreResponse disponibilizarCadeiraLivre(CadeiraLivreRequest request) {
        validarDadosCadeiraLivre(request);
        var agenda = Agenda.of(request,
            autenticacaoService.getUsuarioAutenticado(),
            horarioService.buscarPorId(request.getHorarioId()),
            servicoService.buscarServicosPorIds(request.getServicosIds()));
        validarEmpresasServicosEUsuario(request, agenda);
        agenda.calcularTotal(request.getDesconto());
        agendaRepository.save(agenda);
        enviarDadosNotificacaoCadeiraLivre(agenda);
        return CadeiraLivreResponse.of(agendaService.buscarAgendaPorId(agenda.getId()));
    }

    private void validarDadosCadeiraLivre(CadeiraLivreRequest request) {
        if (isEmpty(request)) {
            throw AGENDA_SEM_DADOS;
        }
        if (isEmpty(request.getDesconto())) {
            throw CADEIRA_LIVRE_SEM_DESCONTO;
        }
        if (isEmpty(request.getServicosIds())) {
            throw AGENDA_SEM_SERVICOS;
        }
        if (isEmpty(request.getEmpresaId())) {
            throw AGENDA_SEM_EMPRESA;
        }
    }

    private void validarEmpresasServicosEUsuario(CadeiraLivreRequest request,
                                                 Agenda agenda) {
        acessoService.validarPermissoesDoUsuario(request.getEmpresaId());
        agenda.setEmpresa(empresaService.buscarPorId(request.getEmpresaId()));
        servicoService.validarServicosExistentes(new ArrayList<>(agenda.getServicos()));
    }

    private void enviarDadosNotificacaoCadeiraLivre(Agenda agenda) {
        var agendaSalva = agendaService.buscarAgendaPorId(agenda.getId());
        notificacaoService.gerarDadosNotificacao(NotificacaoCorpoRequest.of(
            gerarMensagemNotificacaoCadeiraLivre(agendaSalva),
            NOVA_CADEIRA_LIVRE_NOTIFICACAO,
            TOKEN_CADEIRA_LIVRE
            )
        );
    }

    private String gerarMensagemNotificacaoCadeiraLivre(Agenda agenda) {
        return "Nova Cadeira Livre liberada para "
            .concat(agenda.getEmpresa().getNome())
            .concat(" oferecendo serviços de ")
            .concat(servicoService.tratarNomesServicos(new ArrayList<>(agenda.getServicos())))
            .concat(" com ")
            .concat(String.valueOf(agenda.getDesconto()))
            .concat("% de desconto!");
    }

    public List<CadeiraLivreResponse> buscarCadeirasLivres() {
        return agendaRepository.findAll()
            .stream()
            .map(CadeiraLivreResponse::of)
            .collect(Collectors.toList());
    }

    public List<CadeiraLivreResponse> buscarCadeirasLivresPorEmpresa(Integer empresaId) {
        acessoService.validarPermissoesDoUsuario(empresaId);
        return agendaRepository.findByEmpresaIdAndTipoAgendaAndSituacao(empresaId, ETipoAgenda.CADEIRA_LIVRE,
            ESituacaoAgenda.DISPNIVEL)
            .stream()
            .map(CadeiraLivreResponse::of)
            .collect(Collectors.toList());
    }

    public CadeiraLivreResponse buscarCadeiraLivreResponsePorIdEPorEmpresaId(Integer id, Integer empresaId) {
        return CadeiraLivreResponse.of(buscarCadeiraLivrePorIdEPorEmpresaId(id, empresaId));
    }

    public Agenda buscarCadeiraLivrePorIdEPorEmpresaId(Integer id, Integer empresaId) {
        acessoService.validarPermissoesDoUsuario(empresaId);
        return agendaRepository
            .findByIdAndEmpresaIdAndTipoAgenda(id, empresaId, ETipoAgenda.CADEIRA_LIVRE)
            .orElseThrow(() -> CADEIRA_LIVRE_NAO_ENCONTRADA);
    }

    @Transactional
    public SuccessResponseDetails indisponibilizarCadeiraLivre(Integer id, Integer empresaId) {
        var cadeiraLivre = buscarCadeiraLivrePorIdEPorEmpresaId(id, empresaId);
        validarCadeiraLivreComClienteAtribuido(cadeiraLivre);
        cadeiraLivre.setSituacao(ESituacaoAgenda.CANCELADA);
        agendaRepository.save(cadeiraLivre);
        return new SuccessResponseDetails("A cadeira livre foi indisponibilizada com sucesso.");
    }

    private void validarCadeiraLivreComClienteAtribuido(Agenda agenda) {
        if (!isEmpty(agenda.getClienteId())) {
            throw CADEIRA_LIVRE_COM_CLIENTE;
        }
    }
}
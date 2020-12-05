package br.com.cadeiralivreempresaapi.modulos.agenda.service;

import br.com.cadeiralivreempresaapi.modulos.agenda.dto.agenda.AgendaRequest;
import br.com.cadeiralivreempresaapi.modulos.agenda.dto.agenda.CadeiraLivreRequest;
import br.com.cadeiralivreempresaapi.modulos.agenda.dto.agenda.CadeiraLivreResponse;
import br.com.cadeiralivreempresaapi.modulos.agenda.model.Agenda;
import br.com.cadeiralivreempresaapi.modulos.agenda.repository.AgendaRepository;
import br.com.cadeiralivreempresaapi.modulos.comum.response.SuccessResponseDetails;
import br.com.cadeiralivreempresaapi.modulos.empresa.service.EmpresaService;
import br.com.cadeiralivreempresaapi.modulos.funcionario.service.FuncionarioService;
import br.com.cadeiralivreempresaapi.modulos.notificacao.dto.NotificacaoCorpoRequest;
import br.com.cadeiralivreempresaapi.modulos.notificacao.service.NotificacaoService;
import br.com.cadeiralivreempresaapi.modulos.usuario.dto.UsuarioAutenticado;
import br.com.cadeiralivreempresaapi.modulos.usuario.service.AutenticacaoService;
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
public class AgendaService {

    @Autowired
    private AgendaRepository agendaRepository;
    @Autowired
    private AutenticacaoService autenticacaoService;
    @Autowired
    private NotificacaoService notificacaoService;
    @Autowired
    private FuncionarioService funcionarioService;
    @Autowired
    private ServicoService servicoService;
    @Autowired
    private EmpresaService empresaService;
    @Autowired
    private HorarioService horarioService;

    @Transactional
    public SuccessResponseDetails reservarAgenda(AgendaRequest request) {
        // TODO implementar reserva de agenda
        return null;
    }

    @Transactional
    public CadeiraLivreResponse disponibilizarCadeiraLivre(CadeiraLivreRequest request) {
        validarDadosCadeiraLivre(request);
        var usuarioAutenticado = autenticacaoService.getUsuarioAutenticado();
        var agenda = Agenda.of(request,
            usuarioAutenticado,
            horarioService.buscarPorId(request.getHorarioId()),
            servicoService.buscarServicosPorIds(request.getServicosIds()));
        validarEmpresasServicosEUsuario(request, agenda, usuarioAutenticado);
        agenda.calcularTotal(request.getDesconto());
        agendaRepository.save(agenda);
        enviarDadosNotificacaoCadeiraLivre(agenda);
        return buscarCadeiraLivrePorId(agenda.getId());
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
                                                 Agenda agenda,
                                                 UsuarioAutenticado usuario) {
        agenda.setEmpresa(empresaService.buscarPorId(request.getEmpresaId()));
        servicoService.validarServicosExistentes(new ArrayList<>(agenda.getServicos()));
        if (usuario.isFuncionario()) {
            funcionarioService.validarUsuario(usuario.getId());
        }
    }

    private void enviarDadosNotificacaoCadeiraLivre(Agenda agenda) {
        var agendaSalva = buscarAgendaPorId(agenda.getId());
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

    public CadeiraLivreResponse buscarCadeiraLivrePorId(Integer id) {
        return CadeiraLivreResponse.of(buscarAgendaPorId(id));
    }

    public Agenda buscarAgendaPorId(Integer id) {
        return agendaRepository.findById(id)
            .orElseThrow(() -> AGENDA_NAO_ENCONTRADA);
    }
}
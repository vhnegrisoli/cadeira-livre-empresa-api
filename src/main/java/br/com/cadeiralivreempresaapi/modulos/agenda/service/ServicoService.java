package br.com.cadeiralivreempresaapi.modulos.agenda.service;

import br.com.cadeiralivreempresaapi.modulos.agenda.dto.servico.ServicoRequest;
import br.com.cadeiralivreempresaapi.modulos.agenda.dto.servico.ServicoResponse;
import br.com.cadeiralivreempresaapi.modulos.agenda.model.Servico;
import br.com.cadeiralivreempresaapi.modulos.agenda.repository.AgendaRepository;
import br.com.cadeiralivreempresaapi.modulos.agenda.repository.ServicoRepository;
import br.com.cadeiralivreempresaapi.modulos.comum.response.SuccessResponseDetails;
import br.com.cadeiralivreempresaapi.modulos.empresa.service.EmpresaService;
import br.com.cadeiralivreempresaapi.modulos.funcionario.repository.FuncionarioRepository;
import br.com.cadeiralivreempresaapi.modulos.usuario.dto.UsuarioAutenticado;
import br.com.cadeiralivreempresaapi.modulos.usuario.service.AutenticacaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static br.com.cadeiralivreempresaapi.modulos.agenda.messages.AgendaHorarioMessages.*;
import static br.com.cadeiralivreempresaapi.modulos.comum.util.Constantes.ESPACO;
import static br.com.cadeiralivreempresaapi.modulos.comum.util.Constantes.SEPARAR_POR_VIRGULAS;
import static org.springframework.util.ObjectUtils.isEmpty;

@Service
public class ServicoService {

    private static final Double ZERO = 0.00;

    @Autowired
    private ServicoRepository servicoRepository;
    @Autowired
    private EmpresaService empresaService;
    @Autowired
    private AutenticacaoService autenticacaoService;
    @Autowired
    private FuncionarioRepository funcionarioRepository;
    @Autowired
    private AgendaRepository agendaRepository;

    @Transactional
    public ServicoResponse salvarNovoServico(ServicoRequest request) {
        validarDadosServico(request);
        var servico = Servico.of(request);
        validarServicoExistente(servico);
        definirEmpresaParaServico(servico);
        return buscarServicoPorId(servicoRepository.save(servico).getId());
    }

    @Transactional
    public ServicoResponse atualizarServico(ServicoRequest request,
                                                   Integer id) {
        validarDadosServico(request);
        var servico = Servico.of(request);
        servico.setId(id);
        validarServicoExistente(servico);
        definirEmpresaParaServico(servico);
        servicoRepository.save(servico);
        return buscarServicoPorId(id);
    }

    private void definirEmpresaParaServico(Servico servico) {
        var empresa = empresaService.buscarPorId(servico.getEmpresa().getId());
        servico.setEmpresa(empresa);
    }

    private void validarDadosServico(ServicoRequest request) {
        if (isEmpty(request)) {
            throw SERVICO_VAZIO;
        }
        if (isEmpty(request.getDescricao())) {
            throw SERVICO_DESCRICAO_VAZIA;
        }
        if (isEmpty(request.getEmpresaId())) {
            throw SERVICO_EMPRESA_VAZIA;
        }
        if (isEmpty(request.getPreco())) {
            throw SERVICO_PRECO_VAZIO;
        }
        if (request.getPreco() <= ZERO) {
            throw SERVICO_PRECO_MENOR_IGUAL_ZERO;
        }
    }

    private void validarServicoExistente(Servico servico) {
        if (isEmpty(servico.getId())
            && servicoRepository.existsByDescricaoAndEmpresaId(servico.getDescricao(), servico.getEmpresa().getId())
            || !isEmpty(servico.getId())
            && servicoRepository
            .existsByDescricaoAndEmpresaIdAndIdNot(servico.getDescricao(), servico.getEmpresa().getId(), servico.getId())) {
            throw SERVICO_JA_EXISTENTE;
        }
    }

    public void validarServicosExistentes(List<Servico> servicos) {
        servicos
            .stream()
            .map(Servico::getId)
            .forEach(this::validarServicoExistentePorId);
    }

    private void validarServicoExistentePorId(Integer id) {
        if (!servicoRepository.existsById(id)) {
            throw SERVICO_NAO_ENCONTRADO;
        }
    }

    public List<ServicoResponse> buscarServicosPorEmpresa(Integer empresaId) {
        validarPermissoesUsuario(empresaId);
        return servicoRepository.findByEmpresaIdOrderByDescricao(empresaId)
            .stream()
            .map(ServicoResponse::of)
            .collect(Collectors.toList());
    }

    public Servico buscarPorId(Integer id) {
        return servicoRepository.findById(id)
            .orElseThrow(() -> SERVICO_NAO_ENCONTRADO);
    }

    public ServicoResponse buscarServicoPorId(Integer id) {
        var servico = buscarPorId(id);
        validarPermissoesUsuario(servico.getEmpresa().getId());
        return ServicoResponse.of(servico);
    }

    public Set<Servico> buscarServicosPorIds(List<Integer> ids) {
        return servicoRepository.findByIdIn(ids);
    }

    @Transactional
    public SuccessResponseDetails removerServicoPorId(Integer id) {
        var servico = buscarPorId(id);
        validarPermissoesUsuario(servico.getEmpresa().getId());
        validarServicoExistenteParaAgenda(servico);
        servicoRepository.delete(servico);
        return SERVICO_REMOVIDO_SUCESSO;
    }

    private void validarServicoExistenteParaAgenda(Servico servico) {
        if (agendaRepository.existsByServicosId(servico.getId())) {
            throw SERVICO_EXISTENTE_AGENDA;
        }
    }

    private void validarPermissoesUsuario(Integer empresaId) {
        var usuarioAutenticado = autenticacaoService.getUsuarioAutenticado();
        if (!usuarioAutenticado.isAdmin()
            && !isSocioProprietarioValido(usuarioAutenticado, empresaId)
            && !isFuncionarioValido(usuarioAutenticado, empresaId)) {
            throw SERVICO_SEM_PERMISSAO;
        }
    }

    private boolean isSocioProprietarioValido(UsuarioAutenticado usuarioAutenticado, Integer empresaId) {
        return usuarioAutenticado.isSocioOuProprietario()
            && empresaService.existeEmpresaParaUsuario(empresaId, usuarioAutenticado.getId());
    }

    private boolean isFuncionarioValido(UsuarioAutenticado usuarioAutenticado, Integer empresaId) {
        return usuarioAutenticado.isFuncionario()
            && funcionarioRepository.existsByUsuarioIdAndEmpresaId(usuarioAutenticado.getId(), empresaId);
    }

    public String tratarNomesServicos(List<Servico> servicos) {
        var servicosString = servicos
            .stream()
            .map(servico -> ESPACO.toString().concat(servico.getDescricao()))
            .collect(Collectors.joining(SEPARAR_POR_VIRGULAS));
        if (!isEmpty(servicosString) && servicosString.charAt(0) == ESPACO) {
            return servicosString.substring(1);
        }
        return servicosString;
    }
}

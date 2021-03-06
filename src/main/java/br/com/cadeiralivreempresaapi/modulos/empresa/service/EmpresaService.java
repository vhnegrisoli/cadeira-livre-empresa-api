package br.com.cadeiralivreempresaapi.modulos.empresa.service;

import br.com.cadeiralivreempresaapi.modulos.agenda.service.HorarioService;
import br.com.cadeiralivreempresaapi.modulos.agenda.service.ServicoService;
import br.com.cadeiralivreempresaapi.modulos.comum.dto.PageRequest;
import br.com.cadeiralivreempresaapi.modulos.comum.response.SuccessResponseDetails;
import br.com.cadeiralivreempresaapi.modulos.empresa.dto.*;
import br.com.cadeiralivreempresaapi.modulos.empresa.enums.ESituacaoEmpresa;
import br.com.cadeiralivreempresaapi.modulos.empresa.model.Empresa;
import br.com.cadeiralivreempresaapi.modulos.empresa.repository.EmpresaRepository;
import br.com.cadeiralivreempresaapi.modulos.jwt.service.JwtService;
import br.com.cadeiralivreempresaapi.modulos.usuario.dto.UsuarioAutenticado;
import br.com.cadeiralivreempresaapi.modulos.usuario.model.Usuario;
import br.com.cadeiralivreempresaapi.modulos.usuario.service.AutenticacaoService;
import br.com.cadeiralivreempresaapi.modulos.usuario.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static br.com.cadeiralivreempresaapi.modulos.empresa.messages.EmpresaMessages.*;
import static br.com.cadeiralivreempresaapi.modulos.jwt.messages.JwtMessages.USUARIO_NAO_AUTENTICADO;

@Service
public class EmpresaService {

    @Autowired
    private EmpresaRepository empresaRepository;
    @Autowired
    private UsuarioService usuarioService;
    @Autowired
    private AutenticacaoService autenticacaoService;
    @Autowired
    private ServicoService servicoService;
    @Autowired
    private HorarioService horarioService;
    @Autowired
    private JwtService jwtService;

    @Transactional
    public SuccessResponseDetails salvar(EmpresaRequest request) {
        var empresa = Empresa.of(request);
        empresa.adicionarProprietario(usuarioService.buscarPorId(autenticacaoService.getUsuarioAutenticadoId()));
        validarNovaEmpresa(empresa);
        empresaRepository.save(empresa);
        return EMPRESA_CRIADA_SUCESSO;
    }

    @Transactional
    public SuccessResponseDetails editar(EmpresaRequest request, Integer id) {
        var empresa = Empresa.of(request);
        empresa.setId(id);
        var empresaExistente = buscarPorId(id);
        validarEdicaoEmpresa(empresa);
        validarEmpresaAtiva(empresaExistente);
        empresa.setSocios(empresaExistente.getSocios());
        empresa.setSituacao(empresaExistente.getSituacao());
        empresaRepository.save(empresa);
        return EMPRESA_ALTERADA_SUCESSO;
    }

    private void validarNovaEmpresa(Empresa empresa) {
        validarEmpresaExistentePorRazaoSocialAoSalvar(empresa);
        validarEmpresaExistentePorCnpjAoSalvar(empresa);
    }

    private void validarEdicaoEmpresa(Empresa empresa) {
        validarEmpresaExistentePorRazaoSocialAoEditar(empresa);
        validarEmpresaExistentePorCnpjAoEditar(empresa);
    }

    private void validarEmpresaExistentePorRazaoSocialAoSalvar(Empresa empresa) {
        if (empresaRepository.existsByRazaoSocial(empresa.getRazaoSocial())) {
            throw RAZAO_SOCIAL_EXISTENTE;
        }
    }

    private void validarEmpresaExistentePorCnpjAoSalvar(Empresa empresa) {
        if (empresaRepository.existsByCnpj(empresa.getCnpj())) {
            throw CNPJ_EXISTENTE;
        }
    }

    private void validarEmpresaExistentePorRazaoSocialAoEditar(Empresa empresa) {
        if (empresaRepository.existsByRazaoSocialAndIdNot(empresa.getRazaoSocial(), empresa.getId())) {
            throw RAZAO_SOCIAL_EXISTENTE;
        }
    }

    private void validarEmpresaExistentePorCnpjAoEditar(Empresa empresa) {
        if (empresaRepository.existsByCnpjAndIdNot(empresa.getCnpj(), empresa.getId())) {
            throw CNPJ_EXISTENTE;
        }
    }

    private void validarEmpresaAtiva(Empresa empresa) {
        if (!empresa.isAtiva()) {
            throw EMPRESA_INATIVA;
        }
    }

    public EmpresaResponse buscarPorIdComSocios(Integer id) {
        var empresa = buscarPorId(id);
        return EmpresaResponse.of(empresa);
    }

    public Page<EmpresaPageResponse> buscarTodas(PageRequest pageable, EmpresaFiltros filtros) {
        var usuarioAutenticado = autenticacaoService.getUsuarioAutenticado();
        tratarPermissoesBuscaEmpresas(usuarioAutenticado, filtros);
        return empresaRepository.findAll(filtros.toPredicate().build(), pageable)
            .map(EmpresaPageResponse::of);
    }

    private void tratarPermissoesBuscaEmpresas(UsuarioAutenticado usuarioAutenticado, EmpresaFiltros filtros) {
        if (!usuarioAutenticado.isAdmin()) {
            filtros.setSocioId(usuarioAutenticado.getId());
        }
    }

    public Empresa buscarPorId(Integer id) {
        validarPermissaoDoUsuario(autenticacaoService.getUsuarioAutenticado(), id);
        return empresaRepository
            .findById(id).orElseThrow(() -> EMPRESA_NAO_ENCONTRADA);
    }

    public Boolean existeEmpresaParaUsuario(Integer empresaId, Integer usuarioId) {
        return empresaRepository.existsByIdAndSociosId(empresaId, usuarioId);
    }

    public void validarPermissaoDoUsuario(UsuarioAutenticado usuarioAutenticado, Integer empresaId) {
        if (usuarioAutenticado.isSocioOuProprietario()
            && !empresaRepository.existsByIdAndSociosId(empresaId, usuarioAutenticado.getId())) {
            throw EMPRESA_USUARIO_SEM_PERMISSAO;
        }
    }

    public void inserirSocio(Usuario usuario, Integer empresaId) {
        var empresa = buscarPorId(empresaId);
        empresa.adicionarProprietario(usuario);
        empresaRepository.save(empresa);
    }

    @Transactional
    public SuccessResponseDetails alterarSituacao(Integer id) {
        var usuarioAutenticado = autenticacaoService.getUsuarioAutenticado();
        validarPermissaoDoUsuario(usuarioAutenticado, id);
        var empresa = buscarPorId(id);
        empresa.setSituacao(empresa.isAtiva()
            ? ESituacaoEmpresa.INATIVA
            : ESituacaoEmpresa.ATIVA);
        return EMPRESA_SITUACAO_ALTERADA_SUCESSO;
    }

    public List<EmpresaListagemClienteResponse> buscarEmpresasParaCliente(String token, EmpresaFiltros filtros) {
        validarClienteComJwtValido(token);
        filtros.setSituacao(ESituacaoEmpresa.ATIVA);
        return empresaRepository
            .findAll(filtros.toPredicate().build())
            .stream()
            .map(EmpresaListagemClienteResponse::of)
            .collect(Collectors.toList());
    }

    public EmpresaClienteResponse buscarEmpresaPorId(Integer id, String token) {
        validarClienteComJwtValido(token);
        var empresa = empresaRepository
            .findByIdAndSituacao(id, ESituacaoEmpresa.ATIVA)
            .orElseThrow(() -> EMPRESA_NAO_ENCONTRADA);
        var servicos = servicoService.buscarServicosPorEmpresaParaCliente(empresa.getId());
        var horarios = horarioService.buscarHorariosPorEmpresaParaCliente(empresa.getId());
        return EmpresaClienteResponse.of(empresa, servicos, horarios);
    }

    private void validarClienteComJwtValido(String jwtToken) {
        if (!jwtService.verificarUsuarioValidoComTokenValida(jwtToken)) {
            throw USUARIO_NAO_AUTENTICADO;
        }
    }
}

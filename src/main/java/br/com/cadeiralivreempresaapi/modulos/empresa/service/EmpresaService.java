package br.com.cadeiralivreempresaapi.modulos.empresa.service;

import br.com.cadeiralivreempresaapi.config.exception.ValidacaoException;
import br.com.cadeiralivreempresaapi.modulos.comum.dto.PageRequest;
import br.com.cadeiralivreempresaapi.modulos.comum.response.SuccessResponseDetails;
import br.com.cadeiralivreempresaapi.modulos.empresa.dto.*;
import br.com.cadeiralivreempresaapi.modulos.empresa.model.Empresa;
import br.com.cadeiralivreempresaapi.modulos.empresa.repository.EmpresaRepository;
import br.com.cadeiralivreempresaapi.modulos.funcionario.repository.FuncionarioRepository;
import br.com.cadeiralivreempresaapi.modulos.usuario.dto.UsuarioAutenticado;
import br.com.cadeiralivreempresaapi.modulos.usuario.model.Usuario;
import br.com.cadeiralivreempresaapi.modulos.usuario.service.AutenticacaoService;
import br.com.cadeiralivreempresaapi.modulos.usuario.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class EmpresaService {

    @Autowired
    private EmpresaRepository empresaRepository;
    @Autowired
    private UsuarioService usuarioService;
    @Autowired
    private AutenticacaoService autenticacaoService;
    @Autowired
    private FuncionarioRepository funcionarioRepository;

    @Transactional
    public SuccessResponseDetails salvar(EmpresaRequest request) {
        var usuario = usuarioService.buscarPorId(autenticacaoService.getUsuarioAutenticadoId());
        validarUsuarioComPermissaoParaGerenciarEmpresa(usuario);
        var empresa = Empresa.of(request, usuario);
        validarNovaEmpresa(empresa);
        empresaRepository.save(empresa);
        return new SuccessResponseDetails("A empresa " + empresa.getNome() + " foi criada com sucesso!");
    }

    @Transactional
    public SuccessResponseDetails editar(EmpresaRequest request, Integer id) {
        var usuario = usuarioService.buscarPorId(autenticacaoService.getUsuarioAutenticadoId());
        validarUsuarioComPermissaoParaGerenciarEmpresa(usuario);
        var empresa = Empresa.of(request, usuario);
        empresa.setId(id);
        validarEdicaoEmpresa(empresa);
        empresaRepository.save(empresa);
        return new SuccessResponseDetails("A empresa " + empresa.getNome() + " foi alterada com sucesso!");
    }

    private void validarUsuarioComPermissaoParaGerenciarEmpresa(Usuario usuario) {
        if (!usuario.possuiPermissaoCadastrarEmpresa()) {
            throw new ValidacaoException("Você não tem permissão para salvar uma empresa.");
        }
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
            throw new ValidacaoException("Não é possível salvar a empresa pois já existe outra Razão Social salva.");
        }
    }

    private void validarEmpresaExistentePorCnpjAoSalvar(Empresa empresa) {
        if (empresaRepository.existsByCnpj(empresa.getCnpj())) {
            throw new ValidacaoException("Não é possível salvar a empresa pois já existe outro CNPJ salvo.");
        }
    }

    private void validarEmpresaExistentePorRazaoSocialAoEditar(Empresa empresa) {
        if (empresaRepository.existsByRazaoSocialAndIdNot(empresa.getRazaoSocial(), empresa.getId())) {
            throw new ValidacaoException("Não é possível editar a empresa pois já existe outra Razão Social salva.");
        }
    }

    private void validarEmpresaExistentePorCnpjAoEditar(Empresa empresa) {
        if (empresaRepository.existsByCnpjAndIdNot(empresa.getCnpj(), empresa.getId())) {
            throw new ValidacaoException("Não é possível editar a empresa pois já existe outro CNPJ salvo.");
        }
    }

    public EmpresaResponse buscarPorIdComSocios(Integer id) {
        var usuarioAutenticado = autenticacaoService.getUsuarioAutenticado();
        var empresa = buscarPorId(id);
        var socios = funcionarioRepository.buscarSociosDaEmpresa(id);
        validarPermissaoDoUsuarioParaVisualizarEmpresa(usuarioAutenticado, empresa, socios);
        return EmpresaResponse.of(empresa, socios);
    }

    public Page<EmpresaPageResponse> buscarTodas(PageRequest pageable, EmpresaFiltros filtros) {
        var usuarioAutenticado = autenticacaoService.getUsuarioAutenticado();
        tratarPermissoesBuscaEmpresas(usuarioAutenticado, filtros);
        return empresaRepository.findAll(filtros.toPredicate().build(), pageable)
            .map(EmpresaPageResponse::of);
    }

    private void tratarPermissoesBuscaEmpresas(UsuarioAutenticado usuarioAutenticado, EmpresaFiltros filtros) {
        if (!usuarioAutenticado.isAdmin()) {
            if (usuarioAutenticado.isProprietario()) {
                filtros.setProprietarioId(usuarioAutenticado.getId());
            }
            if (usuarioAutenticado.isSocio()) {
                filtros.setSocioId(usuarioAutenticado.getId());
            }
        }
    }

    private void validarPermissaoDoUsuarioParaVisualizarEmpresa(UsuarioAutenticado usuarioAutenticado,
                                                                Empresa empresa,
                                                                List<SocioResponse> socios) {
        if (!usuarioAutenticado.isAdmin()
            && !isProprietario(usuarioAutenticado, empresa)
            || !isSocioEmpresa(usuarioAutenticado, socios)) {
            throw new ValidacaoException("Você não tem permissão para visualizar esta empresa.");
        }
    }

    private boolean isProprietario(UsuarioAutenticado usuarioAutenticado, Empresa empresa) {
        return usuarioAutenticado.getId().equals(empresa.getUsuario().getId());
    }

    private boolean isSocioEmpresa(UsuarioAutenticado usuarioAutenticado, List<SocioResponse> socios) {
        return socios
            .stream()
            .map(SocioResponse::getSocioId)
            .anyMatch(socioId -> usuarioAutenticado.getId().equals(socioId));
    }

    public Empresa buscarPorId(Integer id) {
        return empresaRepository
            .findById(id).orElseThrow(() -> new ValidacaoException("A empresa não foi encontrada."));
    }
}

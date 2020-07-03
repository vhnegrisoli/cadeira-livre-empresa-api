package br.com.cadeiralivreempresaapi.modulos.empresa.service;

import br.com.cadeiralivreempresaapi.config.exception.ValidacaoException;
import br.com.cadeiralivreempresaapi.modulos.comum.dto.PageRequest;
import br.com.cadeiralivreempresaapi.modulos.comum.response.SuccessResponseDetails;
import br.com.cadeiralivreempresaapi.modulos.empresa.dto.EmpresaFiltros;
import br.com.cadeiralivreempresaapi.modulos.empresa.dto.EmpresaPageResponse;
import br.com.cadeiralivreempresaapi.modulos.empresa.dto.EmpresaRequest;
import br.com.cadeiralivreempresaapi.modulos.empresa.dto.EmpresaResponse;
import br.com.cadeiralivreempresaapi.modulos.empresa.model.Empresa;
import br.com.cadeiralivreempresaapi.modulos.empresa.repository.EmpresaRepository;
import br.com.cadeiralivreempresaapi.modulos.usuario.dto.UsuarioAutenticado;
import br.com.cadeiralivreempresaapi.modulos.usuario.model.Usuario;
import br.com.cadeiralivreempresaapi.modulos.usuario.service.AutenticacaoService;
import br.com.cadeiralivreempresaapi.modulos.usuario.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class EmpresaService {

    @Autowired
    private EmpresaRepository empresaRepository;
    @Autowired
    private UsuarioService usuarioService;
    @Autowired
    private AutenticacaoService autenticacaoService;

    @Transactional
    public SuccessResponseDetails salvar(EmpresaRequest request) {
        var empresa = Empresa.of(request);
        empresa.adicionarProprietario(usuarioService.buscarPorId(autenticacaoService.getUsuarioAutenticadoId()));
        validarNovaEmpresa(empresa);
        empresaRepository.save(empresa);
        return new SuccessResponseDetails("A empresa " + empresa.getNome() + " foi criada com sucesso!");
    }

    @Transactional
    public SuccessResponseDetails editar(EmpresaRequest request, Integer id) {
        var empresa = Empresa.of(request);
        empresa.setId(id);
        validarPermissaoDoUsuario(autenticacaoService.getUsuarioAutenticado(), id);
        validarEdicaoEmpresa(empresa);
        var empresaExistente = buscarPorId(id);
        empresa.setSocios(empresaExistente.getSocios());
        empresa.setSituacao(empresaExistente.getSituacao());
        empresaRepository.save(empresa);
        return new SuccessResponseDetails("A empresa " + empresa.getNome() + " foi alterada com sucesso!");
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
        validarPermissaoDoUsuario(usuarioAutenticado, empresa.getId());
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
        return empresaRepository
            .findById(id).orElseThrow(() -> new ValidacaoException("A empresa não foi encontrada."));
    }

    private void validarPermissaoDoUsuario(UsuarioAutenticado usuarioAutenticado, Integer empresaId) {
        if (!usuarioAutenticado.isAdmin()
            && !empresaRepository.existsByIdAndSocios(empresaId, Usuario.of(usuarioAutenticado))) {
            throw new ValidacaoException("Usuário sem permissão para visualizar essa empresa.");
        }
    }

    public void inserirSocio(Usuario usuario, Integer empresaId) {
        var empresa = buscarPorId(empresaId);
        validarPermissaoDoUsuario(autenticacaoService.getUsuarioAutenticado(), empresa.getId());
        empresa.getSocios().add(usuario);
        empresaRepository.save(empresa);
    }
}

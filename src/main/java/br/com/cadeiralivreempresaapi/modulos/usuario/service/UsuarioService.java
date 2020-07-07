package br.com.cadeiralivreempresaapi.modulos.usuario.service;

import br.com.cadeiralivreempresaapi.modulos.comum.dto.PageRequest;
import br.com.cadeiralivreempresaapi.modulos.comum.response.SuccessResponseDetails;
import br.com.cadeiralivreempresaapi.modulos.empresa.service.EmpresaService;
import br.com.cadeiralivreempresaapi.modulos.funcionario.service.FuncionarioService;
import br.com.cadeiralivreempresaapi.modulos.usuario.dto.UsuarioAutenticado;
import br.com.cadeiralivreempresaapi.modulos.usuario.dto.UsuarioFiltros;
import br.com.cadeiralivreempresaapi.modulos.usuario.dto.UsuarioRequest;
import br.com.cadeiralivreempresaapi.modulos.usuario.model.Usuario;
import br.com.cadeiralivreempresaapi.modulos.usuario.repository.UsuarioRepository;
import br.com.caelum.stella.validation.CPFValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static br.com.cadeiralivreempresaapi.modulos.empresa.exception.EmpresaMessages.PROPRIETARIO_CRIADO_SUCESSO;
import static br.com.cadeiralivreempresaapi.modulos.empresa.exception.EmpresaMessages.SOCIO_CRIADO_SUCESSO;
import static br.com.cadeiralivreempresaapi.modulos.funcionario.exception.FuncionarioMessages.FUNCIONARIO_CRIADO_SUCESSO;
import static br.com.cadeiralivreempresaapi.modulos.usuario.enums.EPermissao.*;
import static br.com.cadeiralivreempresaapi.modulos.usuario.exception.UsuarioMessages.*;
import static br.com.cadeiralivreempresaapi.modulos.usuario.model.Usuario.of;
import static org.springframework.util.ObjectUtils.isEmpty;

@Slf4j
@Service
@SuppressWarnings("PMD.TooManyStaticImports")
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private AutenticacaoService autenticacaoService;
    @Autowired
    private PermissaoService permissaoService;
    @Autowired
    private EmpresaService empresaService;
    @Autowired
    private FuncionarioService funcionarioService;

    @Transactional
    public SuccessResponseDetails salvarProprietario(UsuarioRequest usuarioRequest) {
        var usuario = of(usuarioRequest);
        usuario.setPermissoes(List.of(permissaoService.buscarPorCodigo(PROPRIETARIO)));
        salvarUsuario(usuario);
        return PROPRIETARIO_CRIADO_SUCESSO;
    }

    @Transactional
    public SuccessResponseDetails salavarSocio(UsuarioRequest usuarioRequest, Integer empresaId) {
        var usuario = of(usuarioRequest);
        usuario.setPermissoes(List.of(permissaoService.buscarPorCodigo(SOCIO)));
        empresaService.inserirSocio(salvarUsuario(usuario), empresaId);
        return SOCIO_CRIADO_SUCESSO;
    }

    @Transactional
    public SuccessResponseDetails salavarFuncionario(UsuarioRequest usuarioRequest, Integer empresaId) {
        var usuario = of(usuarioRequest);
        usuario.setPermissoes(List.of(permissaoService.buscarPorCodigo(FUNCIONARIO)));
        funcionarioService.salvarFuncionario(salvarUsuario(usuario), empresaId);
        return FUNCIONARIO_CRIADO_SUCESSO;
    }

    private Usuario salvarUsuario(Usuario usuario) {
        validarDadosUsuario(usuario);
        usuario.setSenha(passwordEncoder.encode(usuario.getSenha()));
        return usuarioRepository.save(usuario);
    }

    @Transactional
    public SuccessResponseDetails editarDadosUsuario(UsuarioRequest usuarioRequest, Integer id) {
        validarPermissoesUsuario(id);
        usuarioRequest.setId(id);
        var usuario = of(usuarioRequest);
        validarDadosUsuario(usuario);
        usuario.setSenha(passwordEncoder.encode(usuario.getSenha()));
        usuario.setPermissoes(buscarPorId(usuarioRequest.getId()).getPermissoes());
        usuario.setSituacao(buscarPorId(usuario.getId()).getSituacao());
        usuarioRepository.save(usuario);
        return USUARIO_ALTERADO_SUCESSO;
    }

    private void validarDadosUsuario(Usuario usuario) {
        validarCpf(usuario);
        validarDataNascimento(usuario);
        validarEmailExistente(usuario);
        validarCpfExistente(usuario);
    }

    private void validarCpf(Usuario usuario) {
        if (!isEmpty(usuario.getCpf())) {
            try {
                var cpfValidator = new CPFValidator();
                cpfValidator.assertValid(usuario.getCpf());
            } catch (Exception ex) {
                throw CPF_INVALIDO;
            }
        } else {
            throw CPF_NAO_INFORMADO;
        }
    }

    private void validarDataNascimento(Usuario usuario) {
        if (usuario.getDataNascimento().isEqual(LocalDate.now())) {
            throw DATA_NASCIMENTO_IGUAL_HOJE;
        }
        if (usuario.getDataNascimento().isAfter(LocalDate.now())) {
            throw DATA_NASCIMENTO_MAIOR_HOJE;
        }
    }

    private void validarEmailExistente(Usuario usuario) {
        usuarioRepository.findByEmail(usuario.getEmail())
            .ifPresent(usuarioExistente -> {
                if (usuario.isNovoCadastro() || !usuario.getId().equals(usuarioExistente.getId())) {
                    throw EMAIL_JA_CADASTRADO;
                }
            });
    }

    private void validarCpfExistente(Usuario usuario) {
        usuarioRepository.findByCpf(usuario.getCpf())
            .ifPresent(usuarioExistente -> {
                if (usuario.isNovoCadastro() || !usuario.getId().equals(usuarioExistente.getId())) {
                    throw CPF_JA_CADASTRADO;
                }
            });
    }

    public Usuario buscarPorId(Integer id) {
        return usuarioRepository.findById(id)
            .orElseThrow(() -> USUARIO_NAO_ENCONTRADO);
    }

    @Transactional
    public UsuarioAutenticado getUsuarioAutenticadoAtualizaUltimaData() {
        var usuarioAtualizado = usuarioRepository
            .findById(autenticacaoService.getUsuarioAutenticadoId())
            .orElseThrow(() -> USUARIO_NAO_ENCONTRADO);
        return UsuarioAutenticado.of(atualizarUltimoAcesso(usuarioAtualizado));
    }

    @Transactional
    private Usuario atualizarUltimoAcesso(Usuario usuario) {
        usuario.setUltimoAcesso(LocalDateTime.now());
        return usuarioRepository.save(usuario);
    }

    public Page<Usuario> getUsuarios(PageRequest pageable, UsuarioFiltros filtros) {
        return usuarioRepository.findAll(filtros.toPredicate().build(), pageable);
    }

    @Transactional
    public SuccessResponseDetails atualizarTokenNotificacao(String token) {
        var usuario = usuarioRepository
            .findById(autenticacaoService.getUsuarioAutenticadoId())
            .orElseThrow(() -> USUARIO_NAO_ENCONTRADO);
        if (!usuario.possuiToken(token)) {
            usuario.setTokenNotificacao(token);
            usuarioRepository.save(usuario);
            return TOKEN_ATUALIZADO;
        }
        return TOKEN_EXISTENTE;
    }

    private void validarPermissoesUsuario(Integer usuarioId) {
        var usuarioAutenticado = autenticacaoService.getUsuarioAutenticado();
        if (!usuarioAutenticado.isAdmin() && !usuarioAutenticado.getId().equals(usuarioId)) {
            throw SEM_PERMISSAO_EDITAR;
        }
    }
}


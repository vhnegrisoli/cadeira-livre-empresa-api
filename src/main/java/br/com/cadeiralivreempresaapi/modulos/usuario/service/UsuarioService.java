package br.com.cadeiralivreempresaapi.modulos.usuario.service;

import br.com.cadeiralivreempresaapi.config.exception.ValidacaoException;
import br.com.cadeiralivreempresaapi.modulos.comum.dto.PageRequest;
import br.com.cadeiralivreempresaapi.modulos.comum.response.SuccessResponseDetails;
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

import static br.com.cadeiralivreempresaapi.modulos.usuario.exception.UsuarioException.*;
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

    @Transactional
    public void save(UsuarioRequest usuarioRequest) {
        var usuario = of(usuarioRequest);
        validarDadosUsuario(usuario);
        usuario.setSenha(passwordEncoder.encode(usuarioRequest.getSenha()));
        usuario.setDataCadastro(LocalDateTime.now());
        usuario.setUltimoAcesso(LocalDateTime.now());
        usuarioRepository.save(usuario);
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
                throw new ValidacaoException("O CPF está inválido.");
            }
        } else {
            throw new ValidacaoException("O CPF deve ser informado.");
        }
    }

    private void validarDataNascimento(Usuario usuario) {
        if (usuario.getDataNascimento().isEqual(LocalDate.now())) {
            throw USUARIO_DATA_NASCIMENTO_IGUAL_HOJE.getException();
        }
        if (usuario.getDataNascimento().isAfter(LocalDate.now())) {
            throw USUARIO_DATA_NASCIMENTO_MAIOR_HOJE.getException();
        }
    }

    private void validarEmailExistente(Usuario usuario) {
        usuarioRepository.findByEmail(usuario.getEmail())
            .ifPresent(usuarioExistente -> {
                if (usuario.isNovoCadastro() || !usuario.getId().equals(usuarioExistente.getId())) {
                    throw USUARIO_EMAIL_JA_CADASTRADO.getException();
                }
            });
    }

    private void validarCpfExistente(Usuario usuario) {
        usuarioRepository.findByCpf(usuario.getCpf())
            .ifPresent(usuarioExistente -> {
                if (usuario.isNovoCadastro() || !usuario.getId().equals(usuarioExistente.getId())) {
                    throw USUARIO_CPF_JA_CADASTRADO.getException();
                }
            });
    }

    @Transactional
    public UsuarioAutenticado getUsuarioAutenticadoAtualizaUltimaData() {
        var usuarioAtualizado = usuarioRepository
            .findById(autenticacaoService.getUsuarioAutenticadoId())
            .orElseThrow(USUARIO_NAO_ENCONTRADO::getException);
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
            .orElseThrow(USUARIO_NAO_ENCONTRADO::getException);
        if (!usuario.possuiToken(token)) {
            usuario.setTokenNotificacao(token);
            usuarioRepository.save(usuario);
            return new SuccessResponseDetails("Token de notificação atualizado com sucesso!");
        }
        return new SuccessResponseDetails("O usuário já possui esse token de notificação.");
    }
}


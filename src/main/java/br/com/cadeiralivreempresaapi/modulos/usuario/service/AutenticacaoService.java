package br.com.cadeiralivreempresaapi.modulos.usuario.service;

import br.com.cadeiralivreempresaapi.modulos.comum.response.SuccessResponseDetails;
import br.com.cadeiralivreempresaapi.modulos.usuario.dto.TokenResponse;
import br.com.cadeiralivreempresaapi.modulos.usuario.dto.UsuarioAutenticado;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

import static br.com.cadeiralivreempresaapi.modulos.usuario.dto.UsuarioAutenticado.of;
import static br.com.cadeiralivreempresaapi.modulos.usuario.exception.UsuarioException.USUARIO_NAO_ENCONTRADO;
import static br.com.cadeiralivreempresaapi.modulos.usuario.exception.UsuarioException.USUARIO_SEM_SESSAO;
import static org.springframework.util.ObjectUtils.isEmpty;

@Slf4j
@Service
public class AutenticacaoService {

    private static final String ANONYMOUS_USER = "anonymousUser";
    private static final String AUTHORIZATION_HEADER = "Authorization";

    @Autowired
    private TokenStore tokenStore;

    public UsuarioAutenticado getUsuarioAutenticado() {
        UserDetails usuarioAutenticado = null;
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        try {
            if (principal instanceof UserDetails) {
                usuarioAutenticado = (UserDetails) principal;
            }
        } catch (Exception ex) {
            log.error(ex.getMessage());
            throw USUARIO_SEM_SESSAO.getException();
        }
        if (isEmpty(usuarioAutenticado)) {
            throw USUARIO_NAO_ENCONTRADO.getException();
        } else {
            return of(usuarioAutenticado);
        }
    }

    public boolean existeUsuarioAutenticado() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        return !isEmpty(authentication) && !authentication.getName().equals(ANONYMOUS_USER);
    }

    public Integer getUsuarioAutenticadoId() {
        return getUsuarioAutenticado().getId();
    }

    public SuccessResponseDetails logout(HttpServletRequest request) {
        var usuario = getUsuarioAutenticado();
        var oauth2AccessToken = tokenStore.readAccessToken(getTokenHeader(request));
        tokenStore.removeAccessToken(oauth2AccessToken);
        return new SuccessResponseDetails("O usuário " + usuario.getNome() + " foi deslogado com sucesso!"
            + " Token revogada: " + oauth2AccessToken.getValue());
    }

    private String getTokenHeader(HttpServletRequest request) {
        return new TokenResponse(request.getHeader(AUTHORIZATION_HEADER)).getToken();
    }
}

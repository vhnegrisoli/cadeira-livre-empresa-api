package br.com.cadeiralivreempresaapi.modulos.usuario.controller;

import br.com.cadeiralivreempresaapi.modulos.comum.dto.PageRequest;
import br.com.cadeiralivreempresaapi.modulos.comum.response.SuccessResponseDetails;
import br.com.cadeiralivreempresaapi.modulos.usuario.dto.TokenResponse;
import br.com.cadeiralivreempresaapi.modulos.usuario.dto.UsuarioAutenticado;
import br.com.cadeiralivreempresaapi.modulos.usuario.dto.UsuarioFiltros;
import br.com.cadeiralivreempresaapi.modulos.usuario.dto.UsuarioRequest;
import br.com.cadeiralivreempresaapi.modulos.usuario.model.Usuario;
import br.com.cadeiralivreempresaapi.modulos.usuario.service.AutenticacaoService;
import br.com.cadeiralivreempresaapi.modulos.usuario.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    private static final String AUTHORIZATION = "authorization";

    @Autowired
    private UsuarioService usuarioService;
    @Autowired
    private AutenticacaoService autenticacaoService;

    @GetMapping
    public Page<Usuario> getUsuarios(PageRequest pageable, UsuarioFiltros usuarioFiltros) {
        return usuarioService.getUsuarios(pageable, usuarioFiltros);
    }

    @GetMapping("/check-session")
    public String checkSession() {
        return "O usuário " + autenticacaoService.getUsuarioAutenticado().getNome() + " está logado.";
    }

    @GetMapping("logout")
    public SuccessResponseDetails logout(HttpServletRequest request) {
        return autenticacaoService.logout(request);
    }

    @PostMapping("/novo")
     public SuccessResponseDetails novoUsuario(@RequestBody UsuarioRequest usuarioRequest) {
        usuarioService.save(usuarioRequest);
        return new SuccessResponseDetails("Usuário inserido com sucesso!");
    }

    @PutMapping("/alterar-acesso")
    public SuccessResponseDetails alterarDadosUsuario(@RequestBody UsuarioRequest usuarioRequest) {
        usuarioService.save(usuarioRequest);
        return new SuccessResponseDetails("Usuário alterado com sucesso!");
    }

    @GetMapping("/get-token")
    public TokenResponse getAuthorizationToken(@RequestHeader Map<String, String> headers) {
        return new TokenResponse(headers.get(AUTHORIZATION));
    }

    @GetMapping("/usuario-autenticado")
    public UsuarioAutenticado getUsuarioAutenticado() {
        return usuarioService.getUsuarioAutenticadoAtualizaUltimaData();
    }

    @GetMapping("/is-authenticated")
    public boolean verificarSeEstaAutenticado() {
        return autenticacaoService.existeUsuarioAutenticado();
    }

    @PutMapping("atualizar-token-notificacao")
    public SuccessResponseDetails atualizarTokenNotificacao(@RequestParam("token") String token) {
        return usuarioService.atualizarTokenNotificacao(token);
    }
}

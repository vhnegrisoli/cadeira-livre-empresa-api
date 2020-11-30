package br.com.cadeiralivreempresaapi.modulos.jwt.controller;

import br.com.cadeiralivreempresaapi.modulos.jwt.dto.JwtUsuarioResponse;
import br.com.cadeiralivreempresaapi.modulos.jwt.service.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/jwt")
public class JwtController {

    @Autowired
    private JwtService jwtService;

    @GetMapping
    public String gerarTokenTeste() {
        return jwtService.gerarTokenTeste();
    }

    @GetMapping("validar/{jwt}")
    public Boolean validarToken(@PathVariable String jwt) {
        return jwtService.verificarTokenValida(jwt);
    }

    @GetMapping("dados/{jwt}")
    public JwtUsuarioResponse recuperarDadosDoUsuarioDoToken(@PathVariable String jwt) {
        return jwtService.recuperarDadosDoUsuarioDoToken(jwt);
    }
}

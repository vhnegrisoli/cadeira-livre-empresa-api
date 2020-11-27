package br.com.cadeiralivreempresaapi.modulos.jwt.service;

import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/jwt")
public class JwtController {

    @Autowired
    private JwtService jwtService;

    @GetMapping("{jwt}")
    public JwtUsuarioResponse parseJwt(@PathVariable String jwt) {
        return jwtService.parseJwt(jwt);
    }
}

package br.com.cadeiralivreempresaapi.modulos.jwt.service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class JwtService {

    @Value("${jwt.secret}")
    private String secret;

    public JwtUsuarioResponse parseJwt(String jwt) {
        var jwtBody = Jwts
            .parserBuilder()
            .setSigningKey(Keys.hmacShaKeyFor(secret.getBytes()))
            .build()
            .parseClaimsJws(jwt)
            .getBody();
        return JwtUsuarioResponse.of(jwtBody);
    }
}

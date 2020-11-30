package br.com.cadeiralivreempresaapi.modulos.jwt.service;

import br.com.cadeiralivreempresaapi.config.exception.ValidacaoException;
import br.com.cadeiralivreempresaapi.modulos.jwt.dto.JwtUsuarioResponse;
import br.com.cadeiralivreempresaapi.modulos.jwt.repository.UsuarioLoginJwtRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static br.com.cadeiralivreempresaapi.modulos.comum.util.DataUtil.converterParaLocalDateTime;
import static br.com.cadeiralivreempresaapi.modulos.jwt.messages.JwtMessages.ERRO_DESCRIPTOGRAFAR_TOKEN;
import static br.com.cadeiralivreempresaapi.modulos.jwt.messages.JwtMessages.TOKEN_INVALIDA;

@Slf4j
@Service
public class JwtService {

    private static final Integer CINCO_HORAS = 18000000;

    @Autowired
    private UsuarioLoginJwtRepository usuarioLoginJwtRepository;

    @Value("${jwt.secret}")
    private String secret;

    public String gerarTokenTeste() {
        var uuid = UUID.randomUUID();
        var dados = gerarMock(uuid);
        return Jwts
            .builder()
            .setClaims(dados)
            .setIssuedAt(new Date(System.currentTimeMillis()))
            .setExpiration(new Date(System.currentTimeMillis() + CINCO_HORAS))
            .signWith(Keys.hmacShaKeyFor(secret.getBytes()))
            .compact();
    }

    private Map<String, Object> gerarMock(UUID uuid) {
        var usuario = new HashMap<String, Object>();
        usuario.put("id", uuid);
        usuario.put("nome", "Victor Hugo Negrisoli");
        usuario.put("email", "vhnegrisoli@gmail.com");
        usuario.put("cpf", "103.324.589-54");
        return usuario;
    }

    public JwtUsuarioResponse recuperarDadosDoUsuarioDoToken(String jwt) {
        if (verificarTokenValida(jwt)) {
            return JwtUsuarioResponse.of(descriptografarJwt(jwt).getBody());
        }
        throw TOKEN_INVALIDA;
    }

    public Boolean verificarTokenValida(String token) {
        var dadosUsuario = descriptografarJwt(token).getBody();
        var dataExpiracao = converterParaLocalDateTime(dadosUsuario.getExpiration());
        return dataExpiracao.isAfter(LocalDateTime.now());
    }

    public Jws<Claims> descriptografarJwt(String jwt) {
        try {
            return Jwts
                .parserBuilder()
                .setSigningKey(Keys.hmacShaKeyFor(secret.getBytes()))
                .build()
                .parseClaimsJws(jwt);
        } catch (Exception ex) {
            log.error("Erro ao tentar descriptografar token.", ex);
            throw ERRO_DESCRIPTOGRAFAR_TOKEN;
        }
    }

    @Transactional
    private void salvarSessaoUsuario(JwtUsuarioResponse response) {

    }
}
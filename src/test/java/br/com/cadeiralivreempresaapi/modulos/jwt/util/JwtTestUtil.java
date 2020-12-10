package br.com.cadeiralivreempresaapi.modulos.jwt.util;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class JwtTestUtil {

    private static final String JWT_SECRET = "Y2FkZWlyYS1saXZyZS11c3VhcmlvLWFwaS10ZXN0ZQ==";
    private static final Integer CINCO_HORAS = 18000000;

    public static String gerarTokenTeste() {
        var uuid = "5cd48099-1009-43c4-b979-f68148a2a81d";
        var dados = gerarMock(uuid);
        return Jwts
            .builder()
            .setClaims(dados)
            .setIssuedAt(new Date(System.currentTimeMillis()))
            .setExpiration(new Date(System.currentTimeMillis() + CINCO_HORAS))
            .signWith(Keys.hmacShaKeyFor(JWT_SECRET.getBytes()))
            .compact();
    }

    private static Map<String, Object> gerarMock(String uuid) {
        var usuario = new HashMap<String, Object>();
        usuario.put("id", uuid);
        usuario.put("nome", "Victor Hugo Negrisoli");
        usuario.put("email", "vhnegrisoli@gmail.com");
        usuario.put("cpf", "103.324.589-54");
        return usuario;
    }
}

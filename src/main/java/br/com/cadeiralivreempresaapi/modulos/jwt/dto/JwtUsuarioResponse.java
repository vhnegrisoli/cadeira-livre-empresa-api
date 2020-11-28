package br.com.cadeiralivreempresaapi.modulos.jwt.service;

import io.jsonwebtoken.Claims;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JwtUsuarioResponse {

    private Integer id;
    private String nome;
    private String email;
    private String cpf;

    public static JwtUsuarioResponse of(Claims jwtUsuario) {
        return JwtUsuarioResponse
            .builder()
            .id((int) jwtUsuario.get("id"))
            .nome((String) jwtUsuario.get("nome"))
            .email((String) jwtUsuario.get("email"))
            .nome((String) jwtUsuario.get("cpf"))
            .build();
    }
}

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

    private String nome;
    private String email;
    private Integer id;
    private List<String> permissoes;

    public static JwtUsuarioResponse of(Claims jwtUsuario) {
        return JwtUsuarioResponse
            .builder()
            .id((int) jwtUsuario.get("id"))
            .nome((String) jwtUsuario.get("usuario"))
            .email((String) jwtUsuario.get("email"))
            .permissoes((ArrayList<String>) jwtUsuario.get("permissoes"))
            .build();
    }
}

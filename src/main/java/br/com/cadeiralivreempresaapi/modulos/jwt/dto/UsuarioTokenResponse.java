package br.com.cadeiralivreempresaapi.modulos.jwt.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioTokenResponse {

    private String usuarioId;
    private String token;
}

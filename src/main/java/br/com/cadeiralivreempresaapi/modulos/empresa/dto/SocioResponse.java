package br.com.cadeiralivreempresaapi.modulos.empresa.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SocioResponse {

    private Integer socioId;
    private String socioNome;
    private String socioEmail;
    private String socioCpf;
}

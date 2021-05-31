package br.com.cadeiralivreempresaapi.modulos.empresa.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import static org.springframework.util.ObjectUtils.isEmpty;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EnderecoRequest {

    private String estado;
    private String cidade;
    private String bairro;
    private String rua;
    private String numeroRua;
    private String cep;
    private String complemento;
}

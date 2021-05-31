package br.com.cadeiralivreempresaapi.modulos.transacao.dto;

import br.com.cadeiralivreempresaapi.modulos.empresa.model.Empresa;
import br.com.cadeiralivreempresaapi.modulos.empresa.model.Endereco;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import static org.springframework.util.ObjectUtils.isEmpty;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CobrancaRequest {

    private String nome;
    private EnderecoCobrancaRequest endereco;

    public static CobrancaRequest converterDe(Empresa empresa, Endereco endereco) {
        return CobrancaRequest
            .builder()
            .nome(isEmpty(empresa.getRazaoSocial()) ? empresa.getNome() : empresa.getRazaoSocial())
            .endereco(EnderecoCobrancaRequest.converterDe(endereco))
            .build();
    }
}

package br.com.cadeiralivreempresaapi.modulos.empresa.dto;

import br.com.cadeiralivreempresaapi.modulos.empresa.enums.ETipoEmpresa;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmpresaRequest {

    private Integer id;
    private String nome;
    private String cnpj;
    private String razaoSocial;
    private ETipoEmpresa tipoEmpresa;
}

package br.com.cadeiralivreempresaapi.modulos.empresa.dto;

import br.com.cadeiralivreempresaapi.modulos.empresa.enums.ETipoEmpresa;
import lombok.Data;

@Data
public class EmpresaRequest {

    private Integer id;
    private String nome;
    private String cnpj;
    private String razaoSocial;
    private ETipoEmpresa tipoEmpresa;
}

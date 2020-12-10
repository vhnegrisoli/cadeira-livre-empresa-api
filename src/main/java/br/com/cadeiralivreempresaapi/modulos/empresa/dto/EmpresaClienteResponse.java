package br.com.cadeiralivreempresaapi.modulos.empresa.dto;

import br.com.cadeiralivreempresaapi.modulos.empresa.model.Empresa;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.BeanUtils;

import java.util.List;
import java.util.stream.Collectors;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmpresaClienteResponse {

    private Integer id;
    private String nome;
    private String cnpj;
    private String razaoSocial;
    private List<ProprietarioSocioClienteResponse> proprietarioSocios;
    private String tipoEmpresa;

    public static EmpresaClienteResponse of(Empresa empresa) {
        var response = new EmpresaClienteResponse();
        BeanUtils.copyProperties(empresa, response);
        response.setProprietarioSocios(empresa
            .getSocios()
            .stream()
            .map(ProprietarioSocioClienteResponse::of)
            .collect(Collectors.toList()));
        response.setTipoEmpresa(empresa.getTipoEmpresa().getTipoEmpresa());
        return response;
    }
}

package br.com.cadeiralivreempresaapi.modulos.empresa.dto;

import br.com.cadeiralivreempresaapi.modulos.empresa.enums.ESituacaoEmpresa;
import br.com.cadeiralivreempresaapi.modulos.empresa.enums.ETipoEmpresa;
import br.com.cadeiralivreempresaapi.modulos.empresa.model.Empresa;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.BeanUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmpresaResponse {

    private Integer id;
    private String nome;
    private String cnpj;
    private String razaoSocial;
    private ESituacaoEmpresa situacao;
    private List<ProprietarioSocioResponse> proprietarioSocios;
    @JsonFormat(pattern = "dd/MM/yyyy HH:mmn:ss")
    private LocalDateTime dataCadastro;
    private ETipoEmpresa tipoEmpresa;

    public static EmpresaResponse of(Empresa empresa) {
        var response = new EmpresaResponse();
        BeanUtils.copyProperties(empresa, response);
        response.setProprietarioSocios(empresa
            .getSocios()
            .stream()
            .map(ProprietarioSocioResponse::of)
            .collect(Collectors.toList()));
        return response;
    }
}

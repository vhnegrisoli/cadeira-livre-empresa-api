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

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmpresaResponse {

    private Integer id;
    private String nome;
    private String cnpj;
    private String razapSocial;
    private ESituacaoEmpresa situacao;
    private Integer proprietarioId;
    private String proprietarioNome;
    private String proprietarioEmail;
    private String proprietarioCpf;
    private List<SocioResponse> socios;
    @JsonFormat(pattern = "dd/MM/yyyy HH:mmn:ss")
    private LocalDateTime dataCadastro;
    private ETipoEmpresa tipoEmpresa;

    public static EmpresaResponse of(Empresa empresa, List<SocioResponse> socios) {
        var response = new EmpresaResponse();
        BeanUtils.copyProperties(empresa, response);
        response.setProprietarioId(empresa.getUsuario().getId());
        response.setProprietarioNome(empresa.getUsuario().getNome());
        response.setProprietarioEmail(empresa.getUsuario().getEmail());
        response.setProprietarioCpf(empresa.getUsuario().getCpf());
        response.setSocios(socios);
        return response;
    }
}

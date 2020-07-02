package br.com.cadeiralivreempresaapi.modulos.empresa.dto;

import br.com.cadeiralivreempresaapi.modulos.empresa.enums.ESituacaoEmpresa;
import br.com.cadeiralivreempresaapi.modulos.empresa.enums.ETipoEmpresa;
import br.com.cadeiralivreempresaapi.modulos.empresa.predicate.EmpresaPredicate;
import lombok.Data;

@Data
public class EmpresaFiltros {

    private String cnpj;
    private String nome;
    private ETipoEmpresa tipoEmpresa;
    private ESituacaoEmpresa situacao;
    private String socioProprietarioNome;
    private String socioProprietarioEmail;
    private String socioProprietarioCpf;
    private Integer proprietarioId;
    private Integer socioId;

    public EmpresaPredicate toPredicate() {
        return new EmpresaPredicate()
            .comCnpj(cnpj)
            .comNome(nome)
            .comTipoEmpresa(tipoEmpresa)
            .comSituacao(situacao)
            .comSocioProprietarioNome(socioProprietarioNome)
            .comSocioProprietarioEmail(socioProprietarioEmail)
            .comSocioProprietarioCpf(socioProprietarioCpf)
            .comProprietarioId(proprietarioId)
            .comSocioId(socioId);
    }
}

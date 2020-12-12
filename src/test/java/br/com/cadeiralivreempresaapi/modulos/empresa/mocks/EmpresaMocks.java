package br.com.cadeiralivreempresaapi.modulos.empresa.mocks;

import br.com.cadeiralivreempresaapi.modulos.empresa.dto.EmpresaPageResponse;
import br.com.cadeiralivreempresaapi.modulos.empresa.dto.EmpresaRequest;
import br.com.cadeiralivreempresaapi.modulos.empresa.dto.EmpresaResponse;
import br.com.cadeiralivreempresaapi.modulos.empresa.dto.ProprietarioSocioResponse;
import br.com.cadeiralivreempresaapi.modulos.empresa.enums.ESituacaoEmpresa;
import br.com.cadeiralivreempresaapi.modulos.empresa.enums.ETipoEmpresa;
import br.com.cadeiralivreempresaapi.modulos.empresa.model.Empresa;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static br.com.cadeiralivreempresaapi.modulos.usuario.mocks.UsuarioMocks.umUsuario;

public class EmpresaMocks {

    public static Empresa umaEmpresa() {
        return Empresa
            .builder()
            .id(1)
            .cnpj("82.765.926/0001-32")
            .dataCadastro(LocalDateTime.now())
            .nome("Empresa 01")
            .razaoSocial("Empresa 01")
            .situacao(ESituacaoEmpresa.ATIVA)
            .socios(Stream.of(umUsuario()).collect(Collectors.toList()))
            .tipoEmpresa(ETipoEmpresa.BARBEARIA)
            .tempoRefreshCadeiraLivre(10)
            .build();
    }

    public static EmpresaRequest umaEmpresaRequest() {
        return EmpresaRequest
            .builder()
            .id(1)
            .cnpj("82.765.926/0001-32")
            .nome("Empresa 01")
            .razaoSocial("Empresa 01")
            .tipoEmpresa(ETipoEmpresa.BARBEARIA)
            .tempoRefreshCadeiraLivre(10)
            .build();
    }

    public static EmpresaPageResponse umaEmpresaPageResponse() {
        return EmpresaPageResponse
            .builder()
            .id(1)
            .cnpj("82.765.926/0001-32")
            .nome("Empresa 01")
            .tipoEmpresa(ETipoEmpresa.BARBEARIA)
            .situacao(ESituacaoEmpresa.ATIVA)
            .build();
    }

    public static EmpresaResponse umaEmpresaResponse() {
        return EmpresaResponse
            .builder()
            .id(1)
            .cnpj("82.765.926/0001-32")
            .dataCadastro(LocalDateTime.now())
            .nome("Empresa 01")
            .razaoSocial("Empresa 01")
            .situacao(ESituacaoEmpresa.ATIVA)
            .proprietarioSocios(List.of(umProprietarioSocioResponse()))
            .tipoEmpresa(ETipoEmpresa.BARBEARIA)
            .build();
    }

    public static ProprietarioSocioResponse umProprietarioSocioResponse() {
        return ProprietarioSocioResponse.of(umUsuario());
    }
}

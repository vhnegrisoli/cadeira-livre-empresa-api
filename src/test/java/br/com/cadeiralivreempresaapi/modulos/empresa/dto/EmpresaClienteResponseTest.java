package br.com.cadeiralivreempresaapi.modulos.empresa.dto;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static br.com.cadeiralivreempresaapi.modulos.empresa.mocks.EmpresaMocks.umaEmpresa;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class EmpresaClienteResponseTest {

    @Test
    @DisplayName("Deve retornar objeto de response quando informar uma empresa")
    public void of_deveRetornarObjetoResponse_quandoInformarEmpresa() {
        var response = EmpresaClienteResponse.of(umaEmpresa());
        assertThat(response).isNotNull();
        assertThat(response.getId()).isEqualTo(1);
        assertThat(response.getNome()).isEqualTo("Empresa 01");
        assertThat(response.getRazaoSocial()).isEqualTo("Empresa 01");
        assertThat(response.getCnpj()).isEqualTo("82.765.926/0001-32");
        assertThat(response.getTipoEmpresa()).isEqualTo("Barbearia");
        assertThat(response.getProprietarioSocios()).isEqualTo(List.of(
            new ProprietarioSocioClienteResponse(1, "Usuario", "Sócio")
        ));
    }
}

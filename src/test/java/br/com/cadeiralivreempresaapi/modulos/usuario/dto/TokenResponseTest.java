package br.com.cadeiralivreempresaapi.modulos.usuario.dto;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class TokenResponseTest {

    @Test
    public void deveTestarBearerToken_quandoSolicitado() {
        var token = new TokenResponse("bearer token");
        assertThat(token).isNotNull();
        assertThat(token.getToken()).isEqualTo("token");
        assertThat(token.getBearerToken()).isEqualTo("bearer token");
    }
}

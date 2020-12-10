package br.com.cadeiralivreempresaapi.modulos.jwt.mocks;

import br.com.cadeiralivreempresaapi.modulos.jwt.dto.UsuarioTokenResponse;
import br.com.cadeiralivreempresaapi.modulos.jwt.model.UsuarioLoginJwt;

import static br.com.cadeiralivreempresaapi.modulos.jwt.util.JwtTestUtil.gerarTokenTeste;

public class JwtMocks {

    private static final String JWT = gerarTokenTeste();

    public static UsuarioLoginJwt umUsuarioLoginJwt(Boolean valida) {
        return UsuarioLoginJwt.gerarUsuario(umUsuarioTokenResponse(), valida);
    }

    public static UsuarioTokenResponse umUsuarioTokenResponse() {
        return UsuarioTokenResponse
            .builder()
            .usuarioId("5cd48099-1009-43c4-b979-f68148a2a81d")
            .token(JWT)
            .build();
    }
}

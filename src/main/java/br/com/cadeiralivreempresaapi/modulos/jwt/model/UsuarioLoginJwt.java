package br.com.cadeiralivreempresaapi.modulos.jwt.model;

import br.com.cadeiralivreempresaapi.modulos.jwt.dto.UsuarioTokenResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "USUARIO_LOGIN_JWT")
public class UsuarioLoginJwt {

    @Id
    @Column(name = "USUARIO_ID")
    private String usuarioId;

    @Column(name = "TOKEN", nullable = false, unique = true, length = 500)
    private String token;

    @Column(name = "TOKEN_VALIDA", nullable = false)
    private boolean tokenValida;

    public UsuarioLoginJwt gerarUsuarioJwt(UsuarioTokenResponse response) {
        return UsuarioLoginJwt
            .builder()
            .token(response.getToken())
            .build();
    }
}

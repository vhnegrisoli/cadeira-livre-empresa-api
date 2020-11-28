package br.com.cadeiralivreempresaapi.modulos.jwt;

import br.com.cadeiralivreempresaapi.modulos.jwt.dto.JwtUsuarioResponse;
import br.com.cadeiralivreempresaapi.modulos.jwt.dto.JwtValidadeResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioLoginJwt {

    @Id
    @Column(name = "USUARIO_ID", nullable = false)
    private Integer usuarioId;

    @Column(name = "TOKEN", nullable = false, unique = true)
    private String token;

    @Column(name = "TOKEN_VALIDA", nullable = false)
    private boolean tokenValida;

    public UsuarioLoginJwt atualizarLoginUsuario(JwtValidadeResponse response) {
        return UsuarioLoginJwt
            .builder()
            .token(response.getToken())
            .tokenValida(response.isValida())

    }
}

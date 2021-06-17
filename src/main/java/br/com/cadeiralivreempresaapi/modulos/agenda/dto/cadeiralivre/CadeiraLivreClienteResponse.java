package br.com.cadeiralivreempresaapi.modulos.agenda.dto.cadeiralivre;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CadeiraLivreClienteResponse {

    private Integer status;
    private List<CadeiraLivreResponse> cadeirasLivres;

    public static CadeiraLivreClienteResponse converterDe(List<CadeiraLivreResponse> cadeirasLivres) {
        return CadeiraLivreClienteResponse
            .builder()
            .status(HttpStatus.OK.value())
            .cadeirasLivres(cadeirasLivres)
            .build();
    }
}

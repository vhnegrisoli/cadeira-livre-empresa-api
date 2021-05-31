package br.com.cadeiralivreempresaapi.modulos.cep.client;

import br.com.cadeiralivreempresaapi.modulos.cep.dto.ViaCepResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Optional;

@FeignClient(
    name = "viaCepClient",
    contextId = "viaCepClient",
    url = "${app-config.services.viacep.url}"
)
public interface ViaCepClient {

    @GetMapping("{cep}/json")
    Optional<ViaCepResponse> consultarDadosPorCep(@PathVariable String cep);
}

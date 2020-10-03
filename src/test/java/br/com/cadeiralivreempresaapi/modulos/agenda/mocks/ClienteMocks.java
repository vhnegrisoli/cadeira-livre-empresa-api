package br.com.cadeiralivreempresaapi.modulos.agenda.mocks;

import br.com.cadeiralivreempresaapi.modulos.agenda.dto.ClienteRequest;

public class ClienteMocks {

    public static ClienteRequest umClienteRequest() {
        return ClienteRequest
            .builder()
            .id(1)
            .cpf("460.427.120-80")
            .email("cliente@gmail.com")
            .nome("Cliente")
            .build();
    }
}

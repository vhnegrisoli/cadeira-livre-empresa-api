package br.com.cadeiralivreempresaapi.modulos.cep.service;

import br.com.cadeiralivreempresaapi.config.exception.ValidacaoException;
import br.com.cadeiralivreempresaapi.modulos.cep.client.ViaCepClient;
import br.com.cadeiralivreempresaapi.modulos.cep.dto.CepResponse;
import br.com.cadeiralivreempresaapi.modulos.cep.dto.ViaCepResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ViaCepService {

    @Autowired
    private ViaCepClient viaCepClient;

    public CepResponse consultarDadosPorCep(String cep) {
        try {
            var cepNaoEncontrado = String.format("O cep %s não foi encontrado.", cep);
            var response =  viaCepClient
                .consultarDadosPorCep(cep)
                .orElseThrow(() -> new ValidacaoException(cepNaoEncontrado));
            if (response.isErro()) {
                throw new ValidacaoException(cepNaoEncontrado);
            }
            return CepResponse.converterDe(response);
        } catch (Exception ex) {
            var message = String.format("Erro ao tentar consultar o cep %s na API do ViaCep: %s", cep, ex.getMessage());
            log.error(message, ex);
            throw new ValidacaoException(message);
        }
    }
}

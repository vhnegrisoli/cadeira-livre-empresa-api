package br.com.cadeiralivreempresaapi.modulos.jwt.messages;

import br.com.cadeiralivreempresaapi.config.exception.ValidacaoException;

public interface JwtMessages {

    ValidacaoException ERRO_DESCRIPTOGRAFAR_TOKEN =
        new ValidacaoException("Erro ao tentar descriptografar o token.");
    ValidacaoException TOKEN_INVALIDA =
        new ValidacaoException("O token está inválido.");
}

package br.com.cadeiralivreempresaapi.config.exception;

import lombok.Data;

@Data
public class ValidacaoExceptionDetails {

    private int status;
    private String message;

}

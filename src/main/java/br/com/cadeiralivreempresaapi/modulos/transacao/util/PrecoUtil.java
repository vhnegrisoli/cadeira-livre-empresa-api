package br.com.cadeiralivreempresaapi.modulos.transacao.util;

import java.math.BigDecimal;

public class PrecoUtil {

    private static final String PONTO = ".";
    private static final String VIRGULA = ",";
    private static final String VAZIO = "";

    public static BigDecimal tratarValorTransacao(Double valor) {
        try {
            var valorString = valor.toString();
            valorString = valorString.replace(PONTO, VAZIO);
            valorString = valorString.replace(VIRGULA, VAZIO);
            return BigDecimal.valueOf(Long.parseLong(valorString));
        } catch (Exception ex) {
            return BigDecimal.ZERO;
        }
    }
}

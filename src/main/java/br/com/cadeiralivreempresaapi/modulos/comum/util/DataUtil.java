package br.com.cadeiralivreempresaapi.modulos.comum.util;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

public class DataUtil {

    public static LocalDateTime converterParaLocalDateTime(Date data) {
        return data
            .toInstant()
            .atZone(ZoneId.systemDefault())
            .toLocalDateTime();
    }
}

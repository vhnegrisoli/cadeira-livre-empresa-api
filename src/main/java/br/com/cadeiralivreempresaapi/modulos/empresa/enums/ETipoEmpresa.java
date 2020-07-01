package br.com.cadeiralivreempresaapi.modulos.empresa.enums;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum ETipoEmpresa {

    SALAO("Salão de Beleza"),
    BARBEARIA("Barbearia");

    private String tipoEmpresa;
}

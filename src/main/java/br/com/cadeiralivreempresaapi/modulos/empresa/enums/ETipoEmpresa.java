package br.com.cadeiralivreempresaapi.modulos.empresa.enums;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum ETipoEmpresa {

    SALAO("Salão de Beleza"),
    CABELO("Cabeleireiro/a"),
    BARBEARIA("Barbearia");

    private String tipoEmpresa;
}

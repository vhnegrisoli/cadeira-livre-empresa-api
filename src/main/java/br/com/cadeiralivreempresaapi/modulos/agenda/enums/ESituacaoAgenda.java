package br.com.cadeiralivreempresaapi.modulos.agenda.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum ESituacaoAgenda {

    DISPNIVEL("Disponível"),
    RESERVA("Reservada"),
    CANCELADA("Cancelada");

    @Getter
    private String descricaoSituacao;
}

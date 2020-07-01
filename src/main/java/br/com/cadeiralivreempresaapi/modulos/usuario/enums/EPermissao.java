package br.com.cadeiralivreempresaapi.modulos.usuario.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum EPermissao {

    PROPRIETARIO(2, "Proprietário"),
    ADMIN(1, "Administrador"),
    USER(3, "Usuário");

    @Getter
    private Integer id;
    @Getter
    private String descricao;
}

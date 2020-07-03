package br.com.cadeiralivreempresaapi.modulos.usuario.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum EPermissao {

    ADMIN(1, "Administrador"),
    PROPRIETARIO(2, "Proprietário"),
    SOCIO(3, "Sócio"),
    GERENTE(4, "Gerente"),
    FUNCIONARIO(5, "Funcionário");

    @Getter
    private Integer id;
    @Getter
    private String descricao;
}

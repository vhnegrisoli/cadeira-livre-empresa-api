package br.com.cadeiralivreempresaapi.modulos.usuario.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum EPermissao {

    PROPRIETARIO(2, "Proprietário"),
    ADMIN(1, "Administrador"),
    USER(3, "Usuário"),
    CLIENTE(4, "Cliente"),
    FUNCIONARIO(5, "Funcionário"),
    GERENTE(6, "Gerente"),
    SOCIO(7, "Sócio");

    @Getter
    private Integer id;
    @Getter
    private String descricao;
}

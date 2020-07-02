package br.com.cadeiralivreempresaapi.modulos.empresa.predicate;

import br.com.cadeiralivreempresaapi.config.PredicateBase;
import br.com.cadeiralivreempresaapi.modulos.empresa.enums.ESituacaoEmpresa;
import br.com.cadeiralivreempresaapi.modulos.empresa.enums.ETipoEmpresa;
import com.querydsl.jpa.JPAExpressions;

import static br.com.cadeiralivreempresaapi.modulos.empresa.model.QEmpresa.empresa;
import static br.com.cadeiralivreempresaapi.modulos.funcionario.model.QFuncionario.funcionario;
import static br.com.cadeiralivreempresaapi.modulos.usuario.model.QUsuario.usuario;
import static br.com.cadeiralivreempresaapi.modulos.usuario.utils.PermissaoUtils.PERMISSOES_SOCIO_PROPRIETARIO;
import static org.springframework.util.ObjectUtils.isEmpty;

@SuppressWarnings("PMD.TooManyStaticImports")
public class EmpresaPredicate extends PredicateBase {

    public EmpresaPredicate comCnpj(String cnpj) {
        if (!isEmpty(cnpj)) {
            builder.and(empresa.cnpj.containsIgnoreCase(cnpj));
        }
        return this;
    }

    public EmpresaPredicate comNome(String nome) {
        if (!isEmpty(nome)) {
            builder.and(empresa.nome.containsIgnoreCase(nome));
        }
        return this;
    }

    public EmpresaPredicate comTipoEmpresa(ETipoEmpresa tipoEmpresa) {
        if (!isEmpty(tipoEmpresa)) {
            builder.and(empresa.tipoEmpresa.eq(tipoEmpresa));
        }
        return this;
    }

    public EmpresaPredicate comSituacao(ESituacaoEmpresa situacao) {
        if (!isEmpty(situacao)) {
            builder.and(empresa.situacao.eq(situacao));
        }
        return this;
    }

    public EmpresaPredicate comSocioProprietarioNome(String nome) {
        if (!isEmpty(nome)) {
            builder
                .and(empresa.usuario.nome.containsIgnoreCase(nome))
                .or(empresa.id.in(
                    JPAExpressions.select(funcionario.empresa.id)
                        .from(funcionario)
                        .where(funcionario.usuario.nome.containsIgnoreCase(nome)
                            .and(usuario.permissoes.any().permissao.in(PERMISSOES_SOCIO_PROPRIETARIO)))
                    )
                );
        }
        return this;
    }

    public EmpresaPredicate comSocioProprietarioEmail(String email) {
        if (!isEmpty(email)) {
            builder
                .and(empresa.usuario.email.containsIgnoreCase(email))
                .or(empresa.id.in(
                    JPAExpressions.select(funcionario.empresa.id)
                        .from(funcionario)
                        .where(funcionario.usuario.email.containsIgnoreCase(email)
                            .and(usuario.permissoes.any().permissao.in(PERMISSOES_SOCIO_PROPRIETARIO)))
                    )
                );
        }
        return this;
    }

    public EmpresaPredicate comSocioProprietarioCpf(String cpf) {
        if (!isEmpty(cpf)) {
            builder
                .and(empresa.usuario.cpf.containsIgnoreCase(cpf))
                .or(empresa.id.in(
                    JPAExpressions.select(funcionario.empresa.id)
                        .from(funcionario)
                        .where(funcionario.usuario.cpf.containsIgnoreCase(cpf)
                            .and(usuario.permissoes.any().permissao.in(PERMISSOES_SOCIO_PROPRIETARIO)))
                    )
                );
        }
        return this;
    }

    public EmpresaPredicate comProprietarioId(Integer usuarioId) {
        if (!isEmpty(usuarioId)) {
            builder.and(empresa.usuario.id.eq(usuarioId));
        }
        return this;
    }

    public EmpresaPredicate comSocioId(Integer socioId) {
        if (!isEmpty(socioId)) {
            builder
                .and(empresa.id.in(
                    JPAExpressions.select(funcionario.empresa.id)
                        .from(funcionario)
                        .where(funcionario.usuario.id.eq(socioId)
                        )
                    )
            );
        }
        return this;
    }
}
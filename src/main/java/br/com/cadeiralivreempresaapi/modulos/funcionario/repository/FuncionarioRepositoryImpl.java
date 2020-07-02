package br.com.cadeiralivreempresaapi.modulos.funcionario.repository;

import br.com.cadeiralivreempresaapi.modulos.empresa.dto.SocioResponse;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

import static br.com.cadeiralivreempresaapi.modulos.empresa.model.QEmpresa.empresa;
import static br.com.cadeiralivreempresaapi.modulos.usuario.model.QUsuario.usuario;
import static br.com.cadeiralivreempresaapi.modulos.usuario.model.QPermissao.permissao1;
import static br.com.cadeiralivreempresaapi.modulos.funcionario.model.QFuncionario.funcionario;
import static br.com.cadeiralivreempresaapi.modulos.usuario.utils.PermissaoUtils.PERMISSOES_SOCIO_PROPRIETARIO;

@Repository
@SuppressWarnings("PMD.TooManyStaticImports")
public class FuncionarioRepositoryImpl implements FuncionarioRepositoryCustom {

    @Autowired
    private EntityManager entityManager;

    @Override
    public List<SocioResponse> buscarSociosDaEmpresa(Integer empresaId) {
        return new JPAQueryFactory(entityManager)
            .select(Projections.constructor(SocioResponse.class,
                usuario.id,
                usuario.nome,
                usuario.email,
                usuario.cpf)
            )
            .from(funcionario)
            .leftJoin(funcionario.usuario, usuario)
            .leftJoin(usuario.permissoes, permissao1)
            .leftJoin(funcionario.empresa, empresa)
            .where(empresa.id.eq(empresaId)
                .and(usuario.permissoes.any().permissao.in(PERMISSOES_SOCIO_PROPRIETARIO)))
            .fetch();
    }
}

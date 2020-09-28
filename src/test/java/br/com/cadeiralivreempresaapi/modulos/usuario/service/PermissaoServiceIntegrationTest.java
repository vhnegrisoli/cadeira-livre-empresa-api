package br.com.cadeiralivreempresaapi.modulos.usuario.service;

import br.com.cadeiralivreempresaapi.config.exception.ValidacaoException;
import br.com.cadeiralivreempresaapi.modulos.usuario.enums.EPermissao;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@Import(PermissaoService.class)
@Sql(scripts = {"classpath:/usuarios_tests.sql"})
@DataJpaTest
public class PermissaoServiceIntegrationTest {

    @Autowired
    private PermissaoService service;

    @Test
    public void buscarPorId_deveRetornarPermissao_quandoEncontrado() {
        var permissao = service.buscarPorId(1);
        assertThat(permissao).isNotNull();
        assertThat(permissao.getId()).isEqualTo(1);
        assertThat(permissao.getDescricao()).isEqualTo("Administrador");
        assertThat(permissao.getPermissao()).isEqualTo(EPermissao.ADMIN);
    }

    @Test
    public void buscarPorId_deveLancarException_quandoNaoForEncontrado() {
        assertThatExceptionOfType(ValidacaoException.class)
            .isThrownBy(() -> service.buscarPorId(1000))
            .withMessage("A permissão não foi encontrada.");
    }

    @Test
    public void buscarPorCodigo_deveRetornarPermissao_quandoEncontrado() {
        var permissao = service.buscarPorCodigo(EPermissao.ADMIN);
        assertThat(permissao).isNotNull();
        assertThat(permissao.getId()).isEqualTo(1);
        assertThat(permissao.getDescricao()).isEqualTo("Administrador");
        assertThat(permissao.getPermissao()).isEqualTo(EPermissao.ADMIN);
    }

    @Test
    public void buscarPorCodigo_deveLancarException_quandoNaoForEncontrado() {
        assertThatExceptionOfType(ValidacaoException.class)
            .isThrownBy(() -> service.buscarPorCodigo(EPermissao.PERMISSAO_NAO_MAPEADA))
            .withMessage("A permissão não foi encontrada.");
    }
}

package br.com.cadeiralivreempresaapi.modulos.empresa.service;

import br.com.cadeiralivreempresaapi.config.exception.PermissaoException;
import br.com.cadeiralivreempresaapi.modulos.empresa.enums.ESituacaoEmpresa;
import br.com.cadeiralivreempresaapi.modulos.empresa.enums.ETipoEmpresa;
import br.com.cadeiralivreempresaapi.modulos.empresa.repository.EmpresaRepository;
import br.com.cadeiralivreempresaapi.modulos.usuario.service.AutenticacaoService;
import br.com.cadeiralivreempresaapi.modulos.usuario.service.UsuarioService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import static br.com.cadeiralivreempresaapi.modulos.usuario.mocks.UsuarioMocks.umUsuarioAutenticadoSocio;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.Mockito.when;

@DataJpaTest
@ActiveProfiles("test")
@Import(EmpresaService.class)
@ExtendWith(MockitoExtension.class)
@Sql(scripts = {
    "classpath:/usuarios_tests.sql",
    "classpath:/funcionarios_tests.sql"
})
public class EmpresaServiceIntegrationTest {

    @Autowired
    private EmpresaService service;
    @Autowired
    private EmpresaRepository empresaRepository;
    @MockBean
    private UsuarioService usuarioService;
    @MockBean
    private AutenticacaoService autenticacaoService;

    @Test
    @DisplayName("Deve salvar empresa quando dados estiverem corretos")
    public void buscarPorId_deveRetornarEmpresa_quandoEncontrarPorIdEUsuarioPossuirPermissao() {
        var usuarioAutenticado = umUsuarioAutenticadoSocio();
        usuarioAutenticado.setId(6);
        when(autenticacaoService.getUsuarioAutenticado()).thenReturn(usuarioAutenticado);
        var empresa = service.buscarPorId(4);
        assertThat(empresa).isNotNull();
        assertThat(empresa.getId()).isEqualTo(4);
        assertThat(empresa.getSocios().size()).isEqualTo(2);
        assertThat(empresa.getNome()).isEqualTo("Empresa 01 Edicao");
        assertThat(empresa.getRazaoSocial()).isEqualTo("Empresa 01");
        assertThat(empresa.getCnpj()).isEqualTo("26.343.835/0001-38");
        assertThat(empresa.getTipoEmpresa()).isEqualTo(ETipoEmpresa.SALAO);
        assertThat(empresa.getSituacao()).isEqualTo(ESituacaoEmpresa.ATIVA);
    }

    @Test
    @DisplayName("Deve salvar empresa quando dados estiverem corretos")
    public void buscarPorId_deveLancarException_quandoUsaurioNaoPossuirPermissao() {
        var usuarioAutenticado = umUsuarioAutenticadoSocio();
        usuarioAutenticado.setId(3);
        when(autenticacaoService.getUsuarioAutenticado()).thenReturn(usuarioAutenticado);

        assertThatExceptionOfType(PermissaoException.class)
            .isThrownBy(() -> service.buscarPorId(1000))
            .withMessage("Usuário sem permissão para visualizar essa empresa.");
    }
}

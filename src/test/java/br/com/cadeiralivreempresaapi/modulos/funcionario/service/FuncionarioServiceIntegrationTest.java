package br.com.cadeiralivreempresaapi.modulos.funcionario.service;

import br.com.cadeiralivreempresaapi.config.exception.PermissaoException;
import br.com.cadeiralivreempresaapi.modulos.empresa.service.EmpresaService;
import br.com.cadeiralivreempresaapi.modulos.funcionario.repository.FuncionarioRepository;
import br.com.cadeiralivreempresaapi.modulos.usuario.enums.ESituacaoUsuario;
import br.com.cadeiralivreempresaapi.modulos.usuario.service.AutenticacaoService;
import br.com.cadeiralivreempresaapi.modulos.usuario.service.PermissaoService;
import br.com.cadeiralivreempresaapi.modulos.usuario.service.UsuarioService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import static br.com.cadeiralivreempresaapi.modulos.usuario.mocks.UsuarioMocks.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.Mockito.when;

@DataJpaTest
@ActiveProfiles("test")
@Import({
    FuncionarioService.class,
    EmpresaService.class,
    UsuarioService.class
})
@ExtendWith(MockitoExtension.class)
@Sql(scripts = {
    "classpath:/usuarios_tests.sql",
    "classpath:/funcionarios_tests.sql"
})
public class FuncionarioServiceIntegrationTest {

    @Autowired
    private FuncionarioService service;
    @Autowired
    private FuncionarioRepository funcionarioRepository;
    @Autowired
    private UsuarioService usuarioService;
    @MockBean
    private PermissaoService permissaoService;
    @MockBean
    private PasswordEncoder passwordEncoder;
    @Autowired
    private EmpresaService empresaService;
    @MockBean
    private AutenticacaoService autenticacaoService;

    @Test
    @DisplayName("Deve buscar por ID quando informar ID e usuário for admin")
    public void buscarPorId_deveBuscarFuncionario_quandoInformarIdEUsuarioForAdmin() {
        when(autenticacaoService.getUsuarioAutenticado()).thenReturn(umUsuarioAutenticadoAdmin());

        var response = service.buscarPorId(11);
        assertThat(response).isNotNull();
        assertThat(response.getId()).isEqualTo(11);
        assertThat(response.getUsuarioId()).isEqualTo(10);
        assertThat(response.getNome()).isEqualTo("Funcionário 1");
        assertThat(response.getEmail()).isEqualTo("funcionario1@gmail.com");
        assertThat(response.getCpf()).isEqualTo("192.393.640-99");
        assertThat(response.getEmpresa()).isEqualTo("Empresa 01 Edicao");
        assertThat(response.getCnpj()).isEqualTo("26.343.835/0001-38");
        assertThat(response.getSituacao()).isEqualTo(ESituacaoUsuario.INATIVO);
    }

    @Test
    @DisplayName("Deve buscar por ID quando informar ID e usuário for proprietário")
    public void buscarPorId_deveBuscarFuncionario_quandoInformarIdEUsuarioForProprietario() {
        var proprietario = umUsuarioAutenticadoProprietario();
        proprietario.setId(2);
        when(autenticacaoService.getUsuarioAutenticado()).thenReturn(proprietario);

        var response = service.buscarPorId(11);
        assertThat(response).isNotNull();
        assertThat(response.getId()).isEqualTo(11);
        assertThat(response.getUsuarioId()).isEqualTo(10);
        assertThat(response.getNome()).isEqualTo("Funcionário 1");
        assertThat(response.getEmail()).isEqualTo("funcionario1@gmail.com");
        assertThat(response.getCpf()).isEqualTo("192.393.640-99");
        assertThat(response.getEmpresa()).isEqualTo("Empresa 01 Edicao");
        assertThat(response.getCnpj()).isEqualTo("26.343.835/0001-38");
        assertThat(response.getSituacao()).isEqualTo(ESituacaoUsuario.INATIVO);
    }

    @Test
    @DisplayName("Deve buscar por ID quando informar ID e usuário for sócio")
    public void buscarPorId_deveBuscarFuncionario_quandoInformarIdEUsuarioForSocio() {
        var socio = umUsuarioAutenticadoSocio();
        socio.setId(6);
        when(autenticacaoService.getUsuarioAutenticado()).thenReturn(socio);

        var response = service.buscarPorId(11);
        assertThat(response).isNotNull();
        assertThat(response.getId()).isEqualTo(11);
        assertThat(response.getUsuarioId()).isEqualTo(10);
        assertThat(response.getNome()).isEqualTo("Funcionário 1");
        assertThat(response.getEmail()).isEqualTo("funcionario1@gmail.com");
        assertThat(response.getCpf()).isEqualTo("192.393.640-99");
        assertThat(response.getEmpresa()).isEqualTo("Empresa 01 Edicao");
        assertThat(response.getCnpj()).isEqualTo("26.343.835/0001-38");
        assertThat(response.getSituacao()).isEqualTo(ESituacaoUsuario.INATIVO);
    }

    @Test
    @DisplayName("Deve buscar por ID quando informar ID e usuário for funcionário")
    public void buscarPorId_deveBuscarFuncionario_quandoInformarIdEUsuarioForFuncionario() {
        var funcionario = umUsuarioAutenticadoFuncionario();
        funcionario.setId(10);
        when(autenticacaoService.getUsuarioAutenticado()).thenReturn(funcionario);

        var response = service.buscarPorId(11);
        assertThat(response).isNotNull();
        assertThat(response.getId()).isEqualTo(11);
        assertThat(response.getUsuarioId()).isEqualTo(10);
        assertThat(response.getNome()).isEqualTo("Funcionário 1");
        assertThat(response.getEmail()).isEqualTo("funcionario1@gmail.com");
        assertThat(response.getCpf()).isEqualTo("192.393.640-99");
        assertThat(response.getEmpresa()).isEqualTo("Empresa 01 Edicao");
        assertThat(response.getCnpj()).isEqualTo("26.343.835/0001-38");
        assertThat(response.getSituacao()).isEqualTo(ESituacaoUsuario.INATIVO);
    }

    @Test
    @DisplayName("Deve lançar exception ao tentar buscar por ID e usuário for proprietário sem permissão")
    public void buscarPorId_deveLancarException_quandoProprietarioNaoTiverPermissao() {
        var proprietario = umUsuarioAutenticadoProprietario();
        proprietario.setId(10);
        when(autenticacaoService.getUsuarioAutenticado()).thenReturn(proprietario);

        assertThatExceptionOfType(PermissaoException.class)
            .isThrownBy(() -> service.buscarPorId(11))
            .withMessage("Usuário sem permissão para visualizar funcionários desta empresa.");
    }

    @Test
    @DisplayName("Deve lançar exception ao tentar buscar por ID e usuário for sócio sem permissão")
    public void buscarPorId_deveLancarException_quandoSocioNaoTiverPermissao() {
        var socio = umUsuarioAutenticadoSocio();
        socio.setId(10);
        when(autenticacaoService.getUsuarioAutenticado()).thenReturn(socio);

        assertThatExceptionOfType(PermissaoException.class)
            .isThrownBy(() -> service.buscarPorId(11))
            .withMessage("Usuário sem permissão para visualizar funcionários desta empresa.");
    }

    @Test
    @DisplayName("Deve lançar exception ao tentar buscar por ID e usuário for funcionário sem permissão")
    public void buscarPorId_deveLancarException_quandoFuncionarioNaoTiverPermissao() {
        var socio = umUsuarioAutenticadoFuncionario();
        socio.setId(1000);
        when(autenticacaoService.getUsuarioAutenticado()).thenReturn(socio);

        assertThatExceptionOfType(PermissaoException.class)
            .isThrownBy(() -> service.buscarPorId(11))
            .withMessage("Usuário sem permissão para visualizar funcionários desta empresa.");
    }
}

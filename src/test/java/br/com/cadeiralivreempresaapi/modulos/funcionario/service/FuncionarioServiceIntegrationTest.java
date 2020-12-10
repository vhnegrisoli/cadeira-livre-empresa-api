package br.com.cadeiralivreempresaapi.modulos.funcionario.service;

import br.com.cadeiralivreempresaapi.config.exception.PermissaoException;
import br.com.cadeiralivreempresaapi.modulos.comum.dto.PageRequest;
import br.com.cadeiralivreempresaapi.modulos.empresa.service.EmpresaService;
import br.com.cadeiralivreempresaapi.modulos.funcionario.dto.FuncionarioFiltros;
import br.com.cadeiralivreempresaapi.modulos.funcionario.repository.FuncionarioRepository;
import br.com.cadeiralivreempresaapi.modulos.jwt.service.JwtService;
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
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.when;

@DataJpaTest
@ActiveProfiles("test")
@Import({
    FuncionarioService.class,
    EmpresaService.class,
    JwtService.class,
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

    @Test
    @DisplayName("Deve buscar todos sem filtros os funcionário quando usuário tiver permissão sendo admin")
    public void buscarTodos_deveBuscarTodosOsFuncionarios_quandoUsuarioForAdmin() {
        when(autenticacaoService.getUsuarioAutenticado()).thenReturn(umUsuarioAutenticadoAdmin());

        assertThat(service.buscarTodos(new PageRequest(), new FuncionarioFiltros()))
            .extracting("id", "nome", "email", "situacao", "empresa", "cnpj")
            .containsExactly(
                tuple(11, "Funcionário 1", "funcionario1@gmail.com",
                    ESituacaoUsuario.INATIVO, "Empresa 01 Edicao", "26.343.835/0001-38"),
                tuple(14, "Funcionário 2", "funcionario2@gmail.com",
                    ESituacaoUsuario.ATIVO, "Empresa 02", "49.579.794/0001-89")
            );
    }

    @Test
    @DisplayName("Deve buscar os funcionários com filtros de cnpj e empresa quando usuário tiver permissão sendo admin")
    public void buscarTodos_deveBuscarTodosOsFuncionariosComFiltrosCnpjEmpresa_quandoUsuarioForAdmin() {
        when(autenticacaoService.getUsuarioAutenticado()).thenReturn(umUsuarioAutenticadoAdmin());

        var filtros = new FuncionarioFiltros();
        filtros.setCpf("192");
        filtros.setCnpj("343");
        assertThat(service.buscarTodos(new PageRequest(), filtros))
            .extracting("id", "nome", "email", "situacao", "empresa", "cnpj")
            .containsExactly(
                tuple(11, "Funcionário 1", "funcionario1@gmail.com",
                    ESituacaoUsuario.INATIVO, "Empresa 01 Edicao", "26.343.835/0001-38")
            );
    }

    @Test
    @DisplayName("Deve buscar os funcionários com filtros de situaçao quando usuário tiver permissão sendo admin")
    public void buscarTodos_deveBuscarTodosOsFuncionariosComFiltrosSituacao_quandoUsuarioForAdmin() {
        when(autenticacaoService.getUsuarioAutenticado()).thenReturn(umUsuarioAutenticadoAdmin());

        var filtros = new FuncionarioFiltros();
        filtros.setSituacao(ESituacaoUsuario.ATIVO);
        assertThat(service.buscarTodos(new PageRequest(), filtros))
            .extracting("id", "nome", "email", "situacao", "empresa", "cnpj")
            .containsExactly(
                tuple(14, "Funcionário 2", "funcionario2@gmail.com",
                    ESituacaoUsuario.ATIVO, "Empresa 02", "49.579.794/0001-89")
            );
    }

    @Test
    @DisplayName("Deve buscar todos sem filtros os funcionário quando usuário tiver permissão sendo proprietário")
    public void buscarTodos_deveBuscarTodosOsFuncionarios_quandoUsuarioForProprietario() {
        var proprietario = umUsuarioAutenticadoProprietario();
        proprietario.setId(2);
        when(autenticacaoService.getUsuarioAutenticado()).thenReturn(proprietario);

        assertThat(service.buscarTodos(new PageRequest(), new FuncionarioFiltros()))
            .extracting("id", "nome", "email", "situacao", "empresa", "cnpj")
            .containsExactly(
                tuple(11, "Funcionário 1", "funcionario1@gmail.com",
                    ESituacaoUsuario.INATIVO, "Empresa 01 Edicao", "26.343.835/0001-38")
            );
    }

    @Test
    @DisplayName("Deve buscar todos sem filtros os funcionário quando usuário tiver permissão sendo sócio")
    public void buscarTodos_deveBuscarTodosOsFuncionarios_quandoUsuarioForSocio() {
        var socio = umUsuarioAutenticadoSocio();
        socio.setId(6);
        when(autenticacaoService.getUsuarioAutenticado()).thenReturn(socio);

        assertThat(service.buscarTodos(new PageRequest(), new FuncionarioFiltros()))
            .extracting("id", "nome", "email", "situacao", "empresa", "cnpj")
            .containsExactly(
                tuple(11, "Funcionário 1", "funcionario1@gmail.com",
                    ESituacaoUsuario.INATIVO, "Empresa 01 Edicao", "26.343.835/0001-38")
            );
    }

    @Test
    @DisplayName("Deve buscar todos sem filtros os funcionário quando usuário tiver permissão sendo funcionário")
    public void buscarTodos_deveBuscarTodosOsFuncionarios_quandoUsuarioForFuncionario() {
        var funcionario = umUsuarioAutenticadoFuncionario();
        funcionario.setId(10);
        when(autenticacaoService.getUsuarioAutenticado()).thenReturn(funcionario);

        assertThat(service.buscarTodos(new PageRequest(), new FuncionarioFiltros()))
            .extracting("id", "nome", "email", "situacao", "empresa", "cnpj")
            .containsExactly(
                tuple(11, "Funcionário 1", "funcionario1@gmail.com",
                    ESituacaoUsuario.INATIVO, "Empresa 01 Edicao", "26.343.835/0001-38")
            );
    }

    @Test
    @DisplayName("Deve não buscar dados quando usuário for proprietário e não possuir funcionário")
    public void buscarTodos_deveNaoBuscarDados_quandoUsuarioProprietarioNaoPuderVisualizar() {
        var proprietario = umUsuarioAutenticadoProprietario();
        proprietario.setId(11);
        when(autenticacaoService.getUsuarioAutenticado()).thenReturn(proprietario);

        assertThat(service.buscarTodos(new PageRequest(), new FuncionarioFiltros()).getTotalElements())
            .isEqualTo(0L);
    }

    @Test
    @DisplayName("Deve não buscar dados quando usuário for sócio e não possuir funcionário")
    public void buscarTodos_deveNaoBuscarDados_quandoUsuarioSocioNaoPuderVisualizar() {
        var socio = umUsuarioAutenticadoSocio();
        socio.setId(12);
        when(autenticacaoService.getUsuarioAutenticado()).thenReturn(socio);

        assertThat(service.buscarTodos(new PageRequest(), new FuncionarioFiltros()).getTotalElements())
            .isEqualTo(0L);
    }

    @Test
    @DisplayName("Deve não buscar dados quando usuário for funcionário e não possuir funcionário")
    public void buscarTodos_deveNaoBuscarDados_quandoUsuarioFuncionarioNaoPuderVisualizar() {
        var funcionario = umUsuarioAutenticadoFuncionario();
        funcionario.setId(14);
        when(autenticacaoService.getUsuarioAutenticado()).thenReturn(funcionario);

        assertThat(service.buscarTodos(new PageRequest(), new FuncionarioFiltros()).getTotalElements())
            .isEqualTo(0L);
    }

    @Test
    @DisplayName("Deve não buscar dados quando usuário for proprietário e não possuir o funcionário no filtro")
    public void buscarTodos_deveNaoBuscarDados_quandoUsuarioProprietarioNaoPossuirFuncionarioNoFiltro() {
        var proprietario = umUsuarioAutenticadoProprietario();
        proprietario.setId(11);
        when(autenticacaoService.getUsuarioAutenticado()).thenReturn(proprietario);

        var filtros = new FuncionarioFiltros();
        filtros.setCpf("289.993.010-95");
        assertThat(service.buscarTodos(new PageRequest(), filtros).getTotalElements())
            .isEqualTo(0L);
    }

    @Test
    @DisplayName("Deve não buscar dados quando usuário for sócio e não possuir o funcionário no filtro")
    public void buscarTodos_deveNaoBuscarDados_quandoUsuarioSocioNaoPossuirFuncionarioNoFiltro() {
        var socio = umUsuarioAutenticadoSocio();
        socio.setId(6);
        when(autenticacaoService.getUsuarioAutenticado()).thenReturn(socio);

        var filtros = new FuncionarioFiltros();
        filtros.setCpf("289.993.010-95");
        assertThat(service.buscarTodos(new PageRequest(), filtros).getTotalElements())
            .isEqualTo(0L);
    }

    @Test
    @DisplayName("Deve não buscar dados quando usuário for funcionário e não possuir o funcionário no filtro")
    public void buscarTodos_deveNaoBuscarDados_quandoUsuarioFuncionarioNaoPossuirFuncionarioNoFiltro() {
        var funcionario = umUsuarioAutenticadoFuncionario();
        funcionario.setId(10);
        when(autenticacaoService.getUsuarioAutenticado()).thenReturn(funcionario);

        var filtros = new FuncionarioFiltros();
        filtros.setCpf("289.993.010-95");
        assertThat(service.buscarTodos(new PageRequest(), filtros).getTotalElements())
            .isEqualTo(0L);
    }
}

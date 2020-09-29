package br.com.cadeiralivreempresaapi.modulos.usuario.service;

import br.com.cadeiralivreempresaapi.config.exception.ValidacaoException;
import br.com.cadeiralivreempresaapi.modulos.empresa.service.EmpresaService;
import br.com.cadeiralivreempresaapi.modulos.funcionario.service.FuncionarioService;
import br.com.cadeiralivreempresaapi.modulos.usuario.enums.ESituacaoUsuario;
import br.com.cadeiralivreempresaapi.modulos.usuario.repository.UsuarioRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;

import static br.com.cadeiralivreempresaapi.modulos.usuario.mocks.UsuarioMocks.umUsuarioAutenticadoAdmin;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.Mockito.*;

@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@Import(UsuarioService.class)
@Sql(scripts = {"classpath:/usuarios_tests.sql"})
@DataJpaTest
public class UsuarioServiceIntegrationTest {

    @Autowired
    private UsuarioService service;
    @Autowired
    private UsuarioRepository usuarioRepository;
    @MockBean
    private PasswordEncoder passwordEncoder;
    @MockBean
    private AutenticacaoService autenticacaoService;
    @MockBean
    private PermissaoService permissaoService;
    @MockBean
    private EmpresaService empresaService;
    @MockBean
    private FuncionarioService funcionarioService;

    @Test
    public void buscarPorId_deveBuscarUsuario_quandoEncontrarPorId() {
        var usuario = service.buscarPorId(1);

        assertThat(usuario).isNotNull();
        assertThat(usuario.getNome()).isEqualTo("Victor Hugo Negrisoli");
        assertThat(usuario.getEmail()).isEqualTo("victorhugonegrisoli.ccs@gmail.com");
    }

    @Test
    public void buscarPorId_deveLancarException_quandoNaoEncontrarPorId() {
        assertThatExceptionOfType(ValidacaoException.class)
            .isThrownBy(() -> service.buscarPorId(1000))
            .withMessage("Usuário não encontrado.");
    }

    @Test
    public void getUsuarioAutenticadoAtualizaUltimaData_deveRetornarUsuarioAutenticadoEAtualizarUltimoAcesso_quandoSolicitado() {
        when(autenticacaoService.getUsuarioAutenticadoId()).thenReturn(1);
        var usuarioUltimoAcessoAnterior = usuarioRepository.findById(1).get().getUltimoAcesso();
        var response = service.getUsuarioAutenticadoAtualizaUltimaData();
        assertThat(response).isNotNull();
        assertThat(response.getUltimoAcesso()).isNotEqualTo(usuarioUltimoAcessoAnterior);
    }

    @Test
    public void getUsuarioAutenticadoAtualizaUltimaData_deveLancarException_quandoNaoEncontrarUsuario() {
        when(autenticacaoService.getUsuarioAutenticadoId()).thenReturn(10000);
        assertThatExceptionOfType(ValidacaoException.class)
            .isThrownBy(() -> service.getUsuarioAutenticadoAtualizaUltimaData())
            .withMessage("Usuário não encontrado.");
    }

    @Test
    public void atualizarTokenNotificacao_deveAtualizarToken_quandoUsuarioNaoPossuirToken() {
        when(autenticacaoService.getUsuarioAutenticadoId()).thenReturn(2);

        var usuarioTokenAntigo = usuarioRepository.findById(2).get();
        assertThat(usuarioTokenAntigo.getTokenNotificacao()).isNull();

        var response = service.atualizarTokenNotificacao("123456789");

        assertThat(response).isNotNull();
        assertThat(response.getStatus()).isEqualTo(200);
        assertThat(response.getMessage()).isEqualTo("Token de notificação atualizado com sucesso!");

        var usuarioTokenNovo = usuarioRepository.findById(2).get();
        assertThat(usuarioTokenNovo.getTokenNotificacao()).isEqualTo("123456789");
    }

    @Test
    public void atualizarTokenNotificacao_deveAtualizarToken_quandoInformarUmNovoQueOUsuarioNaoPossui() {
        when(autenticacaoService.getUsuarioAutenticadoId()).thenReturn(1);

        var usuarioTokenAntigo = usuarioRepository.findById(1).get();
        assertThat(usuarioTokenAntigo.getTokenNotificacao()).isEqualTo("123456");

        var response = service.atualizarTokenNotificacao("123456789");
        assertThat(response).isNotNull();
        assertThat(response.getStatus()).isEqualTo(200);
        assertThat(response.getMessage()).isEqualTo("Token de notificação atualizado com sucesso!");

        var usuarioTokenNovo = usuarioRepository.findById(1).get();
        assertThat(usuarioTokenNovo.getTokenNotificacao()).isEqualTo("123456789");
    }

    @Test
    public void atualizarTokenNotificacao_deveNaoAtualizarToken_quandoInformarUmTokenQueUsuarioJaPossui() {
        when(autenticacaoService.getUsuarioAutenticadoId()).thenReturn(1);

        var usuarioTokenAntigo = usuarioRepository.findById(1).get();
        assertThat(usuarioTokenAntigo.getTokenNotificacao()).isEqualTo("123456");

        var response = service.atualizarTokenNotificacao("123456");
        assertThat(response).isNotNull();
        assertThat(response.getStatus()).isEqualTo(200);
        assertThat(response.getMessage()).isEqualTo("O usuário já possui esse token de notificação.");

        var usuarioTokenNovo = usuarioRepository.findById(1).get();
        assertThat(usuarioTokenNovo.getTokenNotificacao()).isEqualTo("123456");
    }

    @Test
    public void atualizarTokenNotificacao_deveLancarException_quandoNaoEncontrarUsuario() {
        when(autenticacaoService.getUsuarioAutenticadoId()).thenReturn(10000);
        assertThatExceptionOfType(ValidacaoException.class)
            .isThrownBy(() -> service.atualizarTokenNotificacao("123456789"))
            .withMessage("Usuário não encontrado.");
    }

    @Test
    public void alterarSituacao_deveAtivar_quandoUsuarioEstiverInativoEForAdmin() {
        when(autenticacaoService.getUsuarioAutenticado()).thenReturn(umUsuarioAutenticadoAdmin());

        alterarSituacao(1, ESituacaoUsuario.INATIVO);

        var response = service.alterarSituacao(1);
        assertThat(response).isNotNull();
        assertThat(response.getStatus()).isEqualTo(200);
        assertThat(response.getMessage()).isEqualTo("A situação do usuário foi alterada com sucesso!");

        var usuarioAlterado = usuarioRepository.findById(1).get();
        assertThat(usuarioAlterado.isAtivo()).isTrue();

        verify(funcionarioService, times(0)).validarUsuario(anyInt());
    }

    @Test
    public void alterarSituacao_deveAtivar_quandoUsuarioEstiverInativoEForProprietario() {
        when(autenticacaoService.getUsuarioAutenticado()).thenReturn(umUsuarioAutenticadoAdmin());

        alterarSituacao(2, ESituacaoUsuario.INATIVO);

        var response = service.alterarSituacao(2);
        assertThat(response).isNotNull();
        assertThat(response.getStatus()).isEqualTo(200);
        assertThat(response.getMessage()).isEqualTo("A situação do usuário foi alterada com sucesso!");

        var usuarioAlterado = usuarioRepository.findById(2).get();
        assertThat(usuarioAlterado.isAtivo()).isTrue();

        verify(funcionarioService, times(0)).validarUsuario(anyInt());
    }

    @Test
    public void alterarSituacao_deveAtivar_quandoUsuarioEstiverInativoEForSocio() {
        when(autenticacaoService.getUsuarioAutenticado()).thenReturn(umUsuarioAutenticadoAdmin());

        alterarSituacao(6, ESituacaoUsuario.INATIVO);

        var response = service.alterarSituacao(6);
        assertThat(response).isNotNull();
        assertThat(response.getStatus()).isEqualTo(200);
        assertThat(response.getMessage()).isEqualTo("A situação do usuário foi alterada com sucesso!");

        var usuarioAlterado = usuarioRepository.findById(6).get();
        assertThat(usuarioAlterado.isAtivo()).isTrue();

        verify(funcionarioService, times(0)).validarUsuario(anyInt());
    }

    @Test
    public void alterarSituacao_deveAtivar_quandoUsuarioEstiverInativoEForFuncionario() {
        when(autenticacaoService.getUsuarioAutenticado()).thenReturn(umUsuarioAutenticadoAdmin());

        alterarSituacao(10, ESituacaoUsuario.INATIVO);

        var response = service.alterarSituacao(10);
        assertThat(response).isNotNull();
        assertThat(response.getStatus()).isEqualTo(200);
        assertThat(response.getMessage()).isEqualTo("A situação do usuário foi alterada com sucesso!");

        var usuarioAlterado = usuarioRepository.findById(10).get();
        assertThat(usuarioAlterado.isAtivo()).isTrue();

        verify(funcionarioService, times(1)).validarUsuario(anyInt());
    }

    @Test
    public void alterarSituacao_deveInativar_quandoUsuarioEstiverAtivoEForAdmin() {
        when(autenticacaoService.getUsuarioAutenticado()).thenReturn(umUsuarioAutenticadoAdmin());

        alterarSituacao(1, ESituacaoUsuario.ATIVO);

        var response = service.alterarSituacao(1);
        assertThat(response).isNotNull();
        assertThat(response.getStatus()).isEqualTo(200);
        assertThat(response.getMessage()).isEqualTo("A situação do usuário foi alterada com sucesso!");

        var usuarioAlterado = usuarioRepository.findById(1).get();
        assertThat(usuarioAlterado.isAtivo()).isFalse();

        verify(funcionarioService, times(0)).validarUsuario(anyInt());
    }

    @Test
    public void alterarSituacao_deveInativar_quandoUsuarioEstiverAtivoEForProprietario() {
        when(autenticacaoService.getUsuarioAutenticado()).thenReturn(umUsuarioAutenticadoAdmin());

        alterarSituacao(2, ESituacaoUsuario.ATIVO);

        var response = service.alterarSituacao(2);
        assertThat(response).isNotNull();
        assertThat(response.getStatus()).isEqualTo(200);
        assertThat(response.getMessage()).isEqualTo("A situação do usuário foi alterada com sucesso!");

        var usuarioAlterado = usuarioRepository.findById(2).get();
        assertThat(usuarioAlterado.isAtivo()).isFalse();

        verify(funcionarioService, times(0)).validarUsuario(anyInt());
    }

    @Test
    public void alterarSituacao_deveInativar_quandoUsuarioEstiverAtivoEForSocio() {
        when(autenticacaoService.getUsuarioAutenticado()).thenReturn(umUsuarioAutenticadoAdmin());

        alterarSituacao(6, ESituacaoUsuario.ATIVO);

        var response = service.alterarSituacao(6);
        assertThat(response).isNotNull();
        assertThat(response.getStatus()).isEqualTo(200);
        assertThat(response.getMessage()).isEqualTo("A situação do usuário foi alterada com sucesso!");

        var usuarioAlterado = usuarioRepository.findById(6).get();
        assertThat(usuarioAlterado.isAtivo()).isFalse();

        verify(funcionarioService, times(0)).validarUsuario(anyInt());
    }

    @Test
    public void alterarSituacao_deveInativar_quandoUsuarioEstiverAtivoEForFuncionario() {
        when(autenticacaoService.getUsuarioAutenticado()).thenReturn(umUsuarioAutenticadoAdmin());

        alterarSituacao(10, ESituacaoUsuario.ATIVO);

        var response = service.alterarSituacao(10);
        assertThat(response).isNotNull();
        assertThat(response.getStatus()).isEqualTo(200);
        assertThat(response.getMessage()).isEqualTo("A situação do usuário foi alterada com sucesso!");

        var usuarioAlterado = usuarioRepository.findById(10).get();
        assertThat(usuarioAlterado.isAtivo()).isFalse();

        verify(funcionarioService, times(1)).validarUsuario(anyInt());
    }

    private void alterarSituacao(Integer id, ESituacaoUsuario situacao) {
        var usuario = usuarioRepository.findById(id).get();
        usuario.setSituacao(situacao);
        usuarioRepository.save(usuario);
    }
}

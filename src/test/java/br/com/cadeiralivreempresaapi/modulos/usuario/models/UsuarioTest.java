package br.com.cadeiralivreempresaapi.modulos.usuario.models;

import br.com.cadeiralivreempresaapi.modulos.usuario.enums.ESexo;
import br.com.cadeiralivreempresaapi.modulos.usuario.enums.ESituacaoUsuario;
import br.com.cadeiralivreempresaapi.modulos.usuario.model.Usuario;
import org.junit.Test;

import java.time.LocalDate;
import java.time.Period;
import java.util.Set;

import static br.com.cadeiralivreempresaapi.modulos.usuario.mocks.UsuarioMocks.*;
import static org.assertj.core.api.Assertions.assertThat;

public class UsuarioTest {

    @Test
    public void getIdade_deveRetornarIdade_quandoSolicitado() {
        var idade = Period.between(LocalDate.parse("1998-01-01"), LocalDate.now()).getYears();
        assertThat(umUsuario().getIdade()).isEqualTo(idade);
    }

    @Test
    public void getIdade_deveRetornarZero_quandoDataNascimentoForNula() {
        var usuario = umUsuario();
        usuario.setDataNascimento(null);
        assertThat(usuario.getIdade()).isEqualTo(0);
    }

    @Test
    public void isAniversario_deveRetornarTrue_seForAniversarioDoUsuario() {
        var usuario = umUsuario();
        usuario.setDataNascimento(LocalDate.now().minusYears(22));
        assertThat(usuario.isAniversario()).isTrue();
    }

    @Test
    public void isAniversario_deveRetornarFalse_seNaoForAniversarioDoUsuario() {
        var usuario = umUsuario();
        usuario.setDataNascimento(LocalDate.now().minusYears(22).minusDays(1));
        assertThat(usuario.isAniversario()).isFalse();
    }

    @Test
    public void isAtivo_deveRetornarTrue_seForUsuarioAtivo() {
        assertThat(umUsuario().isAtivo()).isTrue();
    }

    @Test
    public void isAtivo_deveRetornarFalse_seNaoForUsuarioAtivo() {
        var usuario = umUsuario();
        usuario.setSituacao(ESituacaoUsuario.INATIVO);
        assertThat(usuario.isAtivo()).isFalse();
    }

    @Test
    public void isSocioOuProprietario_deveRetornarTrue_seForUsuarioProprietario() {
        var usuario = umUsuario();
        usuario.setPermissoes(Set.of(umaPermissaoProprietario()));
        assertThat(usuario.isSocioOuProprietario()).isTrue();
    }

    @Test
    public void isSocioOuProprietario_deveRetornarTrue_seForUsuarioSocio() {
        var usuario = umUsuario();
        usuario.setPermissoes(Set.of(umaPermissaoSocio()));
        assertThat(usuario.isSocioOuProprietario()).isTrue();
    }

    @Test
    public void isSocioOuProprietario_deveRetornarFalse_seNaoForUsuarioSocioOuProprietario() {
        assertThat(umUsuario().isSocioOuProprietario()).isFalse();
    }

    @Test
    public void isFuncionario_deveRetornarTrue_seForUsuarioFuncionario() {
        var usuario = umUsuario();
        usuario.setPermissoes(Set.of(umaPermissaoFuncionario()));
        assertThat(usuario.isFuncionario()).isTrue();
    }

    @Test
    public void isFuncionario_deveRetornarFalse_seNaoForUsuarioFuncionario() {
        assertThat(umUsuario().isFuncionario()).isFalse();
    }

    @Test
    public void isNovoCadastro_deveRetornarTrue_seForUsuarioNovo() {
        var usuario = umUsuario();
        usuario.setId(null);
        assertThat(usuario.isNovoCadastro()).isTrue();
    }

    @Test
    public void isNovoCadastro_deveRetornarFalse_seNaoForUsuarioNovo() {
        assertThat(umUsuario().isNovoCadastro()).isFalse();
    }

    @Test
    public void possuiToken_deveRetornarTrue_seUsuarioPossuirTokenDeNotificacaoIgualInformada() {
        assertThat(umUsuario().possuiToken("123456")).isTrue();
    }

    @Test
    public void possuiToken_deveRetornarFalse_seUsuarioNaoPossuirTokenDeNotificacaoIgualInformada() {
        assertThat(umUsuario().possuiToken("654321")).isFalse();
    }

    @Test
    public void possuiToken_deveRetornarFalse_seUsuarioTokenDeNotificacaoForNulo() {
        var usuario = umUsuario();
        usuario.setTokenNotificacao(null);
        assertThat(usuario.possuiToken("123456")).isFalse();
    }

    @Test
    public void of_deveConverterParaModelDeUsuario_quandoInformarObjetoDoTipoUsuarioRequest() {
        var usuario = Usuario.of(umUsuarioRequest());
        assertThat(usuario).isNotNull();
        assertThat(usuario.getId()).isEqualTo(1);
        assertThat(usuario.getCpf()).isEqualTo("332.368.250-57");
        assertThat(usuario.getEmail()).isEqualTo("usuario@gmail.com");
        assertThat(usuario.getNome()).isEqualTo("Usuario");
        assertThat(usuario.getSenha()).isEqualTo("123456");
        assertThat(usuario.getSexo()).isEqualTo(ESexo.MASCULINO);
        assertThat(usuario.getDataNascimento()).isEqualTo(LocalDate.parse("1998-01-01"));
    }

    @Test
    public void of_deveConverterParaModelDeUsuario_quandoInformarObjetoDoTipoUsuarioUsuarioAutenticadoA() {
        var usuario = Usuario.of(umUsuarioAutenticadoAdmin());
        assertThat(usuario).isNotNull();
        assertThat(usuario.getId()).isEqualTo(1);
    }
}

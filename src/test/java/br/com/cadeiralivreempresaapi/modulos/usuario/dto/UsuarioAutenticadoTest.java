package br.com.cadeiralivreempresaapi.modulos.usuario.dto;

import br.com.cadeiralivreempresaapi.modulos.usuario.enums.ESexo;
import org.junit.Test;

import java.util.List;

import static br.com.cadeiralivreempresaapi.modulos.usuario.mocks.UsuarioMocks.*;
import static org.assertj.core.api.Assertions.assertThat;

public class UsuarioAutenticadoTest {

    @Test
    public void isAdmin_deveRetornarTrue_seUsuarioAutenticadoForAdmin() {
        assertThat(umUsuarioAutenticadoAdmin().isAdmin()).isTrue();
    }

    @Test
    public void isAdmin_deveRetornarFalse_seUsuarioAutenticadoNaoForAdmin() {
        assertThat(umUsuarioAutenticadoFuncionario().isAdmin()).isFalse();
    }

    @Test
    public void isFuncionario_deveRetornarTrue_seUsuarioAutenticadoForFuncionario() {
        assertThat(umUsuarioAutenticadoFuncionario().isFuncionario()).isTrue();
    }

    @Test
    public void isFuncionario_deveRetornarFalse_seUsuarioAutenticadoNaoForFuncionario() {
        assertThat(umUsuarioAutenticadoAdmin().isFuncionario()).isFalse();
    }

    @Test
    public void isSocioOuProprietario_deveRetornarTrue_seUsuarioAutenticadoForProprietario() {
        assertThat(umUsuarioAutenticadoProprietario().isSocioOuProprietario()).isTrue();
    }

    @Test
    public void isSocioOuProprietario_deveRetornarTrue_seUsuarioAutenticadoForSocio() {
        assertThat(umUsuarioAutenticadoSocio().isSocioOuProprietario()).isTrue();
    }

    @Test
    public void isSocioOuProprietario_deveRetornarFalse_seUsuarioAutenticadoNaoForSocioNemProprietario() {
        assertThat(umUsuarioAutenticadoAdmin().isSocioOuProprietario()).isFalse();
    }

    @Test
    public void of_deveConverterParaDtoDeUsuarioAutenticado_quandoInformarModelDeUsuario() {
        var usuario = UsuarioAutenticado.of(umUsuario());
        assertThat(usuario).isNotNull();
        assertThat(usuario.getId()).isEqualTo(1);
        assertThat(usuario.getCpf()).isEqualTo("332.368.250-57");
        assertThat(usuario.getEmail()).isEqualTo("usuario@gmail.com");
        assertThat(usuario.getNome()).isEqualTo("Usuario");
        assertThat(usuario.getSexo()).isEqualTo(ESexo.MASCULINO);
        assertThat(usuario.getPermissoes()).isEqualTo(List.of("ADMIN"));
    }

    @Test
    public void of_deveConverterParaDtoDeUsuarioAutenticado_quandoInformarObjetoUserDetails() {
        var usuario = UsuarioAutenticado.of(umUserDetails());
        assertThat(usuario).isNotNull();
        assertThat(usuario.getId()).isEqualTo(1);
        assertThat(usuario.getCpf()).isEqualTo("332.368.250-57");
        assertThat(usuario.getEmail()).isEqualTo("usuario@gmail.com");
        assertThat(usuario.getNome()).isEqualTo("Usuario");
        assertThat(usuario.getSexo()).isEqualTo(ESexo.MASCULINO);
        assertThat(usuario.getPermissoes()).isEqualTo(List.of("ADMIN"));
    }
}

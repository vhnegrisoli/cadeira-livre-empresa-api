package br.com.cadeiralivreempresaapi.modulos.agenda.model;

import br.com.cadeiralivreempresaapi.modulos.agenda.enums.ESituacaoAgenda;
import br.com.cadeiralivreempresaapi.modulos.agenda.enums.ETipoAgenda;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Set;

import static br.com.cadeiralivreempresaapi.modulos.agenda.mocks.AgendaMocks.*;
import static br.com.cadeiralivreempresaapi.modulos.agenda.mocks.HorarioMocks.umHorario;
import static br.com.cadeiralivreempresaapi.modulos.agenda.mocks.ServicoMocks.umServico;
import static br.com.cadeiralivreempresaapi.modulos.usuario.mocks.UsuarioMocks.umUsuarioAutenticadoAdmin;
import static org.assertj.core.api.Assertions.assertThat;

public class AgendaTest {

    @Test
    @DisplayName("Deve converter para Model de Agenda quando informar DTO de AgendaRequest")
    public void of_deveConverterParaModelDeAgenda_quandoInformarDtoDeAgendaRequest() {
        var agenda = Agenda.of(umaAgendaRequest());
        assertThat(agenda).isNotNull();
        assertThat(agenda.getEmpresa().getId()).isEqualTo(1);
        assertThat(agenda.getHorario().getId()).isEqualTo(1);
        assertThat(agenda.getServicos().iterator().next().getId()).isEqualTo(1);
        assertThat(agenda.getSituacao()).isEqualTo(ESituacaoAgenda.RESERVA);
        assertThat(agenda.getTipoAgenda()).isEqualTo(ETipoAgenda.HORARIO_MARCADO);
        assertThat(agenda.getTotalDesconto()).isNull();
    }

    @Test
    @DisplayName("Deve converter para Model de Agenda quando informar DTO de CadeiraLivreRequest")
    public void of_deveConverterParaModelDeAgenda_quandoInformarDtoDeCadeiraLivreRequest() {
        var agenda = Agenda.of(umaCadeiraLivreRequest(), umUsuarioAutenticadoAdmin(), umHorario(), Set.of(umServico()));
        assertThat(agenda).isNotNull();
        assertThat(agenda.getEmpresa().getId()).isEqualTo(1);
        assertThat(agenda.getHorario().getId()).isEqualTo(1);
        assertThat(agenda.getServicos().iterator().next().getId()).isEqualTo(1);
        assertThat(agenda.getSituacao()).isEqualTo(ESituacaoAgenda.DISPONIVEL);
        assertThat(agenda.getTipoAgenda()).isEqualTo(ETipoAgenda.CADEIRA_LIVRE);
        assertThat(agenda.getClienteId()).isNull();
        assertThat(agenda.getClienteNome()).isNull();
        assertThat(agenda.getClienteEmail()).isNull();
        assertThat(agenda.getClienteCpf()).isNull();
        assertThat(agenda.getTotalDesconto()).isEqualTo(25.0);
    }

    @Test
    @DisplayName("Deve calcular o total da agenda sem desconto quando não informar um desconto")
    public void calcularTotal_deveCalcularTotalSemDesconto_quandoNaoInfomrarDesconto() {
        var agenda = umaAgendaHorarioMarcado();
        assertThat(agenda.getTotalDesconto()).isNull();
        var servicoOutroPreco = umServico();
        servicoOutroPreco.setPreco(23.45);
        agenda.setServicos(Set.of(umServico(), servicoOutroPreco));
        agenda.calcularTotal(null);
        assertThat(agenda.getTotalDesconto()).isNotNull();
        assertThat(agenda.getTotalDesconto()).isEqualTo(48.45);
    }

    @Test
    @DisplayName("Deve calcular o total da agenda com desconto quando informar um desconto")
    public void calcularTotal_deveCalcularTotalComDesconto_quandoInfomrarDesconto() {
        var agenda = umaAgendaHorarioMarcado();
        assertThat(agenda.getTotalDesconto()).isNull();
        var servicoOutroPreco = umServico();
        servicoOutroPreco.setPreco(23.45);
        agenda.setServicos(Set.of(umServico(), servicoOutroPreco));
        agenda.calcularTotal(57.45f);
        assertThat(agenda.getTotalDesconto()).isNotNull();
        assertThat(agenda.getTotalDesconto()).isEqualTo(27.83452617824078);
    }

    @Test
    @DisplayName("Deve retornar True quando a agenda estiver na situação disponível")
    public void isDisponivel_deveRetornarTrue_quandoAgendaEstiverComSituacaoDisponivel() {
        var agenda = umaAgendaCadeiraLivre();
        agenda.setSituacao(ESituacaoAgenda.DISPONIVEL);

        assertThat(agenda.isDisponivel()).isTrue();
        assertThat(agenda.isCancelada()).isFalse();
        assertThat(agenda.isReservada()).isFalse();
    }

    @Test
    @DisplayName("Deve retornar True quando a agenda estiver na situação cancelada")
    public void isCancelada_deveRetornarTrue_quandoAgendaEstiverComSituacaoCancelada() {
        var agenda = umaAgendaCadeiraLivre();
        agenda.setSituacao(ESituacaoAgenda.CANCELADA);

        assertThat(agenda.isCancelada()).isTrue();
        assertThat(agenda.isDisponivel()).isFalse();
        assertThat(agenda.isReservada()).isFalse();
    }

    @Test
    @DisplayName("Deve retornar True quando a agenda estiver na situação reserva")
    public void isReservada_deveRetornarTrue_quandoAgendaEstiverComSituacaoReserva() {
        var agenda = umaAgendaCadeiraLivre();
        agenda.setSituacao(ESituacaoAgenda.RESERVA);

        assertThat(agenda.isReservada()).isTrue();
        assertThat(agenda.isCancelada()).isFalse();
        assertThat(agenda.isDisponivel()).isFalse();
    }

    @Test
    @DisplayName("Deve retornar o horário da expiração da agenda")
    public void informarHorarioExpiracao_deveRetornarHorario_quandoSolicitado() {
        var agenda = umaAgendaCadeiraLivre();
        agenda.setSituacao(ESituacaoAgenda.DISPONIVEL);
        agenda.setDataCadastro(LocalDateTime.parse("2020-01-01T00:00:00"));
        assertThat(agenda.informarHorarioExpiracao()).isEqualTo(LocalTime.parse("00:30:00"));
    }

    @Test
    @DisplayName("Deve retornar o tempo restante da expiração da agenda")
    public void informarTempoRestante_deveRetornarTempoRestante_quandoSolicitado() {
        var agenda = umaAgendaCadeiraLivre();
        agenda.setSituacao(ESituacaoAgenda.DISPONIVEL);
        agenda.setDataCadastro(LocalDateTime.now().minusMinutes(5));
        assertThat(agenda.informarTempoRestante()).isGreaterThan(20L);
    }
    
    @Test
    @DisplayName("Deve retornar True quando estiver dentro do horário de expiração e com situação disponível")
    public void isValida_deveRetornarTrue_quandoEstiverDentroDoHorarioDeExpiracaoEDisponivel() {
        var agenda = umaAgendaCadeiraLivre();
        assertThat(agenda.isValida()).isTrue();
    }

    @Test
    @DisplayName("Deve retornar False quando não estiver dentro do horário de expiração")
    public void isValida_deveRetornarFalse_quandoNaoEstiverDentroDoHorarioDeExpiracao() {
        var agenda = umaAgendaCadeiraLivre();
        agenda.setDataCadastro(LocalDateTime.now().minusMinutes(31));
        assertThat(agenda.isValida()).isFalse();
    }

    @Test
    @DisplayName("Deve retornar False quando não estiver com a situação disponível")
    public void isValida_deveRetornarFalse_quandoNaoEstiverComSituacaoDisponivel() {
        var agenda = umaAgendaCadeiraLivre();
        agenda.definirSituacaoComoCancelada();
        assertThat(agenda.isValida()).isFalse();
    }

    @Test
    @DisplayName("Deve retornar False quando possuir cliente vinculado")
    public void isValida_deveRetornarFalse_quandoPossuirClienteVinculado() {
        var agenda = umaAgendaCadeiraLivre();
        agenda.setClienteId("asdasd515s1a51d1a5");
        agenda.setClienteNome("testedadosfaltando@gmail.com");
        agenda.setClienteEmail("testedadoscliente@gmail.com");
        agenda.setClienteCpf("10332458954");
        assertThat(agenda.isValida()).isFalse();
    }

    @Test
    @DisplayName("Deve retornar True quando não possuir qualquer dado de cliente vinculado")
    public void isCadeiraLivreSemClienteVinculado_deveRetornarTrue_quandoNaoPossuirCliente() {
        var agenda = umaAgendaCadeiraLivre();
        agenda.definirSituacaoComoCancelada();
        agenda.setClienteId("ASDasdasdsa");
        agenda.setClienteEmail("testedadosfaltando@gmail.com");
        assertThat(agenda.isCadeiraLivreSemClienteVinculado()).isTrue();
    }

    @Test
    @DisplayName("Deve retornar True quando possuir todos os dados do cliente vinculado")
    public void isCadeiraLivreSemClienteVinculado_deveRetornarFalse_quandoPossuirTodosOsDadosDoCliente() {
        var agenda = umaAgendaCadeiraLivre();
        agenda.definirSituacaoComoCancelada();
        agenda.setClienteId("asdasd515s1a51d1a5");
        agenda.setClienteNome("testedadosfaltando@gmail.com");
        agenda.setClienteEmail("testedadoscliente@gmail.com");
        agenda.setClienteCpf("10332458954");
        assertThat(agenda.isCadeiraLivreSemClienteVinculado()).isFalse();
    }
}
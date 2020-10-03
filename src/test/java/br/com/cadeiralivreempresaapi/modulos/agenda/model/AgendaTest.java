package br.com.cadeiralivreempresaapi.modulos.agenda.model;

import br.com.cadeiralivreempresaapi.modulos.agenda.enums.ESituacaoAgenda;
import br.com.cadeiralivreempresaapi.modulos.agenda.enums.ETipoAgenda;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static br.com.cadeiralivreempresaapi.modulos.agenda.mocks.AgendaMocks.*;
import static br.com.cadeiralivreempresaapi.modulos.agenda.mocks.ServicoMocks.umServico;
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
        assertThat(agenda.getClienteId()).isEqualTo(1);
        assertThat(agenda.getClienteNome()).isEqualTo("Cliente");
        assertThat(agenda.getClienteEmail()).isEqualTo("cliente@gmail.com");
        assertThat(agenda.getClienteCpf()).isEqualTo("460.427.120-80");
        assertThat(agenda.getTotal()).isNull();
    }

    @Test
    @DisplayName("Deve converter para Model de Agenda quando informar DTO de CadeiraLivreRequest")
    public void of_deveConverterParaModelDeAgenda_quandoInformarDtoDeCadeiraLivreRequest() {
        var agenda = Agenda.of(umaCadeiraLivreRequest());
        assertThat(agenda).isNotNull();
        assertThat(agenda.getEmpresa().getId()).isEqualTo(1);
        assertThat(agenda.getHorario().getId()).isEqualTo(1);
        assertThat(agenda.getServicos().iterator().next().getId()).isEqualTo(1);
        assertThat(agenda.getSituacao()).isEqualTo(ESituacaoAgenda.DISPNIVEL);
        assertThat(agenda.getTipoAgenda()).isEqualTo(ETipoAgenda.CADEIRA_LIVRE);
        assertThat(agenda.getClienteId()).isNull();
        assertThat(agenda.getClienteNome()).isNull();
        assertThat(agenda.getClienteEmail()).isNull();
        assertThat(agenda.getClienteCpf()).isNull();
        assertThat(agenda.getTotal()).isNull();
    }

    @Test
    @DisplayName("Deve calcular o total da agenda sem desconto quando não informar um desconto")
    public void calcularTotal_deveCalcularTotalSemDesconto_quandoNaoInfomrarDesconto() {
        var agenda = umaAgendaHorarioMarcado();
        assertThat(agenda.getTotal()).isNull();
        var servicoOutroPreco = umServico();
        servicoOutroPreco.setPreco(23.45);
        agenda.setServicos(Set.of(umServico(), servicoOutroPreco));
        agenda.calcularTotal(null);
        assertThat(agenda.getTotal()).isNotNull();
        assertThat(agenda.getTotal()).isEqualTo(48.45);
    }

    @Test
    @DisplayName("Deve calcular o total da agenda com desconto quando informar um desconto")
    public void calcularTotal_deveCalcularTotalComDesconto_quandoInfomrarDesconto() {
        var agenda = umaAgendaHorarioMarcado();
        assertThat(agenda.getTotal()).isNull();
        var servicoOutroPreco = umServico();
        servicoOutroPreco.setPreco(23.45);
        agenda.setServicos(Set.of(umServico(), servicoOutroPreco));
        agenda.calcularTotal(57.45f);
        assertThat(agenda.getTotal()).isNotNull();
        assertThat(agenda.getTotal()).isEqualTo(27.83452617824078);
    }
}
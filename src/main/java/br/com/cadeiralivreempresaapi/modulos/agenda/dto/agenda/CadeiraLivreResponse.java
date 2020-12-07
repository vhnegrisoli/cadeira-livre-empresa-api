package br.com.cadeiralivreempresaapi.modulos.agenda.dto.agenda;

import br.com.cadeiralivreempresaapi.modulos.agenda.model.Agenda;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static br.com.cadeiralivreempresaapi.modulos.comum.util.Constantes.TRINTA_MINUTOS;
import static br.com.cadeiralivreempresaapi.modulos.comum.util.NumeroUtil.converterParaDuasCasasDecimais;
import static java.time.temporal.ChronoUnit.MINUTES;
import static org.springframework.util.ObjectUtils.isEmpty;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CadeiraLivreResponse {

    private Integer id;
    private Integer horarioId;
    @JsonFormat(pattern = "HH:mm")
    private LocalTime horario;
    private List<ServicoAgendaResponse> servicos;
    private Integer empresaId;
    private String empresaNome;
    private String empresaCnpj;
    private BigDecimal desconto;
    private BigDecimal totalDesconto;
    private BigDecimal totalPagamento;
    private LocalTime horarioExpiracao;
    private Long minutosRestantes;
    private Boolean cadeiraLivreValida;
    private String situacao;

    public static CadeiraLivreResponse of(Agenda agenda) {
        var tempoAgenda = agenda.getDataCadastro().toLocalTime();
        var tempoAgendaTrintaMinutos = tempoAgenda.plusMinutes(TRINTA_MINUTOS);
        var tempoAtual = LocalTime.now();
        return definirDadosCadeiraLivre(agenda, tempoAgendaTrintaMinutos, tempoAtual);
    }

    private static CadeiraLivreResponse definirDadosCadeiraLivre(Agenda agenda,
                                                                 LocalTime tempoAgendaTrintaMinutos,
                                                                 LocalTime tempoAtual) {
        var cadeiraLivreValida = tempoAgendaTrintaMinutos.isAfter(tempoAtual) && agenda.isDisponivel();
        return CadeiraLivreResponse
            .builder()
            .id(agenda.getId())
            .empresaId(agenda.getEmpresa().getId())
            .empresaNome(agenda.getEmpresa().getNome())
            .empresaCnpj(agenda.getEmpresa().getCnpj())
            .horarioId(agenda.getHorario().getId())
            .horario(agenda.getHorarioAgendamento())
            .desconto(converterParaDuasCasasDecimais(agenda.getDesconto().doubleValue()))
            .totalDesconto(converterParaDuasCasasDecimais(agenda.getTotalDesconto()))
            .totalPagamento(converterParaDuasCasasDecimais(agenda.getTotalPagamento()))
            .horarioExpiracao(cadeiraLivreValida ? tempoAgendaTrintaMinutos : null)
            .minutosRestantes(cadeiraLivreValida ? tempoAtual.until(tempoAgendaTrintaMinutos, MINUTES) : null)
            .cadeiraLivreValida(cadeiraLivreValida)
            .servicos(tratarServicosDaAgenda(agenda))
            .situacao(agenda.getSituacao().getDescricaoSituacao())
            .build();
    }

    private static List<ServicoAgendaResponse> tratarServicosDaAgenda(Agenda agenda) {
        if (isEmpty(agenda.getServicos())) {
            return Collections.emptyList();
        }
        return agenda
            .getServicos()
            .stream()
            .map(ServicoAgendaResponse::of)
            .collect(Collectors.toList());
    }
}

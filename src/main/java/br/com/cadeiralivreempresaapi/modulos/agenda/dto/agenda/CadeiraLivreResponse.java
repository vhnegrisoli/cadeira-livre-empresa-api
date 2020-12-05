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

import static br.com.cadeiralivreempresaapi.modulos.comum.util.NumeroUtil.converterParaDuasCasasDecimais;
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
    private BigDecimal total;

    public static CadeiraLivreResponse of(Agenda agenda) {
        return CadeiraLivreResponse
            .builder()
            .id(agenda.getId())
            .empresaId(agenda.getEmpresa().getId())
            .empresaNome(agenda.getEmpresa().getNome())
            .empresaCnpj(agenda.getEmpresa().getCnpj())
            .horarioId(agenda.getHorario().getId())
            .horario(agenda.getHorarioAgendamento())
            .total(converterParaDuasCasasDecimais(agenda.getTotal()))
            .servicos(tratarServicosDaAgenda(agenda))
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

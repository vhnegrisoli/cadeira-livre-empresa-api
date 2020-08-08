package br.com.cadeiralivreempresaapi.modulos.agenda.dto;

import br.com.cadeiralivreempresaapi.modulos.agenda.model.Horario;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.BeanUtils;

import java.time.LocalTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HorarioResponse {

    private Integer id;
    @JsonFormat(pattern = "HH:mm")
    private LocalTime horario;
    private Integer diaDaSemanaId;
    private Integer diaNumerico;
    private String diaNome;

    public static HorarioResponse of(Horario horario) {
        var response = new HorarioResponse();
        BeanUtils.copyProperties(horario, response);
        response.setDiaDaSemanaId(horario.getDiaDaSemana().getId());
        response.setDiaNumerico(horario.getDiaDaSemana().getDia());
        response.setDiaNome(horario.getDiaDaSemana().getDiaNome());
        return response;
    }
}

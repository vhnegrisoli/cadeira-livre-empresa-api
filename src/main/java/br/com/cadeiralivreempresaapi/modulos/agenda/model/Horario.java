package br.com.cadeiralivreempresaapi.modulos.agenda.model;

import br.com.cadeiralivreempresaapi.modulos.agenda.dto.HorarioRequest;
import br.com.cadeiralivreempresaapi.modulos.empresa.model.Empresa;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalTime;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "HORARIO")
public class Horario {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "FK_EMPRESA", nullable = false)
    private Empresa empresa;

    @Column(name = "HORARIO", nullable = false)
    private LocalTime horario;

    public static Horario of(HorarioRequest request) {
        return Horario
            .builder()
            .empresa(new Empresa(request.getEmpresaId()))
            .horario(request.getHorario())
            .build();
    }
}
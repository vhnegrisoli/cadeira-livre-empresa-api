package br.com.cadeiralivreempresaapi.modulos.agenda.repository;

import br.com.cadeiralivreempresaapi.modulos.agenda.model.Horario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

public interface HorarioRepository extends JpaRepository<Horario, Integer> {

    Optional<Horario> findByEmpresaIdAndHorario(Integer empresaId, LocalTime horario);

    Boolean existsByEmpresaIdAndHorario(Integer empresaId, LocalTime horario);

    List<Horario> findByEmpresaId(Integer empresaId);
}

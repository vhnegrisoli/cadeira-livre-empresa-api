package br.com.cadeiralivreempresaapi.modulos.agenda.controller;

import br.com.cadeiralivreempresaapi.modulos.agenda.dto.DiaDaSemanaResponse;
import br.com.cadeiralivreempresaapi.modulos.agenda.dto.HorarioRequest;
import br.com.cadeiralivreempresaapi.modulos.agenda.dto.HorarioResponse;
import br.com.cadeiralivreempresaapi.modulos.agenda.service.HorarioService;
import br.com.cadeiralivreempresaapi.modulos.comum.response.SuccessResponseDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/horarios")
public class HorarioController {

    @Autowired
    private HorarioService horarioService;

    @GetMapping("empresa/{empresaId}")
    public List<HorarioResponse> buscarHorariosPorEmpresa(@PathVariable Integer empresaId) {
        return horarioService.buscarHorariosPorEmpresa(empresaId);
    }

    @GetMapping("dias-da-semana")
    public List<DiaDaSemanaResponse> buscarDiasDasemana() {
        return horarioService.buscarDiasDaSemana();
    }

    @PostMapping
    public HorarioResponse salvarHorario(@RequestBody HorarioRequest request) {
        return horarioService.salvarHorario(request);
    }

    @PutMapping("{id}")
    public HorarioResponse alterarHorario(@RequestBody HorarioRequest request, @PathVariable Integer id) {
        return horarioService.alterarHorario(request, id);
    }

    @DeleteMapping("{id}")
    public SuccessResponseDetails removerHorario(@PathVariable Integer id) {
        return horarioService.removerHorarioDoDia(id);
    }
}

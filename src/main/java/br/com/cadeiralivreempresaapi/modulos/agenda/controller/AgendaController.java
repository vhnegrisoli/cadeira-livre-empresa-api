package br.com.cadeiralivreempresaapi.modulos.agenda.controller;

import br.com.cadeiralivreempresaapi.modulos.agenda.dto.agenda.CadeiraLivreRequest;
import br.com.cadeiralivreempresaapi.modulos.agenda.dto.agenda.CadeiraLivreResponse;
import br.com.cadeiralivreempresaapi.modulos.agenda.model.Agenda;
import br.com.cadeiralivreempresaapi.modulos.agenda.service.AgendaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/agendas")
public class AgendaController {

    @Autowired
    private AgendaService service;

    @GetMapping("{id}")
    public Agenda buscarAgendaPorId(@PathVariable Integer id) {
        return service.buscarAgendaPorId(id);
    }

    @GetMapping("cadeira-livre")
    public List<CadeiraLivreResponse> buscarCadeirasLivres() {
        return service.buscarCadeirasLivres();
    }

    @GetMapping("cadeira-livre/{id}")
    public CadeiraLivreResponse buscarCadeirasLivres(@PathVariable Integer id) {
        return service.buscarCadeiraLivrePorId(id);
    }

    @PostMapping("cadeira-livre/disponibilizar")
    public CadeiraLivreResponse disponibilizarCadeiraLivre(@RequestBody CadeiraLivreRequest request) {
        return service.disponibilizarCadeiraLivre(request);
    }
}
package br.com.cadeiralivreempresaapi.modulos.agenda.controller;

import br.com.cadeiralivreempresaapi.modulos.agenda.dto.CadeiraLivreRequest;
import br.com.cadeiralivreempresaapi.modulos.agenda.model.Agenda;
import br.com.cadeiralivreempresaapi.modulos.agenda.service.AgendaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/agendas")
public class AgendaController {

    @Autowired
    private AgendaService service;

    @GetMapping("{id}")
    public Agenda buscarAgendaPorId(@PathVariable Integer id) {
        return service.buscarAgendaPorId(id);
    }

    @PostMapping("cadeira-livre/disponibilizar")
    public void disponibilizarCadeiraLivre(@RequestBody CadeiraLivreRequest request) {
        service.disponibilizarCadeiraLivre(request);
    }
}

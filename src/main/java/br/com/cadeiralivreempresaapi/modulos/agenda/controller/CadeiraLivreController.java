package br.com.cadeiralivreempresaapi.modulos.agenda.controller;

import br.com.cadeiralivreempresaapi.modulos.agenda.dto.agenda.CadeiraLivreRequest;
import br.com.cadeiralivreempresaapi.modulos.agenda.dto.agenda.CadeiraLivreResponse;
import br.com.cadeiralivreempresaapi.modulos.agenda.service.CadeiraLivreService;
import br.com.cadeiralivreempresaapi.modulos.comum.response.SuccessResponseDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cadeiras-livres")
public class CadeiraLivreController {

    @Autowired
    private CadeiraLivreService service;

    @GetMapping("clientes")
    public List<CadeiraLivreResponse> buscarCadeirasLivres() {
        return service.buscarCadeirasLivres();
    }

    @GetMapping("{id}/empresa/{empresaId}")
    public CadeiraLivreResponse buscarCadeiraLivrePorIdEPorEmpresaId(@PathVariable Integer id,
                                                                     @PathVariable Integer empresaId) {
        return service.buscarCadeiraLivreResponsePorIdEPorEmpresaId(id, empresaId);
    }

    @GetMapping("empresa/{empresaId}")
    public List<CadeiraLivreResponse> buscarCadeirasLivresPorEmpresa(@PathVariable Integer empresaId) {
        return service.buscarCadeirasLivresPorEmpresa(empresaId);
    }

    @PostMapping("disponibilizar")
    public CadeiraLivreResponse disponibilizarCadeiraLivre(@RequestBody CadeiraLivreRequest request) {
        return service.disponibilizarCadeiraLivre(request);
    }

    @PutMapping("{id}/empresa/{empresaId}/indisponibilizar")
    public SuccessResponseDetails indisponibilizarCadeiraLivre(@PathVariable Integer id,
                                                               @PathVariable Integer empresaId) {
        return service.indisponibilizarCadeiraLivre(id, empresaId);
    }

    @PostMapping("tempo-expirado/indisponibilizar")
    public void indisponibilizarCadeirasLivresExpiradas() {
        service.indisponibilizarCadeirasLivresExpiradas(true);
    }
}
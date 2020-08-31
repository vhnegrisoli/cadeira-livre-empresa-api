package br.com.cadeiralivreempresaapi.modulos.agenda.controller;

import br.com.cadeiralivreempresaapi.modulos.agenda.dto.ServicoRequest;
import br.com.cadeiralivreempresaapi.modulos.agenda.service.ServicoService;
import br.com.cadeiralivreempresaapi.modulos.comum.response.SuccessResponseDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/servico")
public class ServicoController {

    @Autowired
    private ServicoService servicoService;

    @PostMapping
    public SuccessResponseDetails salvarNovoServico(@RequestBody ServicoRequest request) {
        return servicoService.salvarNovoServico(request);
    }

    @PutMapping("{id}")
    public SuccessResponseDetails atualizarServico(@RequestBody ServicoRequest request,
                                                   @PathVariable Integer id) {
        return servicoService.atualizarServico(request, id);
    }
}

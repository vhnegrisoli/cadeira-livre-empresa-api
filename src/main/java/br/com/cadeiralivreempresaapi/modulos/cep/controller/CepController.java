package br.com.cadeiralivreempresaapi.modulos.cep.controller;

import br.com.cadeiralivreempresaapi.modulos.cep.dto.CepResponse;
import br.com.cadeiralivreempresaapi.modulos.cep.service.ViaCepService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/cep")
public class CepController {

    @Autowired
    private ViaCepService viaCepService;

    @GetMapping("{cep}")
    public CepResponse consultarDadosPorCep(@PathVariable String cep) {
        return viaCepService.consultarDadosPorCep(cep);
    }
}
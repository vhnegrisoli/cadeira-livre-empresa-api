package br.com.cadeiralivreempresaapi.modulos.empresa.controller;

import br.com.cadeiralivreempresaapi.modulos.comum.dto.PageRequest;
import br.com.cadeiralivreempresaapi.modulos.comum.response.SuccessResponseDetails;
import br.com.cadeiralivreempresaapi.modulos.empresa.dto.EmpresaFiltros;
import br.com.cadeiralivreempresaapi.modulos.empresa.dto.EmpresaPageResponse;
import br.com.cadeiralivreempresaapi.modulos.empresa.dto.EmpresaRequest;
import br.com.cadeiralivreempresaapi.modulos.empresa.dto.EmpresaResponse;
import br.com.cadeiralivreempresaapi.modulos.empresa.service.EmpresaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/empresas")
public class EmpresaController {

    @Autowired
    private EmpresaService empresaService;

    @PostMapping
    public SuccessResponseDetails salvar(@RequestBody EmpresaRequest request) {
        return empresaService.salvar(request);
    }

    @PutMapping("{id}")
    public SuccessResponseDetails editar(@RequestBody EmpresaRequest request, @PathVariable Integer id) {
        return empresaService.editar(request, id);
    }

    @GetMapping("{id}")
    public EmpresaResponse buscarPorId(@PathVariable Integer id) {
        return empresaService.buscarPorIdComSocios(id);
    }

    @GetMapping
    public Page<EmpresaPageResponse> buscarTodas(PageRequest pageable, EmpresaFiltros filtros) {
        return empresaService.buscarTodas(pageable, filtros);
    }
}

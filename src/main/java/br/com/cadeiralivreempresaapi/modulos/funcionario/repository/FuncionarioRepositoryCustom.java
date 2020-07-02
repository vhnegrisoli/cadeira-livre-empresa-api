package br.com.cadeiralivreempresaapi.modulos.funcionario.repository;

import br.com.cadeiralivreempresaapi.modulos.empresa.dto.SocioResponse;

import java.util.List;

public interface FuncionarioRepositoryCustom {

    List<SocioResponse> buscarSociosDaEmpresa(Integer empresaId);
}

package br.com.cadeiralivreempresaapi.modulos.agenda.repository;

import br.com.cadeiralivreempresaapi.modulos.agenda.model.Servico;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ServicoRepository extends JpaRepository<Servico, Integer> {

    Boolean existsByDescricaoAndEmpresaId(String descricao, Integer empresaId);

    Boolean existsByDescricaoAndEmpresaIdAndIdNot(String descricao, Integer empresaId, Integer id);

    List<Servico> findByEmpresaIdOrderByDescricao(Integer empresaId);
}

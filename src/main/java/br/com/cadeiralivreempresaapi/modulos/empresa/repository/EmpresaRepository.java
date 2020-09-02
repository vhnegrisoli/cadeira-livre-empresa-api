package br.com.cadeiralivreempresaapi.modulos.empresa.repository;

import br.com.cadeiralivreempresaapi.modulos.empresa.model.Empresa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface EmpresaRepository extends JpaRepository<Empresa, Integer>,
    QuerydslPredicateExecutor<Empresa> {

    Boolean existsByIdAndSociosId(Integer id, Integer usuarioId);

    Boolean existsByRazaoSocial(String razaoSocial);

    Boolean existsByRazaoSocialAndIdNot(String razaoSocial, Integer id);

    Boolean existsByCnpj(String cnpj);

    Boolean existsByCnpjAndIdNot(String cnpj, Integer id);
}


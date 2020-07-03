package br.com.cadeiralivreempresaapi.modulos.empresa.repository;

import br.com.cadeiralivreempresaapi.modulos.empresa.model.Empresa;
import br.com.cadeiralivreempresaapi.modulos.usuario.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface EmpresaRepository extends JpaRepository<Empresa, Integer>,
    QuerydslPredicateExecutor<Empresa> {

    Boolean existsByIdAndSocios(Integer id, Usuario usuario);

    Boolean existsByRazaoSocial(String razaoSocial);

    Boolean existsByRazaoSocialAndIdNot(String razaoSocial, Integer id);

    Boolean existsByCnpj(String cnpj);

    Boolean existsByCnpjAndIdNot(String cnpj, Integer id);
}


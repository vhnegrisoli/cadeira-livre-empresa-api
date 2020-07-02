package br.com.cadeiralivreempresaapi.modulos.funcionario.repository;

import br.com.cadeiralivreempresaapi.modulos.funcionario.model.Funcionario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface FuncionarioRepository extends JpaRepository<Funcionario, Integer>,
    FuncionarioRepositoryCustom, QuerydslPredicateExecutor<Funcionario> {
}
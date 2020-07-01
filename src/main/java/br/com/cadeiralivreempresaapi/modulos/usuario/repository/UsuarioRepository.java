package br.com.cadeiralivreempresaapi.modulos.usuario.repository;

import br.com.cadeiralivreempresaapi.modulos.usuario.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Integer>,
    QuerydslPredicateExecutor<Usuario>,
    UsuarioRepositoryCustom {

    Optional<Usuario> findByEmail(String email);

    Optional<Usuario> findByCpf(String cpf);
}

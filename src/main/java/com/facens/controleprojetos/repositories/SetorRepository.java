package com.facens.controleprojetos.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.facens.controleprojetos.models.Setor;

public interface SetorRepository extends JpaRepository<Setor, Integer> {

    @Query("select distinct s from Setor s left join fetch s.funcionarios order by s.id")
    List<Setor> findAllFetchFuncionarios();

    @Query("select s from Setor s left join fetch s.funcionarios where s.id = :id")
    Optional<Setor> findByIdFetchFuncionarios(@Param("id") Integer id);

    boolean existsByNomeIgnoreCase(String nome);
}

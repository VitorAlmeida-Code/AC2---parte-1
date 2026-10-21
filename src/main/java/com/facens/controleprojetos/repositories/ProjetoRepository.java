package com.facens.controleprojetos.repositories;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.facens.controleprojetos.models.Projeto;

public interface ProjetoRepository extends JpaRepository<Projeto, Integer> {

    @Query("select p from Projeto p left join fetch p.funcionarios where p.id = :id")
    Optional<Projeto> findByIdFetchFuncionarios(@Param("id") Integer id);

    List<Projeto> findByDataInicioBetween(LocalDate inicio, LocalDate fim);

    @Query("select distinct p from Projeto p left join fetch p.funcionarios order by p.id")
    List<Projeto> findAllFetchFuncionarios();
}

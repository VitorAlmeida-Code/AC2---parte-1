package com.facens.controleprojetos.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.facens.controleprojetos.models.Funcionario;
import com.facens.controleprojetos.models.Projeto;

public interface FuncionarioRepository extends JpaRepository<Funcionario, Integer> {

    @Query("select p from Projeto p join p.funcionarios f where f.id = :idFuncionario order by p.id")
    List<Projeto> findProjetosByFuncionarioId(@Param("idFuncionario") Integer idFuncionario);
}

package com.facens.controleprojetos.services;

import java.time.LocalDate;
import java.util.List;

import com.facens.controleprojetos.dtos.DadosProjetoDTO;
import com.facens.controleprojetos.dtos.ProjetoDTO;

public interface ProjetoService {
    void adicionar(ProjetoDTO dto);
    DadosProjetoDTO buscarProjetoPorId(Integer id);
    void vincularFuncionario(Integer idProjeto, Integer idFuncionario);
    List<DadosProjetoDTO> listarTodos();
    List<DadosProjetoDTO> buscarPorPeriodo(LocalDate inicio, LocalDate fim);
}

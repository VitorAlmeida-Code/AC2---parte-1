package com.facens.controleprojetos.services;

import java.util.List;

import com.facens.controleprojetos.dtos.DadosProjetoDTO;
import com.facens.controleprojetos.dtos.FuncionarioDTO;

public interface FuncionarioService {
    void adicionar(FuncionarioDTO dto);
    List<DadosProjetoDTO> buscarProjetos(Integer idFuncionario);
}

package com.facens.controleprojetos.services;

import java.util.List;

import com.facens.controleprojetos.dtos.DadosSetorDTO;
import com.facens.controleprojetos.dtos.SetorDTO;

public interface SetorService {
    void adicionar(SetorDTO dto);
    DadosSetorDTO buscarSetorPorId(Integer idSetor);
    List<DadosSetorDTO> listarTodos();
}

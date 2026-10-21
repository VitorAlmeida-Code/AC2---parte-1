package com.facens.controleprojetos.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.facens.controleprojetos.dtos.DadosFuncionarioDTO;
import com.facens.controleprojetos.dtos.DadosSetorDTO;
import com.facens.controleprojetos.dtos.SetorDTO;
import com.facens.controleprojetos.exceptions.RegraNegocioException;
import com.facens.controleprojetos.models.Setor;
import com.facens.controleprojetos.repositories.SetorRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SetorServiceImpl implements SetorService {

    private final SetorRepository setorRepository;

    @Override
    @Transactional
    public void adicionar(SetorDTO dto) {
        if (dto.getNome() == null || dto.getNome().isBlank()) {
            throw new RegraNegocioException("O nome do setor é obrigatório.");
        }
        String nome = dto.getNome().trim();
        if (nome.length() > 100) {
            throw new RegraNegocioException("O nome do setor deve ter no máximo 100 caracteres.");
        }
        if (setorRepository.existsByNomeIgnoreCase(nome)) {
            throw new RegraNegocioException("Já existe um setor cadastrado com esse nome.");
        }
        setorRepository.save(new Setor(nome));
    }

    @Override
    public DadosSetorDTO buscarSetorPorId(Integer idSetor) {
        return setorRepository.findByIdFetchFuncionarios(idSetor)
                .map(this::converter)
                .orElseThrow(() -> new RegraNegocioException("Setor não encontrado com o ID fornecido."));
    }

    @Override
    public List<DadosSetorDTO> listarTodos() {
        return setorRepository.findAllFetchFuncionarios().stream()
                .map(this::converter)
                .collect(Collectors.toList());
    }

    private DadosSetorDTO converter(Setor s) {
        return DadosSetorDTO.builder()
                .id(s.getId())
                .nome(s.getNome())
                .funcionarios(s.getFuncionarios().stream()
                        .map(f -> DadosFuncionarioDTO.builder().id(f.getId()).nome(f.getNome()).build())
                        .collect(Collectors.toList()))
                .build();
    }
}

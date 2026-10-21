package com.facens.controleprojetos.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.facens.controleprojetos.dtos.DadosFuncionarioDTO;
import com.facens.controleprojetos.dtos.DadosProjetoDTO;
import com.facens.controleprojetos.dtos.FuncionarioDTO;
import com.facens.controleprojetos.exceptions.RegraNegocioException;
import com.facens.controleprojetos.models.Funcionario;
import com.facens.controleprojetos.models.Projeto;
import com.facens.controleprojetos.models.Setor;
import com.facens.controleprojetos.repositories.FuncionarioRepository;
import com.facens.controleprojetos.repositories.SetorRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FuncionarioServiceImpl implements FuncionarioService {

    private final FuncionarioRepository funcionarioRepository;
    private final SetorRepository setorRepository;

    @Override
    @Transactional
    public void adicionar(FuncionarioDTO dto) {
        if (dto.getNome() == null || dto.getNome().isBlank()) {
            throw new RegraNegocioException("O nome do funcionário é obrigatório.");
        }
        if (dto.getNome().trim().length() > 100) {
            throw new RegraNegocioException("O nome do funcionário deve ter no máximo 100 caracteres.");
        }
        if (dto.getSetorId() == null) {
            throw new RegraNegocioException("O setor do funcionário é obrigatório.");
        }
        Setor setor = setorRepository.findById(dto.getSetorId())
                .orElseThrow(() -> new RegraNegocioException("Setor não encontrado com o ID fornecido."));

        Funcionario funcionario = new Funcionario(dto.getNome().trim());
        funcionario.setSetor(setor);
        funcionarioRepository.save(funcionario);
    }

    @Override
    @Transactional(readOnly = true)
    public List<DadosProjetoDTO> buscarProjetos(Integer idFuncionario) {
        if (!funcionarioRepository.existsById(idFuncionario)) {
            throw new RegraNegocioException("Funcionário não encontrado com o ID fornecido.");
        }
        return funcionarioRepository.findProjetosByFuncionarioId(idFuncionario).stream()
                .map(this::converter)
                .collect(Collectors.toList());
    }

    private DadosProjetoDTO converter(Projeto p) {
        return DadosProjetoDTO.builder()
                .id(p.getId())
                .descricao(p.getDescricao())
                .dataInicio(p.getDataInicio())
                .dataFim(p.getDataFim())
                .funcionarios(p.getFuncionarios().stream()
                        .map(f -> DadosFuncionarioDTO.builder().id(f.getId()).nome(f.getNome()).build())
                        .collect(Collectors.toList()))
                .build();
    }
}

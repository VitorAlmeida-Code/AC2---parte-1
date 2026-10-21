package com.facens.controleprojetos.services;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.facens.controleprojetos.dtos.DadosFuncionarioDTO;
import com.facens.controleprojetos.dtos.DadosProjetoDTO;
import com.facens.controleprojetos.dtos.ProjetoDTO;
import com.facens.controleprojetos.exceptions.RegraNegocioException;
import com.facens.controleprojetos.models.Funcionario;
import com.facens.controleprojetos.models.Projeto;
import com.facens.controleprojetos.repositories.FuncionarioRepository;
import com.facens.controleprojetos.repositories.ProjetoRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProjetoServiceImpl implements ProjetoService {

    private final ProjetoRepository projetoRepository;
    private final FuncionarioRepository funcionarioRepository;

    @Override
    @Transactional
    public void adicionar(ProjetoDTO dto) {
        if (dto.getDescricao() == null || dto.getDescricao().isBlank()) {
            throw new RegraNegocioException("A descrição do projeto é obrigatória.");
        }
        if (dto.getDescricao().trim().length() > 200) {
            throw new RegraNegocioException("A descrição do projeto deve ter no máximo 200 caracteres.");
        }
        if (dto.getDataInicio() == null) {
            throw new RegraNegocioException("A data de início do projeto é obrigatória.");
        }
        if (dto.getDataFim() == null) {
            throw new RegraNegocioException("A data de fim do projeto é obrigatória.");
        }
        if (dto.getDataFim().isBefore(dto.getDataInicio())) {
            throw new RegraNegocioException("A data de fim não pode ser anterior à data de início.");
        }

        Projeto projeto = new Projeto(dto.getDescricao().trim(), dto.getDataInicio(), dto.getDataFim());
        projetoRepository.save(projeto);
    }

    @Override
    public DadosProjetoDTO buscarProjetoPorId(Integer id) {
        return projetoRepository.findByIdFetchFuncionarios(id)
                .map(this::converter)
                .orElseThrow(() -> new RegraNegocioException("Projeto não encontrado com o ID fornecido."));
    }

    @Override
    @Transactional
    public void vincularFuncionario(Integer idProjeto, Integer idFuncionario) {
        Projeto projeto = projetoRepository.findByIdFetchFuncionarios(idProjeto)
                .orElseThrow(() -> new RegraNegocioException("Projeto não encontrado com o ID fornecido."));
        Funcionario funcionario = funcionarioRepository.findById(idFuncionario)
                .orElseThrow(() -> new RegraNegocioException("Funcionário não encontrado com o ID fornecido."));

        boolean jaVinculado = projeto.getFuncionarios().stream()
                .anyMatch(f -> f.getId().equals(funcionario.getId()));
        if (jaVinculado) {
            throw new RegraNegocioException("Funcionário já está vinculado a este projeto.");
        }

        projeto.getFuncionarios().add(funcionario);
        projetoRepository.save(projeto);
    }

    @Override
    public List<DadosProjetoDTO> listarTodos() {
        return projetoRepository.findAllFetchFuncionarios().stream()
                .map(this::converter)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<DadosProjetoDTO> buscarPorPeriodo(LocalDate inicio, LocalDate fim) {
        if (inicio == null || fim == null) {
            throw new RegraNegocioException("Informe a data de início e a data de fim do período.");
        }
        if (fim.isBefore(inicio)) {
            throw new RegraNegocioException("A data final do período não pode ser anterior à data inicial.");
        }
        return projetoRepository.findByDataInicioBetween(inicio, fim).stream()
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

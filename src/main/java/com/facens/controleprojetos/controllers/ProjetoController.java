package com.facens.controleprojetos.controllers;

import java.time.LocalDate;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.facens.controleprojetos.dtos.DadosProjetoDTO;
import com.facens.controleprojetos.dtos.ProjetoDTO;
import com.facens.controleprojetos.services.ProjetoService;

@RestController
@RequestMapping("/api/projetos")
public class ProjetoController {

    private final ProjetoService projetoService;

    public ProjetoController(ProjetoService projetoService) {
        this.projetoService = projetoService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void adicionar(@RequestBody ProjetoDTO projeto) {
        projetoService.adicionar(projeto);
    }

    @GetMapping("{id}")
    public DadosProjetoDTO buscarProjetoPorId(@PathVariable Integer id) {
        return projetoService.buscarProjetoPorId(id);
    }

    @PostMapping("{idProjeto}/funcionarios/{idFuncionario}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void vincularFuncionario(@PathVariable Integer idProjeto, @PathVariable Integer idFuncionario) {
        projetoService.vincularFuncionario(idProjeto, idFuncionario);
    }

    @GetMapping
    public List<DadosProjetoDTO> listarTodos() {
        return projetoService.listarTodos();
    }

    @GetMapping("periodo")
    public List<DadosProjetoDTO> buscarPorPeriodo(@RequestParam LocalDate inicio, @RequestParam LocalDate fim) {
        return projetoService.buscarPorPeriodo(inicio, fim);
    }
}

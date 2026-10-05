package com.example.professores.controller;

import com.example.professores.model.Professor;
import com.example.professores.service.ProfessorService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/professores")
public class ProfessorController {

    private final ProfessorService service;

    public ProfessorController(ProfessorService service) {
        this.service = service;
    }

    @GetMapping
    public List<Professor> listar() {
        return service.listar();
    }

    @GetMapping("/nome/{nome}")
    public List<Professor> buscarPorNome(@PathVariable String nome) {
        return service.buscarPorNome(nome);
    }

    @GetMapping("/area/{area}")
    public List<Professor> buscarPorArea(@PathVariable String area) {
        return service.buscarPorArea(area);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Professor cadastrar(@RequestBody Professor professor) {
        return service.cadastrar(professor);
    }

    @PutMapping("/{id}")
    public Professor atualizar(@PathVariable Long id, @RequestBody Professor professor) {
        return service.atualizar(id, professor);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void excluir(@PathVariable Long id) {
        service.excluir(id);
    }
}

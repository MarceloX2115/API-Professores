package com.example.professores.service;

import com.example.professores.model.Professor;
import com.example.professores.repository.ProfessorRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class ProfessorService {

    private final ProfessorRepository repository;

    public ProfessorService(ProfessorRepository repository) {
        this.repository = repository;
    }

    public List<Professor> listar() {
        return repository.findAll();
    }

    public List<Professor> buscarPorNome(String nome) {
        // Consulta derivada do Spring Data (parcial + case insensitive)
        Map<Long, Professor> resultado = new LinkedHashMap<>();
        for (Professor p : repository.findByNomeContainingIgnoreCase(nome)) {
            resultado.put(p.getId(), p);
        }

        // Complemento: ignora acentos (ex.: "joao" encontra "João")
        String alvo = semAcento(nome);
        for (Professor p : repository.findAll()) {
            if (p.getNome() != null && semAcento(p.getNome()).contains(alvo)) {
                resultado.putIfAbsent(p.getId(), p);
            }
        }
        return new ArrayList<>(resultado.values());
    }

    public List<Professor> buscarPorArea(String area) {
        Map<Long, Professor> resultado = new LinkedHashMap<>();
        for (Professor p : repository.findByAreaIgnoreCase(area)) {
            resultado.put(p.getId(), p);
        }

        String alvo = semAcento(area);
        for (Professor p : repository.findAll()) {
            if (p.getArea() != null && semAcento(p.getArea()).equals(alvo)) {
                resultado.putIfAbsent(p.getId(), p);
            }
        }
        return new ArrayList<>(resultado.values());
    }

    private String semAcento(String texto) {
        return Normalizer.normalize(texto, Normalizer.Form.NFD)
                .replaceAll("\\p{InCombiningDiacriticalMarks}+", "")
                .toLowerCase();
    }

    public Professor cadastrar(Professor professor) {
        professor.setId(null);
        return repository.save(professor);
    }

    public Professor atualizar(Long id, Professor dados) {
        Professor professor = repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Professor não encontrado"));
        professor.setNome(dados.getNome());
        professor.setEmail(dados.getEmail());
        professor.setArea(dados.getArea());
        professor.setTelefone(dados.getTelefone());
        return repository.save(professor);
    }

    public void excluir(Long id) {
        if (!repository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Professor não encontrado");
        }
        repository.deleteById(id);
    }
}
package com.evtechsolution.gerenciador_tarefas.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.evtechsolution.gerenciador_tarefas.entities.Tarefa;
import com.evtechsolution.gerenciador_tarefas.repositories.TarefaRepository;



@Service
public class TarefaService {

    private final TarefaRepository tarefaRepository;

    public TarefaService(TarefaRepository tarefaRepository) {
        this.tarefaRepository = tarefaRepository;
    }

    public List<Tarefa> findAll() {
        return tarefaRepository.findAll();
    }

    public Tarefa findById(Long id) {
        return tarefaRepository.findById(id).orElse(null);
    }

    public List<Tarefa> findByUserId(Long userId) {
        return tarefaRepository.findByUserId(userId);
    }

    public Tarefa save(Tarefa tarefa) {
        return tarefaRepository.save(tarefa);
    }

    public void deleteById(Long id) {
        tarefaRepository.deleteById(id);
    }
}
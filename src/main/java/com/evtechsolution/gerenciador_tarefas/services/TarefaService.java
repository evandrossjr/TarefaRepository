package com.evtechsolution.gerenciador_tarefas.services;

import java.nio.file.AccessDeniedException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.evtechsolution.gerenciador_tarefas.entities.Tarefa;
import com.evtechsolution.gerenciador_tarefas.entities.User;
import com.evtechsolution.gerenciador_tarefas.exceptions.DatabaseException;
import com.evtechsolution.gerenciador_tarefas.exceptions.ResourceNotFoundException;
import com.evtechsolution.gerenciador_tarefas.repositories.TarefaRepository;
import com.evtechsolution.gerenciador_tarefas.repositories.UserRepository;

import jakarta.persistence.EntityNotFoundException;



@Service
public class TarefaService {

    @Autowired
    private TarefaRepository tarefaRepository;

    @Autowired
    private UserRepository userRepository;

    
    
    public List<Tarefa> findAll(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User Not Found"));
        return tarefaRepository.findByUser(user);
    }

    public Tarefa findById(Long id, String email) throws AccessDeniedException {
        Tarefa tarefa = tarefaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Tarefa não encontrada"));
        if (!tarefa.getUser().getEmail().equals(email)) {
            throw new AccessDeniedException("Acesso negado.");
        }
        return tarefa;
    }

    public Tarefa insert(Tarefa obj, String username) {
        User user = userRepository.findByEmail(username)
            .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado"));
        
        obj.setUser(user);
        return tarefaRepository.save(obj);
    }

    public void delete(Long id, String username) throws AccessDeniedException {
        if (!tarefaRepository.existsById(id)) {
            throw new ResourceNotFoundException("Tarefa não encontrada com ID: " + id);
        }

        Tarefa tarefa = tarefaRepository.findById(id).orElseThrow();
        if (!tarefa.getUser().getUsername().equals(username)) {
            throw new AccessDeniedException("Acesso negado.");
        }

        try {
            tarefaRepository.deleteById(id);
        } catch (DataIntegrityViolationException e) {
            throw new DatabaseException(e.getMessage());
        }
    }

    @Transactional
    public Tarefa update(Long id, Tarefa obj, String username) throws AccessDeniedException {
        try {
            Tarefa entity = tarefaRepository.getReferenceById(id);

            if (!entity.getUser().getUsername().equals(username)) {
                throw new AccessDeniedException("Acesso negado.");
            }

            updateData(entity, obj);
            return tarefaRepository.save(entity);
        } catch (EntityNotFoundException e) {
            throw new ResourceNotFoundException(id);
        }
    }

    private void updateData(Tarefa entity, Tarefa obj) {
        entity.setTitulo(obj.getTitulo());
        entity.setDescricao(obj.getDescricao());
        entity.setStatus(obj.getStatus());
    }


}

package com.evtechsolution.gerenciador_tarefas.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.evtechsolution.gerenciador_tarefas.entities.Tarefa;
import com.evtechsolution.gerenciador_tarefas.repositories.TarefaRepository;

import ch.qos.logback.core.net.SyslogOutputStream;
import jakarta.persistence.EntityNotFoundException;



@Service
public class TarefaService {
	
	@Autowired
	private TarefaRepository tarefaRepository;

	public List<Tarefa> findAll(){
		return tarefaRepository.findAll();
	}
	
	public Tarefa findById(Long id) {
		Optional<Tarefa> obj = tarefaRepository.findById(id);
		return obj.orElseThrow(()-> new EntityNotFoundException("Tarefa não encontrada!"));		
	}
	
	public Tarefa insert(Tarefa obj) {
		return tarefaRepository.save(obj);
	}
	
	public boolean delete(Long id) {
		if(tarefaRepository.existsById(id)) {
			tarefaRepository.deleteById(id);
			return true;
			
		}
		return false;
	}
	public Tarefa update(Long id, Tarefa obj) {
        Tarefa existingTarefa = findById(id); 
        existingTarefa.setTitulo(obj.getTitulo());
        existingTarefa.setDescricao(obj.getDescricao());
        existingTarefa.setStatus(obj.getStatus());
        existingTarefa.setDataCriacao(obj.getDataCriacao());
        
        return tarefaRepository.save(existingTarefa);
	}
	
}

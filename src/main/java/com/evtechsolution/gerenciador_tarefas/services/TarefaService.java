package com.evtechsolution.gerenciador_tarefas.services;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.evtechsolution.gerenciador_tarefas.entities.Tarefa;
import com.evtechsolution.gerenciador_tarefas.exceptions.DatabaseException;
import com.evtechsolution.gerenciador_tarefas.exceptions.ResourceNotFoundException;
import com.evtechsolution.gerenciador_tarefas.repositories.TarefaRepository;

import jakarta.persistence.EntityNotFoundException;



@Service
public class TarefaService {
	
	@Autowired
	private TarefaRepository tarefaRepository;

	private static final Logger logger = LoggerFactory.getLogger(Tarefa.class);

	
	public List<Tarefa> findAll(){
		return tarefaRepository.findAll();
	}
	
	public Tarefa findById(Long id) {
		Optional<Tarefa> obj = tarefaRepository.findById(id);
		return obj.orElseThrow(()-> new ResourceNotFoundException("Tarefa não encontrada!"));		
	}
	
	public Tarefa insert(Tarefa obj) {
		return tarefaRepository.save(obj);
	}
	
	//public boolean delete(Long id) {
	//	if(tarefaRepository.existsById(id)) {
	//		tarefaRepository.deleteById(id);
	//		return true;
	//	}
	//	return false;
	//}
	
	public void delete(Long id) {
		if (!tarefaRepository.existsById(id)) {
			throw new ResourceNotFoundException("Tarefa não encontrada com ID: " + id);
		}
		try {
		tarefaRepository.deleteById(id);
		logger.info("Tarefa com ID {} deletada com sucesso.", id);
          
		}
		catch (DataIntegrityViolationException e){
            logger.error("Erro de integridade ao tentar deletar tarefa com ID {}: {}", id, e.getMessage());
			throw new DatabaseException(e.getMessage());

			
		}
	}


	//public Tarefa update(Long id, Tarefa obj) {
     //   Tarefa existingTarefa = findById(id); 
     //   existingTarefa.setTitulo(obj.getTitulo());
     //   existingTarefa.setDescricao(obj.getDescricao());
    //    existingTarefa.setStatus(obj.getStatus());
    //    existingTarefa.setDataCriacao(obj.getDataCriacao());    
    //    return tarefaRepository.save(existingTarefa);
	//}
	
	@Transactional
	public Tarefa update(Long id, Tarefa obj) {
		try {
			Tarefa entity = tarefaRepository.getReferenceById(id);
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

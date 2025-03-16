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
import com.evtechsolution.gerenciador_tarefas.entities.User;
import com.evtechsolution.gerenciador_tarefas.exceptions.DatabaseException;
import com.evtechsolution.gerenciador_tarefas.exceptions.ResourceNotFoundException;
import com.evtechsolution.gerenciador_tarefas.repositories.TarefaRepository;
import com.evtechsolution.gerenciador_tarefas.repositories.UserRepository;

import jakarta.persistence.EntityNotFoundException;



@Service
public class UserService {
	
	@Autowired
	private UserRepository userRepository;

	private static final Logger logger = LoggerFactory.getLogger(Tarefa.class);

    @Transactional(readOnly = true)
	public List<User> findAll(){
		return userRepository.findAll();
	}
	
	public User findById(Long id) {
		Optional<User> obj = userRepository.findById(id);
		return obj.orElseThrow(()-> new ResourceNotFoundException("Tarefa não encontrada!"));		
	}
	
	public User insert(User obj) {
		return userRepository.save(obj);
	}
	
	//public boolean delete(Long id) {
	//	if(tarefaRepository.existsById(id)) {
	//		tarefaRepository.deleteById(id);
	//		return true;
	//	}
	//	return false;
	//}
	
	public void delete(Long id) {
		if (!userRepository.existsById(id)) {
			throw new ResourceNotFoundException("Usuário não encontrado com ID: " + id);
		}
		try {
			userRepository.deleteById(id);
		logger.info("Usuário com ID {} deletada com sucesso.", id);
          
		}
		catch (DataIntegrityViolationException e){
            logger.error("Erro de integridade ao tentar deletar usuário com ID {}: {}", id, e.getMessage());
			throw new DatabaseException(e.getMessage());

			
		}
	}



	
	@Transactional
	public User update(Long id, User obj) {
		try {
			User entity = userRepository.getReferenceById(id);
			updateData(entity, obj);
			return userRepository.save(entity);	
		} catch (EntityNotFoundException e) {
			throw new ResourceNotFoundException(id);
		}	
	}

	private void updateData(User entity, User obj) {
		entity.setName(obj.getName());
		entity.setEmail(obj.getEmail());
		entity.setPassword(obj.getPassword());
	}
	
}

package com.evtechsolution.gerenciador_tarefas.services;

import java.nio.file.AccessDeniedException;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.evtechsolution.gerenciador_tarefas.entities.Tarefa;
import com.evtechsolution.gerenciador_tarefas.entities.User;
import com.evtechsolution.gerenciador_tarefas.exceptions.DatabaseException;
import com.evtechsolution.gerenciador_tarefas.exceptions.ResourceNotFoundException;
import com.evtechsolution.gerenciador_tarefas.repositories.UserRepository;

import jakarta.persistence.EntityNotFoundException;



@Service
public class UserService {
	
	@Autowired
	private UserRepository userRepository;

		
	private static final Logger logger = LoggerFactory.getLogger(Tarefa.class);
	
	@Autowired
    private PasswordEncoder passwordEncoder;

    public void salvarUsuario(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
    }

    @Transactional(readOnly = true)
	public List<User> findAll(){
		return userRepository.findAll();
	}
	
    @Transactional(readOnly = true)
    public User findById(Long id, String username) throws AccessDeniedException {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado!"));
        
        // Usuário só pode acessar os próprios dados, a não ser que seja ADMIN
        if (!user.getUsername().equals(username) && !isAdmin(username)) {
            throw new AccessDeniedException("Acesso negado.");
        }

        return user;
    }
	
    private boolean isAdmin(String email) {
        Optional<User> user = userRepository.findByEmail(email);
        return user != null && user.get().getRole().equals("ADMIN");
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
    public User update(Long id, User obj, String username) throws AccessDeniedException {
        try {
            User entity = userRepository.getReferenceById(id);

            if (!entity.getUsername().equals(username) && !isAdmin(username)) {
                throw new AccessDeniedException("Acesso negado.");
            }

            updateData(entity, obj);
            return userRepository.save(entity);
        } catch (EntityNotFoundException e) {
            throw new ResourceNotFoundException(id);
        }
    }
	
	private void updateData(User entity, User obj) {
        entity.setName(obj.getName());
        entity.setEmail(obj.getEmail());
        entity.setPassword(passwordEncoder.encode(obj.getPassword()));
    }
	
}

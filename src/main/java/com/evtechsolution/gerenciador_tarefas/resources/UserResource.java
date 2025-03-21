package com.evtechsolution.gerenciador_tarefas.resources;

import java.nio.file.AccessDeniedException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.evtechsolution.gerenciador_tarefas.entities.User;
import com.evtechsolution.gerenciador_tarefas.exceptions.ResourceNotFoundException;
import com.evtechsolution.gerenciador_tarefas.repositories.UserRepository;
import com.evtechsolution.gerenciador_tarefas.services.UserService;


@RestController
@RequestMapping("/users")
public class UserResource {

    @Autowired
    private UserService userService;
    
    @Autowired
    private UserRepository userRepository;

    // ADMIN - Listar todos os usuários
    @GetMapping
    public ResponseEntity<List<User>> findAll(Authentication authentication) {
        if (!userService.isAdmin(authentication.getName())) {
            return ResponseEntity.status(403).build();
        }
        return ResponseEntity.ok(userService.findAll());
    }

    // Usuário pode ver apenas seu próprio usuário
    @GetMapping("/{id}")
    public ResponseEntity<User> findById(@PathVariable Long id, Authentication authentication) throws AccessDeniedException {
        User user = userService.findById(id, authentication.getName());
        return ResponseEntity.ok(user);
    }
    
    @GetMapping
    public ResponseEntity<List<User>> findAll() {
        return ResponseEntity.ok(userService.findAll());
    }

    public User findById(Long id, String username) throws AccessDeniedException {
        User user = userRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado"));

        if (!user.getEmail().equals(username)) {
            throw new AccessDeniedException("Acesso negado.");
        }
        return user;
    }
    
    private User userService(Long id, String name) {
		// TODO Auto-generated method stub
		return null;
	}

	// Atualizar o próprio usuário
    @PutMapping("/{id}")
    public ResponseEntity<User> update(@PathVariable Long id, @RequestBody User user, Authentication authentication) throws AccessDeniedException {
        User updatedUser = userService.update(id, user, authentication.getName());
        return ResponseEntity.ok(updatedUser);
    }

    // Deletar o próprio usuário
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id, Authentication authentication) throws AccessDeniedException {
        userService.delete(id, authentication.getName());
        return ResponseEntity.noContent().build();
    }
}

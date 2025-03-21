package com.evtechsolution.gerenciador_tarefas.resources;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.evtechsolution.gerenciador_tarefas.entities.User;
import com.evtechsolution.gerenciador_tarefas.entities.enums.UserRole;
import com.evtechsolution.gerenciador_tarefas.services.UserService;


@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    public UserController(UserService userService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        User currentUser = getCurrentUser();
        
        // Apenas ADMIN pode listar todos os usuários
        if (currentUser.getRole() == UserRole.ADMIN) {
            return ResponseEntity.ok(userService.findAll());
        }
        
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        User currentUser = getCurrentUser();
        User user = userService.findById(id);
        
        if (user == null) {
            return ResponseEntity.notFound().build();
        }
        
        // Verifica se o usuário tem permissão para acessar este usuário
        if (currentUser.getRole() == UserRole.ADMIN || 
            currentUser.getId().equals(id)) {
            return ResponseEntity.ok(user);
        }
        
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody User user) {
        User currentUser = getCurrentUser();
        User existingUser = userService.findById(id);
        
        if (existingUser == null) {
            return ResponseEntity.notFound().build();
        }
        
        // Verifica se o usuário tem permissão para atualizar este usuário
        if (currentUser.getRole() == UserRole.ADMIN || 
            currentUser.getId().equals(id)) {
            
            user.setId(id);
            
            // Se a senha foi fornecida, criptografa-a
            if (user.getPassword() != null && !user.getPassword().isEmpty()) {
                user.setPassword(passwordEncoder.encode(user.getPassword()));
            } else {
                user.setPassword(existingUser.getPassword());
            }
            
            // Apenas ADMIN pode alterar a role de um usuário
            if (currentUser.getRole() != UserRole.ADMIN) {
                user.setRole(existingUser.getRole());
            }
            
            User updatedUser = userService.save(user);
            return ResponseEntity.ok(updatedUser);
        }
        
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        User currentUser = getCurrentUser();
        User existingUser = userService.findById(id);
        
        if (existingUser == null) {
            return ResponseEntity.notFound().build();
        }
        
        // Apenas ADMIN pode deletar qualquer usuário
        // Um usuário comum só pode deletar a si mesmo
        if (currentUser.getRole() == UserRole.ADMIN || 
            currentUser.getId().equals(id)) {
            
            userService.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }
    
    // Método auxiliar para obter o usuário atual
    private User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return (User) authentication.getPrincipal();
    }
}
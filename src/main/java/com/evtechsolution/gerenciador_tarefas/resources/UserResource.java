package com.evtechsolution.gerenciador_tarefas.resources;

import java.net.URI;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.evtechsolution.gerenciador_tarefas.dtos.TarefaDTO;
import com.evtechsolution.gerenciador_tarefas.dtos.UserDTO;
import com.evtechsolution.gerenciador_tarefas.entities.User;
import com.evtechsolution.gerenciador_tarefas.infra.security.TokenService;
import com.evtechsolution.gerenciador_tarefas.repositories.UserRepository;
import com.evtechsolution.gerenciador_tarefas.services.UserService;

import jakarta.validation.Valid;


@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

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
    public ResponseEntity<User> findById(@PathVariable Long id, Authentication authentication) {
        User user = userService.findById(id, authentication.getName());
        return ResponseEntity.ok(user);
    }

    // Atualizar o próprio usuário
    @PutMapping("/{id}")
    public ResponseEntity<User> update(@PathVariable Long id, @RequestBody User user, Authentication authentication) {
        User updatedUser = userService.update(id, user, authentication.getName());
        return ResponseEntity.ok(updatedUser);
    }

    // Deletar o próprio usuário
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id, Authentication authentication) {
        userService.delete(id, authentication.getName());
        return ResponseEntity.noContent().build();
    }
}

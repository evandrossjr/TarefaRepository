package com.evtechsolution.gerenciador_tarefas.resources;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.evtechsolution.gerenciador_tarefas.entities.Tarefa;
import com.evtechsolution.gerenciador_tarefas.entities.User;
import com.evtechsolution.gerenciador_tarefas.infra.security.TokenService;
import com.evtechsolution.gerenciador_tarefas.repositories.TarefaRepository;
import com.evtechsolution.gerenciador_tarefas.repositories.UserRepository;

import lombok.RequiredArgsConstructor;


@RestController
@RequestMapping("/tarefas")
public class TarefaResource {

	@Autowired
    private  TarefaRepository tarefaRepository;
	
	@Autowired
    private  UserRepository userRepository;
	
	@Autowired
    private  TokenService tokenService;

    private User getAuthenticatedUser(String token) {
        String email = tokenService.extractUsername(token.replace("Bearer ", ""));
        return userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
    }

    @GetMapping
    public ResponseEntity<List<Tarefa>> listarTarefas(@RequestHeader("Authorization") String token) {
        User user = getAuthenticatedUser(token);
        
        if (user.getRole().equals("ADMIN")) {
            return ResponseEntity.ok(tarefaRepository.findAll()); // ADMIN vê tudo
        } else {
            return ResponseEntity.ok(tarefaRepository.findByUserId(user.getId())); // USER vê só as próprias tarefas
        }
    }

    @PostMapping
    public ResponseEntity<Tarefa> criarTarefa(@RequestHeader("Authorization") String token, @RequestBody Tarefa tarefa) {
        User user = getAuthenticatedUser(token);
        tarefa.setUser(user); // Garante que a tarefa pertence ao usuário autenticado
        return ResponseEntity.ok(tarefaRepository.save(tarefa));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Tarefa> atualizarTarefa(@RequestHeader("Authorization") String token, @PathVariable Long id, @RequestBody Tarefa tarefaAtualizada) {
        User user = getAuthenticatedUser(token);
        Tarefa tarefa = tarefaRepository.findById(id).orElseThrow(() -> new RuntimeException("Tarefa não encontrada"));

        if (user.getRole().equals("ADMIN") || tarefa.getUser().getId().equals(user.getId())) {
            tarefa.setTitulo(tarefaAtualizada.getTitulo());
            tarefa.setDescricao(tarefaAtualizada.getDescricao());
            return ResponseEntity.ok(tarefaRepository.save(tarefa));
        }

        return ResponseEntity.status(HttpStatus.FORBIDDEN).build(); // Bloqueia alteração se não for dono
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarTarefa(@RequestHeader("Authorization") String token, @PathVariable Long id) {
        User user = getAuthenticatedUser(token);
        Tarefa tarefa = tarefaRepository.findById(id).orElseThrow(() -> new RuntimeException("Tarefa não encontrada"));

        if (user.getRole().equals("ADMIN") || tarefa.getUser().getId().equals(user.getId())) {
            tarefaRepository.delete(tarefa);
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.status(HttpStatus.FORBIDDEN).build(); // Bloqueia exclusão se não for dono
    }
    
   
    
}
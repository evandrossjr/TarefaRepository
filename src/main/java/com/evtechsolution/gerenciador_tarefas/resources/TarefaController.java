package com.evtechsolution.gerenciador_tarefas.resources;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.evtechsolution.gerenciador_tarefas.entities.Tarefa;
import com.evtechsolution.gerenciador_tarefas.entities.User;
import com.evtechsolution.gerenciador_tarefas.entities.enums.UserRole;
import com.evtechsolution.gerenciador_tarefas.services.TarefaService;





@RestController
@RequestMapping("/tarefas")
public class TarefaController {

    private final TarefaService tarefaService;

    public TarefaController(TarefaService tarefaService) {
        this.tarefaService = tarefaService;
    }

    @GetMapping
    public ResponseEntity<List<Tarefa>> getAllTarefas() {
        User currentUser = getCurrentUser();
        
        // Se for ADMIN, retorna todas as tarefas
        if (currentUser.getRole() == UserRole.ADMIN) {
            return ResponseEntity.ok(tarefaService.findAll());
        }
        
        // Se for USER, retorna apenas suas tarefas
        return ResponseEntity.ok(tarefaService.findByUserId(currentUser.getId()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Tarefa> getTarefaById(@PathVariable Long id) {
        User currentUser = getCurrentUser();
        Tarefa tarefa = tarefaService.findById(id);
        
        if (tarefa == null) {
            return ResponseEntity.notFound().build();
        }
        
        // Verifica se o usuário tem permissão para acessar esta tarefa
        if (currentUser.getRole() == UserRole.ADMIN || 
            tarefa.getUser().getId().equals(currentUser.getId())) {
            return ResponseEntity.ok(tarefa);
        }
        
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    @PostMapping
    public ResponseEntity<Tarefa> createTarefa(@RequestBody Tarefa tarefa) {
        User currentUser = getCurrentUser();
        
        // Define o usuário atual como dono da tarefa
        tarefa.setUser(currentUser);
        
        Tarefa createdTarefa = tarefaService.save(tarefa);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdTarefa);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Tarefa> updateTarefa(@PathVariable Long id, @RequestBody Tarefa tarefa) {
        User currentUser = getCurrentUser();
        Tarefa existingTarefa = tarefaService.findById(id);
        
        if (existingTarefa == null) {
            return ResponseEntity.notFound().build();
        }
        
        // Verifica se o usuário tem permissão para atualizar esta tarefa
        if (currentUser.getRole() == UserRole.ADMIN || 
            existingTarefa.getUser().getId().equals(currentUser.getId())) {
            
            tarefa.setId(id);
            // Mantém o usuário original da tarefa
            tarefa.setUser(existingTarefa.getUser());
            
            Tarefa updatedTarefa = tarefaService.save(tarefa);
            return ResponseEntity.ok(updatedTarefa);
        }
        
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTarefa(@PathVariable Long id) {
        User currentUser = getCurrentUser();
        Tarefa existingTarefa = tarefaService.findById(id);
        
        if (existingTarefa == null) {
            return ResponseEntity.notFound().build();
        }
        
        // Verifica se o usuário tem permissão para deletar esta tarefa
        if (currentUser.getRole() == UserRole.ADMIN || 
            existingTarefa.getUser().getId().equals(currentUser.getId())) {
            
            tarefaService.deleteById(id);
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
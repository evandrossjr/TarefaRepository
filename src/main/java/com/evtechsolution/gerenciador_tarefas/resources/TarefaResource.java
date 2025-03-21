package com.evtechsolution.gerenciador_tarefas.resources;

import java.nio.file.AccessDeniedException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.evtechsolution.gerenciador_tarefas.entities.Tarefa;
import com.evtechsolution.gerenciador_tarefas.services.TarefaService;





@RestController
@RequestMapping("/tarefas")
public class TarefaResource {

    @Autowired
    private TarefaService tarefaService;

    @GetMapping
    public ResponseEntity<List<Tarefa>> findAll(Authentication  authentication) {
        return ResponseEntity.ok(tarefaService.findAll(authentication.getName()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Tarefa> findById(@PathVariable Long id, Authentication authentication) throws AccessDeniedException {
        Tarefa tarefa = tarefaService.findById(id, authentication.getName());
        return ResponseEntity.ok(tarefa);
    }

    @PostMapping
    public ResponseEntity<Tarefa> insert(@RequestBody Tarefa tarefa, Authentication authentication) {
        Tarefa createdTarefa = tarefaService.insert(tarefa, authentication.getName());
        return ResponseEntity.ok(createdTarefa);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Tarefa> update(@PathVariable Long id, @RequestBody Tarefa tarefa, Authentication authentication) throws AccessDeniedException {
        Tarefa updatedTarefa = tarefaService.update(id, tarefa, authentication.getName());
        return ResponseEntity.ok(updatedTarefa);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id, Authentication authentication) throws AccessDeniedException {
        tarefaService.delete(id, authentication.getName());
        return ResponseEntity.noContent().build();
    }
}


package com.evtechsolution.gerenciador_tarefas.resources;

import java.net.URI;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.evtechsolution.gerenciador_tarefas.entities.Tarefa;
import com.evtechsolution.gerenciador_tarefas.services.TarefaService;

import jakarta.servlet.Servlet;
import jakarta.websocket.server.PathParam;

import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequestMapping(value = "/tarefas")
public class TarefaResource {
	
	@Autowired
	private TarefaService tarefaService;
	
	@GetMapping
	public ResponseEntity<List<Tarefa>> findAll(){
		List<Tarefa> list = tarefaService.findAll();
		return ResponseEntity.ok().body(list);
	}
	
	
	@GetMapping(value = "/{id}")
	public ResponseEntity<Tarefa> findById(@PathVariable Long id){
		Tarefa obj = tarefaService.findById(id);
		return ResponseEntity.ok().body(obj);
	}
	
	@PostMapping
	public ResponseEntity<Tarefa> insert(@RequestBody Tarefa obj){
		obj = tarefaService.insert(obj);
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path(null).buildAndExpand(obj.getId()).toUri();
		return ResponseEntity.created(uri).body(obj);
	}
	
	@DeleteMapping(value = "/{id}")
	public ResponseEntity<Void> delete(@PathVariable Long id){
		tarefaService.delete(id);
		return ResponseEntity.noContent().build();
	}
	
	
	@PutMapping(value = "/{id}")
	public ResponseEntity<Tarefa> udpate(@PathVariable Long id, @RequestBody Tarefa obj){
		obj = tarefaService.update(id, obj);
		return ResponseEntity.ok().body(obj);
	}

	

}

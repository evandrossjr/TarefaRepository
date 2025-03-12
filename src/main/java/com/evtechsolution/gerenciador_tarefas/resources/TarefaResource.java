package com.evtechsolution.gerenciador_tarefas.resources;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.evtechsolution.gerenciador_tarefas.entities.Tarefa;
import com.evtechsolution.gerenciador_tarefas.services.TarefaService;

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
	

}

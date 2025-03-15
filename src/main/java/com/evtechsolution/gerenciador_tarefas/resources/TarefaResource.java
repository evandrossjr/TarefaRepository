package com.evtechsolution.gerenciador_tarefas.resources;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

import com.evtechsolution.gerenciador_tarefas.dtos.TarefaDTO;
import com.evtechsolution.gerenciador_tarefas.entities.Tarefa;
import com.evtechsolution.gerenciador_tarefas.services.TarefaService;

import jakarta.validation.Valid;


@RestController
@RequestMapping(value = "/tarefas")
public class TarefaResource {
	
	 private static final Logger logger = LoggerFactory.getLogger(TarefaResource.class);
	
	@Autowired
	private TarefaService tarefaService;
	
	@GetMapping
	public ResponseEntity<List<TarefaDTO>> findAll(){
		List<Tarefa> list = tarefaService.findAll();
		List<TarefaDTO> dtoList = list.stream()
				.map(t -> new TarefaDTO(t.getId(), t.getTitulo(), t.getDescricao(), t.getStatus(), t.getDataCriacao()))
				.toList();
		return ResponseEntity.ok().body(dtoList);
	}
	
	
	@GetMapping(value = "/{id}")
	public ResponseEntity<TarefaDTO> findById(@PathVariable Long id){
		Tarefa obj = tarefaService.findById(id);
		TarefaDTO dto = new TarefaDTO(obj.getId(), obj.getTitulo(), obj.getDescricao(), obj.getStatus(), obj.getDataCriacao());
		return ResponseEntity.ok().body(dto);
	}
	
	@PostMapping
	public ResponseEntity<TarefaDTO> insert(@RequestBody @Valid TarefaDTO dto){
		Tarefa obj = new Tarefa();
		obj.setTitulo(dto.titulo());
		obj.setDescricao(dto.descricao());
		obj.setStatus(dto.status());
		obj.setDataCriacao(dto.dataCriacao() != null ? dto.dataCriacao() : LocalDateTime.now());
		
		obj = tarefaService.insert(obj);
		
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(obj.getId()).toUri();
		return ResponseEntity.created(uri).body(new TarefaDTO(obj.getId(), obj.getTitulo(), obj.getDescricao(), obj.getStatus(), obj.getDataCriacao()));
	}
	
	@DeleteMapping(value = "/{id}")
	public ResponseEntity<Void> delete(@PathVariable Long id){
		tarefaService.delete(id);
		logger.info("Tarefa com ID {} deletada com sucesso", id);
		return ResponseEntity.noContent().build();
	}
	
	
	@PutMapping(value = "/{id}")
	public ResponseEntity<TarefaDTO> udpate(@PathVariable Long id, @RequestBody @Valid TarefaDTO dto){
		Tarefa obj = new Tarefa();
	    obj.setId(id);
	    obj.setTitulo(dto.titulo());
	    obj.setDescricao(dto.descricao());
	    obj.setStatus(dto.status());
	    obj.setDataCriacao(dto.dataCriacao());
		
		obj = tarefaService.update(id, obj);
		return ResponseEntity.ok()
				.body(new TarefaDTO(obj.getId(), obj.getTitulo(), obj.getDescricao(), obj.getStatus(), obj.getDataCriacao()));
	}

	

}

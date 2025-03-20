package com.evtechsolution.gerenciador_tarefas.resources;

import java.net.URI;
import java.time.LocalDateTime;
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
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.evtechsolution.gerenciador_tarefas.dtos.TarefaDTO;
import com.evtechsolution.gerenciador_tarefas.entities.Tarefa;
import com.evtechsolution.gerenciador_tarefas.entities.User;
import com.evtechsolution.gerenciador_tarefas.infra.security.TokenService;
import com.evtechsolution.gerenciador_tarefas.repositories.TarefaRepository;
import com.evtechsolution.gerenciador_tarefas.repositories.UserRepository;
import com.evtechsolution.gerenciador_tarefas.services.TarefaService;
import com.evtechsolution.gerenciador_tarefas.services.UserService;

import jakarta.validation.Valid;





@RestController
@RequestMapping("/tarefas")
public class TarefaResource {
	
	@Autowired
	private TarefaService tarefaService;

	@Autowired
	private UserService userService;
	
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
	public ResponseEntity<List<TarefaDTO>> findAll(){
		List<Tarefa> list = tarefaService.findAll();
		List<TarefaDTO> dtoList = list.stream()
				.map(t -> new TarefaDTO(t.getId(), t.getTitulo(), t.getDescricao(), t.getStatus(), t.getDataCriacao(),t.getUser().getId()))
				.toList();
		return ResponseEntity.ok().body(dtoList);
	}
    
    @GetMapping(value = "/{id}")
	public ResponseEntity<TarefaDTO> findById(@PathVariable Long id){
		Tarefa obj = tarefaService.findById(id);
		TarefaDTO dto = new TarefaDTO(obj.getId(), obj.getTitulo(), obj.getDescricao(), obj.getStatus(), obj.getDataCriacao(), obj.getUser().getId());
		return ResponseEntity.ok().body(dto);
	}

    @PostMapping
	public ResponseEntity<TarefaDTO> insert(@RequestBody @Valid TarefaDTO dto){
		User user = userService.findById(dto.userId());
		if (user == null) {
			return ResponseEntity.badRequest().body(null);
		}
		
		Tarefa obj = new Tarefa();
		obj.setTitulo(dto.titulo());
		obj.setDescricao(dto.descricao());
		obj.setStatus(dto.status());
		obj.setDataCriacao(dto.dataCriacao() != null ? dto.dataCriacao() : LocalDateTime.now());

		obj.setUser(user);
		
		obj = tarefaService.insert(obj);

		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(obj.getId()).toUri();
		return ResponseEntity.created(uri).body(new TarefaDTO(obj.getId(), obj.getTitulo(), obj.getDescricao(), obj.getStatus(), obj.getDataCriacao(), obj.getUser().getId()));
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

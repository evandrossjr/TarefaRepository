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
@RequestMapping(value = "/users")
public class UserResource {
	
	 private static final Logger logger = LoggerFactory.getLogger(UserResource.class);
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
    private  TokenService tokenService;
	
	private User getAuthenticatedUser(String token) {
        String email = tokenService.extractUsername(token.replace("Bearer ", ""));
        return userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
    }
	
	@GetMapping
	public ResponseEntity<List<UserDTO>> findAll(){
		List<User> list = userService.findAll();
		List<UserDTO> dtoList = list.stream()
		        .map(user -> {
		            List<TarefaDTO> tarefasDTO = user.getTarefasList().stream()
		                .map(t -> new TarefaDTO(
		                    t.getId(), t.getTitulo(), t.getDescricao(), 
		                    t.getStatus(), t.getDataCriacao(), t.getUser().getId()
		                )).toList();

		            return new UserDTO(user.getId(), user.getName(), user.getEmail(), user.getPassword(), user.getRole(), tarefasDTO);
		        }).toList();
		    
		    return ResponseEntity.ok().body(dtoList);
	}
	
	
	@GetMapping(value = "/{id}")
	public ResponseEntity<UserDTO> findById(@PathVariable Long id){
		User obj = userService.findById(id);
		
		List<TarefaDTO> tarefasDTO = obj.getTarefasList().stream()
		        .map(t -> new TarefaDTO(
		            t.getId(), t.getTitulo(), t.getDescricao(), 
		            t.getStatus(), t.getDataCriacao(), t.getUser().getId() 
		        )).toList();
		
		UserDTO dto = new UserDTO(obj.getId(), obj.getName(),obj.getEmail(), obj.getPassword(), obj.getRole(), tarefasDTO);
		return ResponseEntity.ok().body(dto);
	}
	
	@PostMapping
	public ResponseEntity<UserDTO> insert(@RequestBody @Valid UserDTO dto){
		User obj = new User();
		obj.setName(dto.name());
		obj.setEmail(dto.email());
		obj.setPassword(new BCryptPasswordEncoder().encode(dto.password()));
		obj.setRole(dto.role());
		
		obj = userService.insert(obj);
		
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(obj.getId()).toUri();
		return ResponseEntity.created(uri).body(new UserDTO(obj.getId(), obj.getName(), obj.getPassword(), obj.getPassword(), obj.getRole()));
	}
	
	@DeleteMapping(value = "/{id}")
	public ResponseEntity<Void> delete(@PathVariable Long id){
		userService.delete(id);
		logger.info("Usuário com ID {} deletada com sucesso", id);
		return ResponseEntity.noContent().build();
	}
	
	
	


	 @PutMapping("/{id}")
	    public ResponseEntity<User> atualizarUsuario(@RequestHeader("Authorization") String token, @PathVariable Long id, @RequestBody User userAtualizado) {
	        User authUser = getAuthenticatedUser(token);
	        User user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

	        // Se o usuário autenticado não for ADMIN, ele não pode mudar a role
	        if (!authUser.getRole().equals("ADMIN") && !user.getId().equals(authUser.getId())) {
	            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
	        }

	        user.setName(userAtualizado.getName());
	        user.setEmail(userAtualizado.getEmail());

	        return ResponseEntity.ok(userRepository.save(user));
	    }
	

}

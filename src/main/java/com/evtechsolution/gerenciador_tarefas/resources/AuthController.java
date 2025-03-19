package com.evtechsolution.gerenciador_tarefas.resources;


import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.evtechsolution.gerenciador_tarefas.dtos.auth.LoginRequestDTO;
import com.evtechsolution.gerenciador_tarefas.dtos.auth.RegisterRequestDTO;
import com.evtechsolution.gerenciador_tarefas.dtos.auth.ResponseDTO;
import com.evtechsolution.gerenciador_tarefas.entities.User;
import com.evtechsolution.gerenciador_tarefas.infra.security.TokenService;
import com.evtechsolution.gerenciador_tarefas.repositories.UserRepository;



@RestController
@RequestMapping("/auth")
public class AuthController {
	
	@Autowired
    private  UserRepository repository;
	
	@Autowired
    private  PasswordEncoder passwordEncoder;
	
	@Autowired
	private  TokenService tokenService;
    

    @PostMapping("/login")
    public ResponseEntity login(@RequestBody LoginRequestDTO body){
        User user = this.repository.findByEmail(body.email()).orElseThrow(() -> new RuntimeException("User not found"));
        if(passwordEncoder.matches(body.password(), user.getPassword())) {
            String token = this.tokenService.generateToken(user);
            return ResponseEntity.ok(new ResponseDTO(user.getName(), token));
        }
        return ResponseEntity.badRequest().build();
    }


    @PostMapping("/register")
    public ResponseEntity register(@RequestBody RegisterRequestDTO body){
        Optional<User> user = this.repository.findByEmail(body.email());

        if(user.isEmpty()) {
            User newUser = new User();
            newUser.setName(body.name());
            newUser.setEmail(body.email());
            newUser.setPassword(passwordEncoder.encode(body.password()));
            newUser.setRole(body.role());
            
            
            this.repository.save(newUser);

            String token = this.tokenService.generateToken(newUser);
            return ResponseEntity.ok(new ResponseDTO(newUser.getName(), token));
        }
        return ResponseEntity.badRequest().build();
    }
}
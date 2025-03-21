package com.evtechsolution.gerenciador_tarefas.resources;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.evtechsolution.gerenciador_tarefas.dtos.auth.AuthenticationDTO;
import com.evtechsolution.gerenciador_tarefas.dtos.auth.RegisterDTO;
import com.evtechsolution.gerenciador_tarefas.dtos.auth.TokenDTO;
import com.evtechsolution.gerenciador_tarefas.entities.User;
import com.evtechsolution.gerenciador_tarefas.services.AuthService;



@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<TokenDTO> login(@RequestBody AuthenticationDTO data) {
        TokenDTO tokenDTO = authService.authenticate(data);
        return ResponseEntity.ok(tokenDTO);
    }

    @PostMapping("/register")
    public ResponseEntity<User> register(@RequestBody RegisterDTO data) {
        User user = authService.register(data);
        return ResponseEntity.ok(user);
    }
}
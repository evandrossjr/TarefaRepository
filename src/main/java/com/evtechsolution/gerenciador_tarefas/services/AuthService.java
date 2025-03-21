package com.evtechsolution.gerenciador_tarefas.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.evtechsolution.gerenciador_tarefas.dtos.auth.AuthenticationDTO;
import com.evtechsolution.gerenciador_tarefas.dtos.auth.RegisterDTO;
import com.evtechsolution.gerenciador_tarefas.dtos.auth.TokenDTO;
import com.evtechsolution.gerenciador_tarefas.entities.User;
import com.evtechsolution.gerenciador_tarefas.infra.security.TokenService;
import com.evtechsolution.gerenciador_tarefas.repositories.UserRepository;

@Service
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final TokenService tokenService;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public AuthService(AuthenticationManager authenticationManager, 
                      UserRepository userRepository, 
                      TokenService tokenService, 
                      PasswordEncoder passwordEncoder) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.tokenService = tokenService;
        this.passwordEncoder = passwordEncoder;
    }

    public TokenDTO authenticate(AuthenticationDTO data) {
        var usernamePassword = new UsernamePasswordAuthenticationToken(data.getUsername(), data.getPassword());
        Authentication auth = authenticationManager.authenticate(usernamePassword);
        
        User user = (User) auth.getPrincipal();
        String token = tokenService.generateToken(user);
        
        return new TokenDTO(token);
    }

    public User register(RegisterDTO data) {
        // Verifica se o username já existe
        if (userRepository.findByUsername(data.getUsername()) != null) {
            throw new RuntimeException("Username já existe");
        }
        
        // Verifica se o email já existe
        if (userRepository.findByEmail(data.getEmail()) != null) {
            throw new RuntimeException("Email já existe");
        }
        
        String encryptedPassword = passwordEncoder.encode(data.getPassword());
        User newUser = new User(
            data.getName(), 
            data.getUsername(), 
            data.getEmail(), 
            encryptedPassword, 
            data.getRole()
        );
        
        return (User) userRepository.save(newUser);
    }
}


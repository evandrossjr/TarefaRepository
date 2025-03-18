package com.evtechsolution.gerenciador_tarefas.services;
import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.evtechsolution.gerenciador_tarefas.entities.User;
import com.evtechsolution.gerenciador_tarefas.repositories.UserRepository;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public boolean autenticar(String username, String password) {
        // Busca o usuário no banco de dados pelo email
        Optional<User> userOptional = userRepository.findByEmail(username);

        // Verifica se o usuário existe e se a senha está correta
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            if (passwordEncoder.matches(password, user.getPassword())) {
                return true; // Autenticação bem-sucedida
            }
        }

        return false; // Autenticação falhou
    }
}
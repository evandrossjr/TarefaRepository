package com.evtechsolution.gerenciador_tarefas;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.evtechsolution.gerenciador_tarefas.entities.User;
import com.evtechsolution.gerenciador_tarefas.entities.enums.UserRole;
import com.evtechsolution.gerenciador_tarefas.repositories.UserRepository;

@Component
public class AdminSetup implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        // Verifica se já existe um admin
        if (userRepository.findByEmail("admin@example.com") == null) {
            User admin = new User();
            admin.setName("Administrador");
            admin.setUsername("admin");
            admin.setEmail("admin@example.com");
            admin.setPassword(passwordEncoder.encode("admin123"));
            
            // Corrigindo esta linha - usando o enum UserRole.ADMIN em vez de String
            admin.setRole(UserRole.ADMIN);
            
            userRepository.save(admin);
            System.out.println("Usuário admin criado com sucesso!");
        }
    }
}
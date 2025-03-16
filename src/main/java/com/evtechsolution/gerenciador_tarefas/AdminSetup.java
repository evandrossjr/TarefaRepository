package com.evtechsolution.gerenciador_tarefas;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.evtechsolution.gerenciador_tarefas.entities.User;
import com.evtechsolution.gerenciador_tarefas.repositories.UserRepository;

import jakarta.annotation.PostConstruct;

@Configuration
public class AdminSetup {

    private final UserRepository userRepository;

    public AdminSetup(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @PostConstruct
    public void setupAdminUser() {
        if (userRepository.findByEmail("admin@example.com") == null) {
            User admin = new User();
            admin.setName("admin");
            admin.setEmail("admin@admin.com");
            admin.setPassword(new BCryptPasswordEncoder().encode("adminpassword"));
            admin.setRole("ADMIN");

            userRepository.save(admin);
        }
    }
}

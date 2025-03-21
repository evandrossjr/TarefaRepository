package com.evtechsolution.gerenciador_tarefas.infra.security;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.evtechsolution.gerenciador_tarefas.entities.User;
import com.evtechsolution.gerenciador_tarefas.repositories.UserRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserDetails user = userRepository.findByEmail(username);
        if (user == null) {
            // Tenta buscar pelo username se não encontrar pelo email
            user = userRepository.findByUsername(username);
            if (user == null) {
                throw new UsernameNotFoundException("Usuário não encontrado");
            }
        }
        return user;
    }
}
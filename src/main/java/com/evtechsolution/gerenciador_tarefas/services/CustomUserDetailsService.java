package com.evtechsolution.gerenciador_tarefas.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.evtechsolution.gerenciador_tarefas.repositories.UserRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService {

	@Autowired
	private UserRepository userRepository;
	
	@Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByEmail(username)
                .map(user -> new org.springframework.security.core.userdetails.User(
                        user.getEmail(), // Nome de usuário (email)
                        user.getPassword(), // Senha (já codificada)
                        user.getAuthorities() // Lista de roles/autoridades
                ))
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado: " + username));
    }
	

}

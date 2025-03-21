package com.evtechsolution.gerenciador_tarefas.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;

import com.evtechsolution.gerenciador_tarefas.entities.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    UserDetails findByUsername(String username);
    
    // Adicione este método para resolver o erro
    UserDetails findByEmail(String email);
}
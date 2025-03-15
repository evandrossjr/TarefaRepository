package com.evtechsolution.gerenciador_tarefas.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.evtechsolution.gerenciador_tarefas.entities.User;

public interface UserRepository extends JpaRepository<User, Long>{

}

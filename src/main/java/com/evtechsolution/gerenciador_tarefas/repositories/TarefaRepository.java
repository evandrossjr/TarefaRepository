package com.evtechsolution.gerenciador_tarefas.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.evtechsolution.gerenciador_tarefas.entities.Tarefa;

@Repository
public interface TarefaRepository extends JpaRepository<Tarefa, Long>{
	List<Tarefa> findByUserId(Long userId);

}

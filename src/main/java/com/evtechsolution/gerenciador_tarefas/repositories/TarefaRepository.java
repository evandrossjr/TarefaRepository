package com.evtechsolution.gerenciador_tarefas.repositories;

import com.evtechsolution.gerenciador_tarefas.entities.Tarefa;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TarefaRepository extends JpaRepository<Tarefa, Long>{

}

package com.evtechsolution.gerenciador_tarefas.config;

import java.time.LocalDateTime;
import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import com.evtechsolution.gerenciador_tarefas.entities.Tarefa;
import com.evtechsolution.gerenciador_tarefas.entities.enums.Status;
import com.evtechsolution.gerenciador_tarefas.repositories.TarefaRepository;


@Configuration
@Profile("test")
public class DataLoader implements CommandLineRunner{
	
	@Autowired
	private TarefaRepository tarefaRepository;
	
	
	@Override
	public void run(String... args) throws Exception{
		//Tarefa t1 = new Tarefa(null, "Estudar Spring Boot", Status.PENDENTE, LocalDateTime.now());
        //Tarefa t2 = new Tarefa(null, "Criar CRUD de tarefas", Status.EM_ANDAMENTO, LocalDateTime.now());
        //Tarefa t3 = new Tarefa(null, "Testar API", Status.CONCLUIDO, LocalDateTime.now());
        
        //tarefaRepository.saveAll(Arrays.asList(t1, t2, t3));
        System.out.println("Dados iniciais carregados");
	
	}
}

package com.evtechsolution.gerenciador_tarefas.services;




import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import com.evtechsolution.gerenciador_tarefas.entities.Tarefa;
import com.evtechsolution.gerenciador_tarefas.repositories.TarefaRepository;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class TarefaServiceTest {

    @InjectMocks
    private TarefaService tarefaService;

    @Mock
    private TarefaRepository tarefaRepository;

    @Test
    public void testCriarTarefa() {
        Tarefa tarefa = new Tarefa("Nova Tarefa", "Descrição", null);
        Mockito.when(tarefaRepository.save(Mockito.any(Tarefa.class))).thenReturn(tarefa);

        Tarefa resultado = tarefaService.save(tarefa);

        Assertions.assertNotNull(resultado);
        Assertions.assertEquals("Nova Tarefa", resultado.getTitulo());
    }
}


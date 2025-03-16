package com.evtechsolution.gerenciador_tarefas.dtos;

import java.time.LocalDateTime;

import com.evtechsolution.gerenciador_tarefas.entities.enums.Status;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record TarefaDTO(Long Id, @NotBlank String titulo, String descricao, Status status, LocalDateTime dataCriacao, @NotNull Long userId) {
	

}

package com.evtechsolution.gerenciador_tarefas.dtos;

import java.util.Collections;
import java.util.List;

import jakarta.validation.constraints.NotBlank;

public record UserDTO(Long id, @NotBlank String name, @NotBlank String email, @NotBlank String password, List<TarefaDTO> tarefas){

	public UserDTO(Long id, String name, String email, String password) {
        this(id, name, email, password, Collections.emptyList());
	}
}

package com.evtechsolution.gerenciador_tarefas.dtos;

import java.util.Collections;
import java.util.List;

import jakarta.validation.constraints.NotBlank;

public record UserDTO(Long id, @NotBlank String name, @NotBlank String email, @NotBlank String password, @NotBlank String role, List<TarefaDTO> tarefas){

	public UserDTO(Long id, String name, String email, String password, String role) {
        this(id, name, email, password, role, Collections.emptyList());
	}
}

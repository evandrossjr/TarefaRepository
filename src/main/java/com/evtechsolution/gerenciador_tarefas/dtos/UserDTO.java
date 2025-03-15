package com.evtechsolution.gerenciador_tarefas.dtos;

import jakarta.validation.constraints.NotBlank;

public record UserDTO(Long id, @NotBlank String name, @NotBlank String email, @NotBlank String password){

}

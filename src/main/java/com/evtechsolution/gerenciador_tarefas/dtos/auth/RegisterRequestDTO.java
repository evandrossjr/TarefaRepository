package com.evtechsolution.gerenciador_tarefas.dtos.auth;

public record RegisterRequestDTO (String name, String email, String password, String role) {
}
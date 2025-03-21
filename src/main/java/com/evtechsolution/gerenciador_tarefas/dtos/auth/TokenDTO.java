package com.evtechsolution.gerenciador_tarefas.dtos.auth;

public class TokenDTO {
    private String token;
    
    // Construtores
    public TokenDTO() {
    }
    
    public TokenDTO(String token) {
        this.token = token;
    }
    
    // Getters e Setters
    public String getToken() {
        return token;
    }
    
    public void setToken(String token) {
        this.token = token;
    }
}

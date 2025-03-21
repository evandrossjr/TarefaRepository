package com.evtechsolution.gerenciador_tarefas.dtos.auth;

public class AuthenticationDTO {
    private String username;
    private String password;
    
    // Construtores
    public AuthenticationDTO() {
    }
    
    public AuthenticationDTO(String username, String password) {
        this.username = username;
        this.password = password;
    }
    
    // Getters e Setters
    public String getUsername() {
        return username;
    }
    
    public void setUsername(String username) {
        this.username = username;
    }
    
    public String getPassword() {
        return password;
    }
    
    public void setPassword(String password) {
        this.password = password;
    }
}
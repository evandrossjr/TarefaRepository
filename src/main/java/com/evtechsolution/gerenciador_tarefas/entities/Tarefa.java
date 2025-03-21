package com.evtechsolution.gerenciador_tarefas.entities;

import java.time.LocalDateTime;

import com.evtechsolution.gerenciador_tarefas.entities.enums.StatusTarefa;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;


@Entity
public class Tarefa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String titulo;
    
    private String descricao;
    
    private LocalDateTime dataCriacao;
    
    private LocalDateTime dataConclusao;
    
    @Enumerated(EnumType.STRING)
    private StatusTarefa status;
    
    
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    
    public Tarefa() {
        this.dataCriacao = LocalDateTime.now();
        this.status = StatusTarefa.PENDENTE;
    }
    
    // Getters e Setters
    
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getTitulo() {
        return titulo;
    }
    
    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }
    
    public String getDescricao() {
        return descricao;
    }
    
    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }
    
    public LocalDateTime getDataCriacao() {
        return dataCriacao;
    }
    
    public void setDataCriacao(LocalDateTime dataCriacao) {
        this.dataCriacao = dataCriacao;
    }
    
    public LocalDateTime getDataConclusao() {
        return dataConclusao;
    }
    
    public void setDataConclusao(LocalDateTime dataConclusao) {
        this.dataConclusao = dataConclusao;
    }
    
    public StatusTarefa getStatus() {
        return status;
    }
    
    public void setStatus(StatusTarefa status) {
        this.status = status;
    }
    
    public User getUser() {
        return user;
    }
    
    public void setUser(User user) {
        this.user = user;
    }
}
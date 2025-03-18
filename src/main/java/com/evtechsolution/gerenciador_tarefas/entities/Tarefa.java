package com.evtechsolution.gerenciador_tarefas.entities;

import java.time.LocalDateTime;
import java.util.Objects;

import com.evtechsolution.gerenciador_tarefas.entities.enums.Status;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;


@Entity
public class Tarefa {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@NotBlank(message ="Coloque um título")
	private String titulo;
	
	private String descricao;
	
	private Status status;
	
	@NotNull
	private LocalDateTime dataCriacao;
	
	
	@ManyToOne
	@JoinColumn(name = "user_id", nullable = false)
	private User user;

	public Tarefa() {
		
	}

	public Tarefa(Long id, String titulo, Status status, LocalDateTime dataCriacao, User user) {
		super();
		this.id = id;
		this.titulo = titulo;
		this.status = status;
		this.dataCriacao = dataCriacao;
		this.user= user;
	}


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

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public LocalDateTime getDataCriacao() {
		return dataCriacao;
	}

	public void setDataCriacao(LocalDateTime dataCriacao) {
		this.dataCriacao = dataCriacao;
	}
	
	

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Tarefa other = (Tarefa) obj;
		return Objects.equals(id, other.id);
	}
	

}

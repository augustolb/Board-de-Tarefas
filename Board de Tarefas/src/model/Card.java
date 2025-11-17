package model;

import java.time.LocalDateTime;

public class Card {

    // Atributos relacionados ao DAO
    private int id;
    private int idColunaAtual;

    // Atributos principais do card
    private String titulo;
    private String descricao;
    private LocalDateTime dataCriacao;

    // Atributos de Status do model.Card
    private boolean bloqueado;
    private String motivoBloqueio;
    private String motivoDesbloqueio;

    public Card(String titulo, String descricao) {
        this.titulo = titulo;
        this.descricao = descricao;
        this.dataCriacao = LocalDateTime.now();
        this.bloqueado = false;
    }

    public boolean bloquear (String motivo) {
        if (this.bloqueado) {
            return false;
        }
        this.bloqueado = true;
        this.motivoBloqueio = motivo;
        this.motivoDesbloqueio = null;
        return true;
    }

    public boolean desbloquear (String motivo) {
        if (!this.bloqueado) {
            return false;
        }
        this.bloqueado = false;
        this.motivoDesbloqueio = motivo;
        this.motivoBloqueio = null;
        return true;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdColunaAtual() {
        return idColunaAtual;
    }

    public void setIdColunaAtual(int idColunaAtual) {
        this.idColunaAtual = idColunaAtual;
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

    public boolean isBloqueado() {
        return bloqueado;
    }

    public String getMotivoBloqueio() {
        return motivoBloqueio;
    }

    public void setMotivoBloqueio(String motivoBloqueio) {
        this.motivoBloqueio = motivoBloqueio;
    }

    public String getMotivoDesbloqueio() {
        return motivoDesbloqueio;
    }

    public void setMotivoDesbloqueio(String motivoDesbloqueio) {
        this.motivoDesbloqueio = motivoDesbloqueio;
    }


}

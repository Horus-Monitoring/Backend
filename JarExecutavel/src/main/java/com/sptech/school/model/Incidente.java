package com.sptech.school.model;

import java.time.LocalDateTime;

public class Incidente {

    private Integer idRegistroAlerta;
    private String chave;
    private String titulo;
    private String statusAlerta;
    private String criticidade;
    private LocalDateTime dataAlerta;
    private LocalDateTime dataResolucao;
    private String servidor;
    private String componente;
    private Integer fkServidor;
    private Integer fkComponente;

    // Mantido apenas em memória para integração com Jira e Slack (não vai para o BD)
    private String jiraKey;

    public Incidente() {
    }

    public Incidente(Integer idRegistroAlerta, String chave, String titulo, String statusAlerta, String criticidade, LocalDateTime dataAlerta, LocalDateTime dataResolucao, String servidor, String componente, String jiraKey) {
        this.idRegistroAlerta = idRegistroAlerta;
        this.chave = chave;
        this.titulo = titulo;
        this.statusAlerta = statusAlerta;
        this.criticidade = criticidade;
        this.dataAlerta = dataAlerta;
        this.dataResolucao = dataResolucao;
        this.servidor = servidor;
        this.componente = componente;
        this.jiraKey = jiraKey;
    }

    public Integer getIdRegistroAlerta() {
        return idRegistroAlerta;
    }

    public void setIdRegistroAlerta(Integer idRegistroAlerta) {
        this.idRegistroAlerta = idRegistroAlerta;
    }

    public String getChave() {
        return chave;
    }

    public void setChave(String chave) {
        this.chave = chave;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getStatusAlerta() {
        return statusAlerta;
    }

    public void setStatusAlerta(String statusAlerta) {
        this.statusAlerta = statusAlerta;
    }

    public String getCriticidade() {
        return criticidade;
    }

    public void setCriticidade(String criticidade) {
        this.criticidade = criticidade;
    }

    public LocalDateTime getDataAlerta() {
        return dataAlerta;
    }

    public void setDataAlerta(LocalDateTime dataAlerta) {
        this.dataAlerta = dataAlerta;
    }

    public LocalDateTime getDataResolucao() {
        return dataResolucao;
    }

    public void setDataResolucao(LocalDateTime dataResolucao) {
        this.dataResolucao = dataResolucao;
    }

    public String getServidor() {
        return servidor;
    }

    public void setServidor(String servidor) {
        this.servidor = servidor;
    }

    public String getComponente() {
        return componente;
    }

    public void setComponente(String componente) {
        this.componente = componente;
    }

    public String getJiraKey() {
        return jiraKey;
    }

    public void setJiraKey(String jiraKey) {
        this.jiraKey = jiraKey;
    }

    public Integer getFkServidor() {
        return fkServidor;
    }

    public void setFkServidor(Integer fkServidor) {
        this.fkServidor = fkServidor;
    }

    public Integer getFkComponente() {
        return fkComponente;
    }

    public void setFkComponente(Integer fkComponente) {
        this.fkComponente = fkComponente;
    }
}
package com.sptech.school.service;

import java.time.LocalDateTime;

public class RelatorioData {
    // funcionário
    private String nome;
    private String cpf;
    private String funcao;
    private String email;

    // empresa
    private String razaoSocial;

    // servidor
    private String hostname;
    private String macAddress;
    private String statusServidor;

    // componente
    private String tipoComponente;
    private Double limite;
    private String unidadeMedida;

    // alerta
    private LocalDateTime dataAlerta;
    private String criticidade;
    private String statusAlerta;

    public RelatorioData() {
    }

    public RelatorioData(String nome,
                         String cpf,
                         String funcao,
                         String email,
                         String razaoSocial,
                         String hostname,
                         String macAddress,
                         String statusServidor,
                         String tipoComponente,
                         Double limite,
                         String unidadeMedida,
                         LocalDateTime dataAlerta,
                         String criticidade,
                         String statusAlerta) {
        this.nome = nome;
        this.cpf = cpf;
        this.funcao = funcao;
        this.email = email;
        this.razaoSocial = razaoSocial;
        this.hostname = hostname;
        this.macAddress = macAddress;
        this.statusServidor = statusServidor;
        this.tipoComponente = tipoComponente;
        this.limite = limite;
        this.unidadeMedida = unidadeMedida;
        this.dataAlerta = dataAlerta;
        this.criticidade = criticidade;
        this.statusAlerta = statusAlerta;
    }

    public String getNome() {
        return nome;
    }

    public String getCpf() {
        return cpf;
    }

    public String getFuncao() {
        return funcao;
    }

    public String getEmail() {
        return email;
    }

    public String getRazaoSocial() {
        return razaoSocial;
    }

    public String getHostname() {
        return hostname;
    }

    public String getMacAddress() {
        return macAddress;
    }

    public String getStatusServidor() {
        return statusServidor;
    }

    public String getTipoComponente() {
        return tipoComponente;
    }

    public Double getLimite() {
        return limite;
    }

    public String getUnidadeMedida() {
        return unidadeMedida;
    }

    public LocalDateTime getDataAlerta() {
        return dataAlerta;
    }

    public String getCriticidade() {
        return criticidade;
    }

    public String getStatusAlerta() {
        return statusAlerta;
    }

    @Override
    public String toString() {
        return "RelatorioData{" +
                "nome='" + nome + '\'' +
                ", cpf='" + cpf + '\'' +
                ", funcao='" + funcao + '\'' +
                ", email='" + email + '\'' +
                ", razaoSocial='" + razaoSocial + '\'' +
                ", hostname='" + hostname + '\'' +
                ", macAddress='" + macAddress + '\'' +
                ", statusServidor='" + statusServidor + '\'' +
                ", tipoComponente='" + tipoComponente + '\'' +
                ", limite=" + limite +
                ", unidadeMedida='" + unidadeMedida + '\'' +
                ", dataAlerta=" + dataAlerta +
                ", criticidade='" + criticidade + '\'' +
                ", statusAlerta='" + statusAlerta + '\'' +
                '}';
    }
}

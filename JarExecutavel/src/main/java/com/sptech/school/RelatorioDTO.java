package com.sptech.school.dto;

public class RelatorioDTO {

    private String usuario;
    private String email;
    private String mac_address;
    private String servidor;
    private Integer id_empresa;
    private String tipoComponente;


    public RelatorioDTO() {
    }

    public RelatorioDTO(String usuario, String email, String mac_address, String servidor, Integer id_empresa, String tipoComponente) {
        this.usuario = usuario;
        this.email = email;
        this.mac_address = mac_address;
        this.servidor = servidor;
        this.id_empresa = id_empresa;
        this.tipoComponente = tipoComponente;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMac_address() {
        return mac_address;
    }

    public void setMac_address(String mac_address) {
        this.mac_address = mac_address;
    }

    public String getServidor() {
        return servidor;
    }

    public void setServidor(String servidor) {
        this.servidor = servidor;
    }

    public Integer getId_empresa() {
        return id_empresa;
    }

    public void setId_empresa(Integer id_empresa) {
        this.id_empresa = id_empresa;
    }

    public String getTipoComponente() {
        return tipoComponente;
    }

    public void setTipoComponente(String tipoComponente) {
        this.tipoComponente = tipoComponente;
    }

    // getters e setters
}
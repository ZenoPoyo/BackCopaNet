package com.copanet.api.dtos;

public class ModificarUsuarioDTO {

    private Integer usuarioId;
    private String nombre;
    private String email;
    private String password; // puede venir vacío

    public Integer getUsuarioId() { return usuarioId; }
    public String getNombre() { return nombre; }
    public String getEmail() { return email; }
    public String getPassword() { return password; }
}

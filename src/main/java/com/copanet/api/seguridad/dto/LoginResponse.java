package com.copanet.api.seguridad.dto;

import java.util.List;

public class LoginResponse {
    private Integer usuarioId;
    private String email;
    private String nombre;
    private Integer rolId;
    private String token;


    public LoginResponse(Integer usuarioId, String email, String nombre, Integer rolId, String token) {
        this.usuarioId = usuarioId;
        this.email = email;
        this.nombre = nombre;
        this.rolId = rolId;
        this.token = token;
    }


    public Integer getUsuarioId() { return usuarioId; }
    public String getToken() { return token; }
}


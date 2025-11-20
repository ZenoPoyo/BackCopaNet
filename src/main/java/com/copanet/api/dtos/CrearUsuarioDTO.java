package com.copanet.api.dtos;

public class CrearUsuarioDTO {

    private String email;
    private String identificacion;
    private String rol;       // "Administrador" o "DTE"
    private String password;
    private String nombre;

    // === GETTERS & SETTERS ===
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getIdentificacion() { return identificacion; }
    public void setIdentificacion(String identificacion) { this.identificacion = identificacion; }

    public String getRol() { return rol; }
    public void setRol(String rol) { this.rol = rol; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
}

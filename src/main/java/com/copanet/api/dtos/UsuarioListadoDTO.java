package com.copanet.api.dtos;

public class UsuarioListadoDTO {

    private String identificacion;
    private String nombre;
    private String rol;
    private String estado;

    public UsuarioListadoDTO(String identificacion, String nombre, String rol, String estado) {
        this.identificacion = identificacion;
        this.nombre = nombre;
        this.rol = rol;
        this.estado = estado;
    }

    public String getIdentificacion() { return identificacion; }
    public String getNombre() { return nombre; }
    public String getRol() { return rol; }
    public String getEstado() { return estado; }
}


package com.copanet.api.dtos;

public class UsuarioDetalleDTO {

    private Integer usuarioId;
    private String identificacion;
    private String nombre;
    private String email;
    private String rol;

    public UsuarioDetalleDTO(Integer usuarioId, String identificacion, String nombre, String email, String rol) {
        this.usuarioId = usuarioId;
        this.identificacion = identificacion;
        this.nombre = nombre;
        this.email = email;
        this.rol = rol;
    }

    public Integer getUsuarioId() { return usuarioId; }
    public String getIdentificacion() { return identificacion; }
    public String getNombre() { return nombre; }
    public String getEmail() { return email; }
    public String getRol() { return rol; }
}

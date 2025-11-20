package com.copanet.api.dtos;

public class BitacoraDTO {

    private String fecha;
    private String usuario;
    private String accion;
    private String entidad;
    private String detalle;

    public BitacoraDTO(String fecha, String usuario, String accion, String entidad, String detalle) {
        this.fecha = fecha;
        this.usuario = usuario;
        this.accion = accion;
        this.entidad = entidad;
        this.detalle = detalle;
    }

    public String getFecha() { return fecha; }
    public String getUsuario() { return usuario; }
    public String getAccion() { return accion; }
    public String getEntidad() { return entidad; }
    public String getDetalle() { return detalle; }
}

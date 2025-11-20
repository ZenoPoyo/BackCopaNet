package com.copanet.api.dtos;

public class JugadorPlantillaDto {

    private Integer id;
    private String nombre;
    private String identificacion;
    private String posicion;

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getIdentificacion() { return identificacion; }
    public void setIdentificacion(String identificacion) { this.identificacion = identificacion; }

    public String getPosicion() { return posicion; }
    public void setPosicion(String posicion) { this.posicion = posicion; }
}

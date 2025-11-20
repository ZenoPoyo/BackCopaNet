package com.copanet.api.model;

import jakarta.persistence.*;

@Entity
@Table(name = "Jugador", schema = "dbo")
public class Jugador {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "JugadorId")
    private Integer id;

    @Column(name = "Nombre", nullable = false)
    private String nombre;

    @Column(name = "Identificacion", nullable = false)
    private String identificacion;

    @Column(name = "Posicion", nullable = false)
    private String posicion;

    @Column(name = "Estado", nullable = false)
    private String estado;

    // otros campos que tengas en la tabla se pueden agregar luego

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getIdentificacion() {
        return identificacion;
    }

    public void setIdentificacion(String identificacion) {
        this.identificacion = identificacion;
    }

    public String getPosicion() {
        return posicion;
    }

    public void setPosicion(String posicion) {
        this.posicion = posicion;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
}

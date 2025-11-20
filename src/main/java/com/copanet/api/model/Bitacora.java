package com.copanet.api.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import com.copanet.api.seguridad.Usuario;

@Entity
@Table(name = "Bitacora")
public class Bitacora {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "BitacoraId")
    private Long bitacoraId;

    @Column(name = "Fecha")
    private LocalDateTime fecha;

    @Column(name = "UsuarioId")
    private Integer usuarioId;

    @Column(name = "Accion")
    private String accion;

    @Column(name = "Entidad")
    private String entidad;

    @Column(name = "Detalle")
    private String detalle;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "UsuarioId", insertable = false, updatable = false)
    private Usuario usuario;

    public Long getBitacoraId() {
        return bitacoraId;
    }

    public void setBitacoraId(Long bitacoraId) {
        this.bitacoraId = bitacoraId;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public void setFecha(LocalDateTime fecha) {
        this.fecha = fecha;
    }

    public Integer getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(Integer usuarioId) {
        this.usuarioId = usuarioId;
    }

    public String getAccion() {
        return accion;
    }

    public void setAccion(String accion) {
        this.accion = accion;
    }

    public String getEntidad() {
        return entidad;
    }

    public void setEntidad(String entidad) {
        this.entidad = entidad;
    }

    public String getDetalle() {
        return detalle;
    }

    public void setDetalle(String detalle) {
        this.detalle = detalle;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
}


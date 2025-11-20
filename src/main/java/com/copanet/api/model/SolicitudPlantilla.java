package com.copanet.api.model;

import com.copanet.api.seguridad.Usuario;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "SolicitudPlantilla", schema = "dbo")
public class SolicitudPlantilla {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "SolicitudId")
    private Integer solicitudId;

    @ManyToOne
    @JoinColumn(name = "EquipoId", nullable = false)
    private Equipo equipo;

    @ManyToOne
    @JoinColumn(name = "SolicitanteId", nullable = false)
    private Usuario solicitante;

    @ManyToOne
    @JoinColumn(name = "JugadorId")
    private Jugador jugador;

    @Column(name = "Accion", nullable = false)
    private String accion;

    @Column(name = "Estado", nullable = false)
    private String estado;

    @Column(name = "Comentario")
    private String comentario;

    @Column(name = "CreadoEn", nullable = false)
    private LocalDateTime creadoEn;

    @Column(name = "ResueltoEn")
    private LocalDateTime resueltoEn;

    // --------- GETTERS / SETTERS ---------

    public Integer getSolicitudId() {
        return solicitudId;
    }

    public void setSolicitudId(Integer solicitudId) {
        this.solicitudId = solicitudId;
    }

    public Equipo getEquipo() {
        return equipo;
    }

    public void setEquipo(Equipo equipo) {
        this.equipo = equipo;
    }

    public Usuario getSolicitante() {
        return solicitante;
    }

    public void setSolicitante(Usuario solicitante) {
        this.solicitante = solicitante;
    }

    public Jugador getJugador() {
        return jugador;
    }

    public void setJugador(Jugador jugador) {
        this.jugador = jugador;
    }

    public String getAccion() {
        return accion;
    }

    public void setAccion(String accion) {
        this.accion = accion;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    public LocalDateTime getCreadoEn() {
        return creadoEn;
    }

    public void setCreadoEn(LocalDateTime creadoEn) {
        this.creadoEn = creadoEn;
    }

    public LocalDateTime getResueltoEn() {
        return resueltoEn;
    }

    public void setResueltoEn(LocalDateTime resueltoEn) {
        this.resueltoEn = resueltoEn;
    }

    // --------- MÉTODOS DE AYUDA ---------

    /** Referencia tipo #000001 para el frontend */
    @Transient
    public String getReferencia() {
        if (solicitudId == null) return null;
        return String.format("#%06d", solicitudId);
    }

    /** Indica si ya fue aprobada/rechazada (tiene ResueltoEn) */
    @Transient
    public boolean isResuelta() {
        return resueltoEn != null;
    }
}

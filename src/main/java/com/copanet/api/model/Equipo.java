package com.copanet.api.model;

import com.copanet.api.seguridad.Usuario;
import jakarta.persistence.*;

@Entity
@Table(name = "Equipo", schema = "dbo")
public class Equipo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "EquipoId")
    private Integer id;

    @Column(name = "Nombre", nullable = false, unique = true)
    private String nombre;

    @ManyToOne
    @JoinColumn(name = "DteId", nullable = false)
    private Usuario dte; // el director técnico

    @Column(name = "Estado", nullable = false)
    private String estado;

    @Column(name = "CupoMaximo", nullable = false)
    private Integer cupoMaximo;

    @Column(name = "CreadoEn", nullable = false)
    private java.time.LocalDateTime creadoEn;

    // ===== GETTERS y SETTERS =====

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public Usuario getDte() { return dte; }
    public void setDte(Usuario dte) { this.dte = dte; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public Integer getCupoMaximo() { return cupoMaximo; }
    public void setCupoMaximo(Integer cupoMaximo) { this.cupoMaximo = cupoMaximo; }

    public java.time.LocalDateTime getCreadoEn() { return creadoEn; }
    public void setCreadoEn(java.time.LocalDateTime creadoEn) { this.creadoEn = creadoEn; }
}

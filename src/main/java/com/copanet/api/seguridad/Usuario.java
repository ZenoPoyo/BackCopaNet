package com.copanet.api.seguridad;

import jakarta.persistence.*;
import java.util.Set;

@Entity
@Table(name = "Usuario", schema = "dbo")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "UsuarioId")
    private Integer id;

    @Column(name = "Email", nullable = false, unique = true)
    private String email;

    @Column(name = "Nombre", nullable = false)
    private String nombre;

    @Column(name = "Estado", nullable = false)
    private String estado;

    @Column(name = "PasswordHash", nullable = false)
    private byte[] passwordHash; 

    // Relación muchos-a-muchos con Rol a través de UsuarioRol
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "UsuarioRol",
        joinColumns = @JoinColumn(name = "UsuarioId"),
        inverseJoinColumns = @JoinColumn(name = "RolId")
    )
    private Set<Rol> roles;

    // ===== GETTERS y SETTERS =====

    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }

    public String getNombre() {
        return nombre;
    }
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getEstado() {
        return estado;
    }
    public void setEstado(String estado) {
        this.estado = estado;
    }

    public byte[] getPasswordHash() { return passwordHash; }
    public void setPasswordHash(byte[] passwordHash) { this.passwordHash = passwordHash; }

    public Set<Rol> getRoles() {
        return roles;
    }
    public void setRoles(Set<Rol> roles) {
        this.roles = roles;
    }
}

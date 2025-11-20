package com.copanet.api.seguridad;

import jakarta.persistence.*;

@Entity
@Table(name = "Rol", schema = "dbo")
public class Rol {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "RolId")
    private Integer id;

    @Column(name = "Nombre", nullable = false)
    private String nombre;  // 'ADMIN' o 'DTE'

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
}

package com.copanet.api.seguridad;

import jakarta.persistence.*;
import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "UsuarioRecovery", schema = "dbo")
public class UsuarioRecovery {

    @Id
    @Column(name = "RecoveryId")
    private UUID id;                  // UNIQUEIDENTIFIER → UUID

    @Column(name = "UsuarioId", nullable = false)
    private Integer usuarioId;

    @Column(name = "Codigo", nullable = false)
    private String codigo;

    @Column(name = "ExpiraEn", nullable = false)
    private OffsetDateTime expiraEn;

    @Column(name = "Usado", nullable = false)
    private boolean usado;

    @Column(name = "CreadoEn", nullable = false)
    private OffsetDateTime creadoEn;

    // ===== GETTERS Y SETTERS =====

    public UUID getId() {
        return id;
    }
    public void setId(UUID id) {
        this.id = id;
    }

    public Integer getUsuarioId() {
        return usuarioId;
    }
    public void setUsuarioId(Integer usuarioId) {
        this.usuarioId = usuarioId;
    }

    public String getCodigo() {
        return codigo;
    }
    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public OffsetDateTime getExpiraEn() {
        return expiraEn;
    }
    public void setExpiraEn(OffsetDateTime expiraEn) {
        this.expiraEn = expiraEn;
    }

    public boolean isUsado() {
        return usado;
    }
    public void setUsado(boolean usado) {
        this.usado = usado;
    }

    public OffsetDateTime getCreadoEn() {
        return creadoEn;
    }
    public void setCreadoEn(OffsetDateTime creadoEn) {
        this.creadoEn = creadoEn;
    }
}

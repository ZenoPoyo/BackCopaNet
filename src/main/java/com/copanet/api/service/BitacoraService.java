package com.copanet.api.service;

import com.copanet.api.dtos.BitacoraDTO;
import com.copanet.api.model.Bitacora;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Service
public class BitacoraService {

    @Autowired
    private DataSource dataSource;

    // ============================
    // 1. NUEVO: Método simple para registrar
    // ============================
    public void registrarEvento(Integer usuarioId,
                                String accion,
                                String entidad,
                                String detalle) throws Exception {

        String sql = """
            INSERT INTO Bitacora (Fecha, UsuarioId, Accion, Entidad, Detalle)
            VALUES (SYSDATETIME(), ?, ?, ?, ?)
        """;

        try (Connection con = dataSource.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {

            if (usuarioId != null)
                stmt.setInt(1, usuarioId);
            else
                stmt.setNull(1, Types.INTEGER);

            stmt.setString(2, accion);
            stmt.setString(3, entidad);
            stmt.setString(4, detalle);

            stmt.executeUpdate();
        }
    }

    // ============================
    // 2. Listado original (sin cambios)
    // ============================
    public List<BitacoraDTO> listarBitacora() throws Exception {

        List<BitacoraDTO> lista = new ArrayList<>();

        String sql = """
            SELECT 
                b.Fecha,
                ISNULL(u.Nombre, 'Desconocido') AS Usuario,
                b.Accion,
                b.Entidad,
                b.Detalle
            FROM dbo.Bitacora b
            LEFT JOIN dbo.Usuario u ON u.UsuarioId = b.UsuarioId
            ORDER BY b.BitacoraId DESC
        """;

        try (Connection con = dataSource.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {

                String fecha = rs.getString("Fecha");
                String usuario = rs.getNString("Usuario");
                String accion = rs.getString("Accion");
                String entidad = rs.getNString("Entidad");
                String detalle = rs.getNString("Detalle");

                if (usuario == null) usuario = "Desconocido";

                lista.add(new BitacoraDTO(
                        fecha,
                        usuario,
                        accion,
                        entidad,
                        detalle
                ));
            }
        }

        return lista;
    }

    // ============================
    // 3. Método guardar (lo dejo por compatibilidad)
    // ============================
    public void guardar(Bitacora b) throws Exception {
        String sql = """
            INSERT INTO Bitacora (Fecha, UsuarioId, Accion, Entidad, Detalle)
            VALUES (SYSDATETIME(), ?, ?, ?, ?)
        """;

        try (Connection con = dataSource.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {

            if (b.getUsuarioId() != null)
                stmt.setInt(1, b.getUsuarioId());
            else
                stmt.setNull(1, Types.INTEGER);

            stmt.setString(2, b.getAccion());
            stmt.setString(3, b.getEntidad());
            stmt.setString(4, b.getDetalle());

            stmt.executeUpdate();
        }
    }
}

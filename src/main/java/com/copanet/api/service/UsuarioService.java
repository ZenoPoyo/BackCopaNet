package com.copanet.api.service;

import com.copanet.api.dtos.CrearUsuarioDTO;
import com.copanet.api.dtos.UsuarioListadoDTO;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import java.util.ArrayList;
import java.util.List;

@Service
public class UsuarioService {

    @Autowired
    private DataSource dataSource;

    // ===========================================
    //  CREAR USUARIO (usa stored procedure)
    // ===========================================
    public void crearUsuario(CrearUsuarioDTO dto, Integer auditorId) throws Exception {

        int rolId;

        switch (dto.getRol().trim().toLowerCase()) {
            case "administrador":
            case "admin":
                rolId = 1;
                break;

            case "dte":
                rolId = 2;
                break;

            default:
                throw new RuntimeException("Rol inválido");
        }

        try (Connection con = dataSource.getConnection();
             CallableStatement stmt = con.prepareCall("{CALL sp_CrearUsuario(?, ?, ?, ?, ?, ?)}")) {

            stmt.setString(1, dto.getEmail());
            stmt.setString(2, dto.getIdentificacion());
            stmt.setInt(3, rolId);
            stmt.setBytes(4, dto.getPassword().getBytes());  // HASH LUEGO
            stmt.setString(5, dto.getNombre());
            stmt.setInt(6, auditorId);

            stmt.execute();
        }
    }

    // ===========================================
    //  LISTAR USUARIOS (JOIN con la tabla Rol)
    // ===========================================
    public List<UsuarioListadoDTO> listarUsuarios() throws Exception {

        List<UsuarioListadoDTO> lista = new ArrayList<>();

        String sql = """
            SELECT 
                u.Identificacion,
                u.Nombre,
                r.Nombre AS RolNombre,
                u.Estado
            FROM dbo.Usuario u
            JOIN dbo.Rol r ON r.RolId = u.RolId
            ORDER BY u.UsuarioId
        """;

        try (Connection con = dataSource.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {

                String identificacion = rs.getString("Identificacion");
                String nombre = rs.getString("Nombre");
                String rol = rs.getString("RolNombre");
                String estado = rs.getString("Estado");

                lista.add(new UsuarioListadoDTO(
                        identificacion,
                        nombre,
                        rol,
                        estado
                ));
            }
        }

        System.out.println("Usuarios cargados desde SQL = " + lista.size());
            for (var u : lista) {
                System.out.println(u.getIdentificacion() + " - " + u.getNombre() + " - " + u.getRol());
            }

        return lista;
    }
}


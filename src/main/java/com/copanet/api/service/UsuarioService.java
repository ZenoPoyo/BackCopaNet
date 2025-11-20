package com.copanet.api.service;

import com.copanet.api.dtos.CrearUsuarioDTO;
import com.copanet.api.dtos.UsuarioListadoDTO;
import com.copanet.api.dtos.ModificarUsuarioDTO;
import com.copanet.api.dtos.UsuarioDetalleDTO;

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
                u.UsuarioId,
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

                int usuarioId = rs.getInt("UsuarioId");
                String identificacion = rs.getString("Identificacion");
                String nombre = rs.getString("Nombre");
                String rol = rs.getString("RolNombre");
                String estado = rs.getString("Estado");

                lista.add(new UsuarioListadoDTO(
                        usuarioId,
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


    public void eliminarUsuario(Integer usuarioId, Integer auditorId) throws Exception {

        try (Connection con = dataSource.getConnection();
            CallableStatement stmt = con.prepareCall("{CALL sp_EliminarUsuario(?, ?)}")) {

            stmt.setInt(1, usuarioId);
            stmt.setInt(2, auditorId);

            stmt.execute();
        }
    }

    public boolean validarPassword(Integer usuarioId, String password) throws Exception {

        String sql = """
            SELECT PasswordHash
            FROM Usuario
            WHERE UsuarioId = ?
        """;

        try (Connection con = dataSource.getConnection();
            PreparedStatement stmt = con.prepareStatement(sql)) {

            stmt.setInt(1, usuarioId);

            try (ResultSet rs = stmt.executeQuery()) {
                if (!rs.next()) return false;

                byte[] hashBD = rs.getBytes("PasswordHash");
                byte[] hashIngresado = password.getBytes(); // En el futuro se reemplaza por hashing

                return java.util.Arrays.equals(hashBD, hashIngresado);
            }
        }
    }

    public UsuarioDetalleDTO obtenerUsuarioPorIdentificacion(String identificacion) throws Exception {

        String sql = """
            SELECT 
                u.UsuarioId,
                u.Identificacion,
                u.Nombre,
                u.Email,
                r.Nombre AS RolNombre
            FROM Usuario u
            JOIN Rol r ON r.RolId = u.RolId
            WHERE u.Identificacion = ?
        """;

        try (Connection con = dataSource.getConnection();
            PreparedStatement stmt = con.prepareStatement(sql)) {

            stmt.setString(1, identificacion);

            try (ResultSet rs = stmt.executeQuery()) {
                if (!rs.next()) return null;

                return new UsuarioDetalleDTO(
                    rs.getInt("UsuarioId"),
                    rs.getString("Identificacion"),
                    rs.getString("Nombre"),
                    rs.getString("Email"),
                    rs.getString("RolNombre")
                );
            }
        }
    }

    public void modificarUsuario(ModificarUsuarioDTO dto, Integer auditorId) throws Exception {

        byte[] passwordBytes = null;

        // Solo actualizar contraseña si viene una nueva
        if (dto.getPassword() != null && !dto.getPassword().isBlank()) {
            passwordBytes = dto.getPassword().getBytes(); // Luego aplicar hash
        } else {
            // Obtener la contraseña actual
            String sql = "SELECT PasswordHash FROM Usuario WHERE UsuarioId = ?";
            try (Connection con = dataSource.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {

                ps.setInt(1, dto.getUsuarioId());

                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        passwordBytes = rs.getBytes("PasswordHash");
                    } else {
                        throw new RuntimeException("Usuario no encontrado.");
                    }
                }
            }
        }

        try (Connection con = dataSource.getConnection();
            CallableStatement stmt = con.prepareCall("{CALL sp_ModificarUsuario(?, ?, ?, ?, ?)}")) {

            stmt.setInt(1, dto.getUsuarioId());
            stmt.setString(2, dto.getEmail());
            stmt.setBytes(3, passwordBytes);
            stmt.setString(4, dto.getNombre());
            stmt.setInt(5, auditorId);

            stmt.execute();
        }
    }



}


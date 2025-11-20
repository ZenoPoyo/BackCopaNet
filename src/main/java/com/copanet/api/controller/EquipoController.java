package com.copanet.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import com.copanet.api.dtos.JugadorPlantillaDto;
import com.copanet.api.service.EquipoPlantillaService;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/equipos")
@CrossOrigin(origins = {"http://localhost:5173"})
public class EquipoController {

     private final EquipoPlantillaService equipoPlantillaService;

    public EquipoController(EquipoPlantillaService equipoPlantillaService) {
        this.equipoPlantillaService = equipoPlantillaService;
    }
    @Autowired
    private DataSource dataSource;

    @GetMapping("/listar")
    public ResponseEntity<List<Map<String, Object>>> listarEquipos() {

        String sql = """
            SELECT 
                e.EquipoId,
                e.Nombre AS Equipo,
                u.Nombre AS DTE,
                e.CupoMaximo,
                e.Estado
            FROM dbo.Equipo e
            JOIN dbo.Usuario u ON u.UsuarioId = e.DteId
            ORDER BY e.EquipoId DESC
        """;

        List<Map<String, Object>> lista = new ArrayList<>();

        try (Connection con = dataSource.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Map<String, Object> row = new HashMap<>();
                row.put("id", rs.getInt("EquipoId"));
                row.put("nombre", rs.getString("Equipo"));
                row.put("dte", rs.getString("DTE"));
                row.put("plantilla", rs.getInt("CupoMaximo"));
                row.put("estado", rs.getString("Estado"));
                lista.add(row);
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }

        return ResponseEntity.ok(lista);
    }
        @GetMapping("/{equipoId}/plantilla-activa")
    public List<JugadorPlantillaDto> getPlantillaActiva(@PathVariable Integer equipoId) {
        return equipoPlantillaService.obtenerPlantillaActiva(equipoId);
    }
}

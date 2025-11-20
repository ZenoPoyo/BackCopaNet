package com.copanet.api.controller;

import com.copanet.api.dtos.SolicitudPlantillaDto;
import com.copanet.api.service.SolicitudPlantillaService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/solicitudes-plantilla")
// @CrossOrigin(origins = "http://localhost:5173") // si lo necesitas para el frontend
public class SolicitudPlantillaController {

    private final SolicitudPlantillaService service;

    public SolicitudPlantillaController(SolicitudPlantillaService service) {
        this.service = service;
    }

    // 🔹 GET: lista SOLO solicitudes pendientes (ResueltoEn IS NULL)
    @GetMapping
    public List<SolicitudPlantillaDto> listarPendientes() {
        return service.listarPendientes();
    }

    // 🔹 PUT: aprobar una solicitud
    @PutMapping("/{id}/aprobar")
    public SolicitudPlantillaDto aprobar(@PathVariable Integer id) {
        return service.aprobar(id);
    }

    // 🔹 PUT: rechazar una solicitud
    @PutMapping("/{id}/rechazar")
    public SolicitudPlantillaDto rechazar(@PathVariable Integer id) {
        return service.rechazar(id);
    }
}

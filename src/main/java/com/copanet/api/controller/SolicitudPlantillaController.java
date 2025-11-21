package com.copanet.api.controller;

import com.copanet.api.dtos.SolicitudPlantillaDto;
import com.copanet.api.service.SolicitudPlantillaService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/solicitudes-plantilla")

public class SolicitudPlantillaController {

    private final SolicitudPlantillaService service;

    public SolicitudPlantillaController(SolicitudPlantillaService service) {
        this.service = service;
    }

    @GetMapping
    public List<SolicitudPlantillaDto> listarPendientes() {
        return service.listarPendientes();
    }

    @PutMapping("/{id}/aprobar")
    public SolicitudPlantillaDto aprobar(@PathVariable Integer id) {
        return service.aprobar(id);
    }

    @PutMapping("/{id}/rechazar")
    public SolicitudPlantillaDto rechazar(@PathVariable Integer id) {
        return service.rechazar(id);
    }
}

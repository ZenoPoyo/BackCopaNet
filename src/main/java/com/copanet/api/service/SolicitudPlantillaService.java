package com.copanet.api.service;

import com.copanet.api.SolicitudPlantillaRepository;
import com.copanet.api.dtos.SolicitudPlantillaDto;
import com.copanet.api.model.SolicitudPlantilla;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SolicitudPlantillaService {

    private final SolicitudPlantillaRepository repository;
    private final BitacoraService bitacoraService;

    public SolicitudPlantillaService(SolicitudPlantillaRepository repository,
                                     BitacoraService bitacoraService) {
        this.repository = repository;
        this.bitacoraService = bitacoraService;
    }

    // --------------------------------------------------------
    // 1. LISTAR SOLO SOLICITUDES PENDIENTES (ResueltoEn IS NULL)
    // --------------------------------------------------------
    public List<SolicitudPlantillaDto> listarPendientes() {
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        return repository.findByResueltoEnIsNull().stream().map(s -> {
            SolicitudPlantillaDto dto = new SolicitudPlantillaDto();

            dto.setId(s.getSolicitudId()); 

            dto.setReferencia("#" + String.format("%07d", s.getSolicitudId()));
            dto.setDte(s.getSolicitante().getNombre());
            dto.setAccion(s.getAccion());

            dto.setJugador(
                    s.getJugador() != null ? s.getJugador().getNombre() : "N/A"
            );

            dto.setFecha(
                    s.getCreadoEn() != null ? s.getCreadoEn().format(fmt) : ""
            );

            dto.setDetalle(s.getComentario());

            return dto;
        }).collect(Collectors.toList());
    }


// --------------------------------------------------------
// 2. MARCAR COMO APROBADA
// --------------------------------------------------------
public SolicitudPlantillaDto aprobar(Integer id) {
    SolicitudPlantilla sol = repository.findById(id)
            .orElseThrow(() -> new RuntimeException("Solicitud no encontrada"));

    sol.setEstado("APROBADA");
    sol.setResueltoEn(LocalDateTime.now());

    repository.save(sol);

    Integer usuarioId = null;
    if (sol.getSolicitante() != null) {
        usuarioId = sol.getSolicitante().getId(); // o getUsuarioId(), según tu entidad
    }

    try {
        bitacoraService.registrarEvento(
                usuarioId,
                "APROBAR",
                "SolicitudPlantilla",  // 👈 AQUI: SIEMPRE SolicitudPlantilla
                "SolicitudId=" + sol.getSolicitudId() + " | Acción=" + sol.getAccion()
        );
    } catch (Exception e) {
        e.printStackTrace();
    }

    return convertirDto(sol);
}

// --------------------------------------------------------
// 3. MARCAR COMO RECHAZADA
// --------------------------------------------------------
public SolicitudPlantillaDto rechazar(Integer id) {
    SolicitudPlantilla sol = repository.findById(id)
            .orElseThrow(() -> new RuntimeException("Solicitud no encontrada"));

    sol.setEstado("RECHAZADA");
    sol.setResueltoEn(LocalDateTime.now());

    repository.save(sol);

    Integer usuarioId = null;
    if (sol.getSolicitante() != null) {
        usuarioId = sol.getSolicitante().getId(); // o getUsuarioId()
    }

    try {
        bitacoraService.registrarEvento(
                usuarioId,
                "RECHAZAR",
                "SolicitudPlantilla",  // 👈 IGUAL AQUI
                "SolicitudId=" + sol.getSolicitudId() + " | Acción=" + sol.getAccion()
        );
    } catch (Exception e) {
        e.printStackTrace();
    }

    return convertirDto(sol);
}


    // --------------------------------------------------------
    // MÉTODO DE APOYO: convertir entidad → DTO
    // --------------------------------------------------------
    private SolicitudPlantillaDto convertirDto(SolicitudPlantilla s) {
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        SolicitudPlantillaDto dto = new SolicitudPlantillaDto();

        dto.setId(s.getSolicitudId());
        dto.setReferencia("#" + String.format("%07d", s.getSolicitudId()));
        dto.setDte(s.getSolicitante().getNombre());
        dto.setAccion(s.getAccion());
        dto.setJugador(
                s.getJugador() != null ? s.getJugador().getNombre() : "N/A"
        );
        dto.setFecha(
                s.getCreadoEn() != null ? s.getCreadoEn().format(fmt) : ""
        );
        dto.setDetalle(s.getComentario());

        return dto;
    }
}

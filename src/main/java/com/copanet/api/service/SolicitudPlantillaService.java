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

        sol.setEstado("Aprobar");
        sol.setResueltoEn(LocalDateTime.now());

        repository.save(sol);

        // ---- Registrar en bitácora ----
        Integer usuarioId = null;
        if (sol.getSolicitante() != null) {

            usuarioId = sol.getSolicitante().getId();
        }

        try {
            bitacoraService.registrarEvento(
                    usuarioId,
                    "APROBAR",
                    "Equipo",
                    "SolicitudId=" + sol.getSolicitudId() + " | Acción=" + sol.getAccion()
            );
        } catch (Exception e) {
            // No queremos romper la aprobación si falla la bitácora
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

        sol.setEstado("Rechazar");
        sol.setResueltoEn(LocalDateTime.now());

        repository.save(sol);

        // ---- Registrar en bitácora ----
        Integer usuarioId = null;
        if (sol.getSolicitante() != null) {
            // Igual que arriba: usa el getter correcto
            usuarioId = sol.getSolicitante().getId();
        }

        try {
            bitacoraService.registrarEvento(
                    usuarioId,
                    "RECHAZAR",
                    "Equipo",
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

package com.copanet.api.service;

import com.copanet.api.JugadorRepository;
import com.copanet.api.dtos.JugadorPlantillaDto;
import com.copanet.api.model.Jugador;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class EquipoPlantillaService {

    private final JugadorRepository jugadorRepository;

    public EquipoPlantillaService(JugadorRepository jugadorRepository) {
        this.jugadorRepository = jugadorRepository;
    }

    public List<JugadorPlantillaDto> obtenerPlantillaActiva(Integer equipoId) {
        List<Jugador> jugadores = jugadorRepository.findPlantillaActivaByEquipoId(equipoId);

        return jugadores.stream().map(j -> {
            JugadorPlantillaDto dto = new JugadorPlantillaDto();
            dto.setId(j.getId());
            dto.setNombre(j.getNombre());
            dto.setIdentificacion(j.getIdentificacion());
            dto.setPosicion(j.getPosicion());
            dto.setCamiseta(j.getCamiseta());   
            return dto;
        }).collect(Collectors.toList());
    }
}

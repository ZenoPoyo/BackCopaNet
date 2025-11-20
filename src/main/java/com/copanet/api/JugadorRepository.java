package com.copanet.api;

import com.copanet.api.model.Jugador;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface JugadorRepository extends JpaRepository<Jugador, Integer> {

    @Query(value = """
        SELECT j.* 
        FROM Jugador j
        JOIN EquipoJugador ej ON j.JugadorId = ej.JugadorId
        WHERE ej.EquipoId = :equipoId
          AND ej.FechaBaja IS NULL
    """, nativeQuery = true)
    List<Jugador> findPlantillaActivaByEquipoId(@Param("equipoId") Integer equipoId);
}

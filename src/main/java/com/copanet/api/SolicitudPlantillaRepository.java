package com.copanet.api;

import com.copanet.api.model.SolicitudPlantilla;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
public interface SolicitudPlantillaRepository extends JpaRepository<SolicitudPlantilla, Integer> {

    // <- ESTE ES EL QUE FALTA
    List<SolicitudPlantilla> findByResueltoEnIsNull();
}


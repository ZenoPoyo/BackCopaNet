package com.copanet.api;

import com.copanet.api.model.Bitacora;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BitacoraRepository extends JpaRepository<Bitacora, Long> {
}

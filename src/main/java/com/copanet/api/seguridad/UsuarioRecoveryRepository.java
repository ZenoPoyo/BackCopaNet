package com.copanet.api.seguridad;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;
import java.util.Optional;

public interface UsuarioRecoveryRepository
        extends JpaRepository<UsuarioRecovery, UUID> {

    Optional<UsuarioRecovery> findTopByUsuarioIdAndCodigoAndUsadoIsFalseOrderByCreadoEnDesc(
            Integer usuarioId,
            String codigo
    );
}

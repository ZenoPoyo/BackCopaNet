package com.copanet.api.seguridad;

import com.copanet.config.JwtUtil;

import org.springframework.beans.factory.annotation.Autowired;

import com.copanet.api.email.EmailService;
import com.copanet.api.seguridad.dto.LoginRequest;
import com.copanet.api.seguridad.dto.LoginResponse;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.Random;
import java.util.UUID;
import java.nio.charset.StandardCharsets;

@Service
public class AuthService {

    private final UsuarioRepository usuarioRepository;
    private final UsuarioRecoveryRepository recoveryRepository;
    private final JwtUtil jwtUtil;

    @Autowired
    private EmailService emailService;      // <-- (2) Inyección del servicio de email

    @Autowired
    public AuthService(UsuarioRepository usuarioRepository,
                       UsuarioRecoveryRepository recoveryRepository,
                       JwtUtil jwtUtil) {

        this.usuarioRepository = usuarioRepository;
        this.recoveryRepository = recoveryRepository;
        this.jwtUtil = jwtUtil;
    }

    // ====== LOGIN SOLO ADMIN ======
    public LoginResponse login(LoginRequest request) {

        var usuario = usuarioRepository.findByEmailIgnoreCase(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Usuario o contraseña incorrectos"));

        // ===== VALIDAR CONTRASEÑA =====
        if (!new String(usuario.getPasswordHash()).equals(request.getPassword())) {
            throw new RuntimeException("Usuario o contraseña incorrectos");
        }

        // ===== VALIDAR QUE SEA ADMIN =====
        boolean esAdmin = usuario.getRoles().stream()
                .anyMatch(r -> r.getId() == 1);  // RolId = 1 = ADMIN

        if (!esAdmin) {
            throw new RuntimeException("Acceso denegado: solo administradores pueden iniciar sesión.");
        }

        // ===== GENERAR TOKEN CON ROL =====
        String token = jwtUtil.generarToken(
                usuario.getId(),
                usuario.getEmail(),
                1   // si llegó aquí, es admin
        );

        // ===== RESPUESTA =====
        return new LoginResponse(
                usuario.getId(),
                usuario.getEmail(),
                usuario.getNombre(),
                1,         // rolId = 1
                token
        );
    }

    // ====== INICIAR RECUPERACIÓN (PASO 1) ======
    public void iniciarRecovery(String email) {

        var usuario = usuarioRepository.findByEmailIgnoreCase(email)
                .orElseThrow(() -> new RuntimeException("No existe un usuario con ese correo"));

        // código de 4 dígitos
        String codigo = String.format("%04d", new Random().nextInt(10_000));

        UsuarioRecovery rec = new UsuarioRecovery();
        rec.setId(UUID.randomUUID());               // GUID
        rec.setUsuarioId(usuario.getId());
        rec.setCodigo(codigo);
        rec.setUsado(false);
        rec.setCreadoEn(OffsetDateTime.now());
        rec.setExpiraEn(OffsetDateTime.now().plusMinutes(10));

        recoveryRepository.save(rec);

        // ====== (3) ENVIAR EL CÓDIGO AL CORREO ======
        emailService.enviarCodigo(usuario.getEmail(), codigo);

        // Opcional: seguir mostrando en consola
        System.out.println("Código de recuperación para " + email + ": " + codigo);
    }

    // ====== VALIDAR CÓDIGO (PASO 2) ======
    public void validarCodigo(String email, String codigo) {

        var usuario = usuarioRepository.findByEmailIgnoreCase(email)
                .orElseThrow(() -> new RuntimeException("No existe un usuario con ese correo"));

        var rec = recoveryRepository
                .findTopByUsuarioIdAndCodigoAndUsadoIsFalseOrderByCreadoEnDesc(
                        usuario.getId(), codigo)
                .orElseThrow(() -> new RuntimeException("Código inválido"));

        if (OffsetDateTime.now().isAfter(rec.getExpiraEn())) {
            throw new RuntimeException("El código ha expirado");
        }
    }

    // ====== RESET DE CONTRASEÑA (PASO 3) ======
    public void resetPassword(String email, String codigo, String nuevaPassword) {

        var usuario = usuarioRepository.findByEmailIgnoreCase(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        var rec = recoveryRepository
                .findTopByUsuarioIdAndCodigoAndUsadoIsFalseOrderByCreadoEnDesc(
                        usuario.getId(), codigo)
                .orElseThrow(() -> new RuntimeException("Código inválido"));

        if (OffsetDateTime.now().isAfter(rec.getExpiraEn())) {
            throw new RuntimeException("El código ha expirado");
        }

        // Marcar código como usado
        rec.setUsado(true);
        recoveryRepository.save(rec);

        // Guardar nueva contraseña
        usuario.setPasswordHash(nuevaPassword.getBytes(StandardCharsets.UTF_8));
        usuarioRepository.save(usuario);
    }
}

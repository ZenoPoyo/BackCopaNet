package com.copanet.api.seguridad;

import jakarta.servlet.http.HttpServletRequest;

import com.copanet.api.seguridad.dto.LoginRequest;
import com.copanet.api.seguridad.dto.LoginResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.copanet.api.seguridad.dto.RecoveryCodeRequest;
import com.copanet.api.seguridad.dto.RecoveryResetRequest;


@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:5173") 
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
public ResponseEntity<?> login(
        @RequestBody LoginRequest request,
        HttpServletRequest http
) {
    try {
        LoginResponse response = authService.login(request);

        System.out.println(">>> LOGIN LLEGÓ AL BACKEND");
System.out.println("UsuarioId devuelto por login: " + response.getUsuarioId());


        // 🔥 Guardar UsuarioId para el interceptor
        http.setAttribute("UsuarioId", response.getUsuarioId());

        return ResponseEntity.ok(response);

    } catch (RuntimeException ex) {
        return ResponseEntity.status(401).body(ex.getMessage());
    }
}



        @PostMapping("/recovery/email")
    public ResponseEntity<?> iniciarRecovery(@RequestBody LoginRequest body) {
        try {
            authService.iniciarRecovery(body.getEmail());
            return ResponseEntity.ok("Si el correo existe, se ha generado un código de recuperación.");
        } catch (RuntimeException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }
        @PostMapping("/recovery/code")
    public ResponseEntity<?> validarCodigo(@RequestBody RecoveryCodeRequest body) {
        try {
            authService.validarCodigo(body.getEmail(), body.getCode());
            return ResponseEntity.ok("Código válido");
        } catch (RuntimeException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

 @PostMapping("/recovery/reset")
public ResponseEntity<?> resetPassword(@RequestBody RecoveryResetRequest body) {
    try {
        authService.resetPassword(body.getEmail(), body.getCode(), body.getNewPassword());
        return ResponseEntity.ok("Contraseña actualizada correctamente");
    } catch (RuntimeException ex) {
        return ResponseEntity.badRequest().body(ex.getMessage());
    }
}


}

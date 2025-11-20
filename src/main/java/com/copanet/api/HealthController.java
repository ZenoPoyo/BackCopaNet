package com.copanet.api;
import jakarta.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthController {

    @GetMapping("/api/health")
    public String health() {
        return "OK";
    }


    @GetMapping("/test-session")
    public String testSession(HttpServletRequest request) throws Exception {
        Integer usuarioId = 99; // un número cualquiera
        request.setAttribute("UsuarioId", usuarioId);
        return "OK";
    }
}


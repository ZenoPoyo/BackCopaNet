package com.copanet.config;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtUtil {

    private final Key key;

    // Constructor que carga la llave desde application.properties
    public JwtUtil(@Value("${jwt.secret}") String secret) {
        this.key = Keys.hmacShaKeyFor(secret.getBytes());
    }

    // Crear token con usuarioId + email + rolId
    public String generarToken(Integer usuarioId, String email, Integer rolId) {
        return Jwts.builder()
                .setSubject(email)
                .claim("usuarioId", usuarioId)
                .claim("rolId", rolId)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 86400000)) // 1 día
                .signWith(key)
                .compact();
    }

    // Extraer usuarioId
    public Integer obtenerUsuarioId(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();

        return claims.get("usuarioId", Integer.class);
    }

    // Extraer rolId (opcional)
    public Integer obtenerRolId(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();

        return claims.get("rolId", Integer.class);
    }
}

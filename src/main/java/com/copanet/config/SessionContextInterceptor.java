package com.copanet.config;

import com.copanet.config.JwtUtil;


import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;

@Component
public class SessionContextInterceptor implements HandlerInterceptor {

    private final JwtUtil jwtUtil;

    public SessionContextInterceptor(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Autowired
    private DataSource dataSource;

    @Override
    public boolean preHandle(HttpServletRequest request,
                            HttpServletResponse response,
                            Object handler) throws Exception {

        System.out.println("Interceptor ejecutándose…");

        String authHeader = request.getHeader("Authorization");

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);

            try {
                Integer usuarioId = jwtUtil.obtenerUsuarioId(token);

                request.setAttribute("usuarioId", usuarioId);
                
                if (usuarioId != null) {
                    try (Connection con = dataSource.getConnection();
                        PreparedStatement stmt = con.prepareStatement(
                                "EXEC sp_set_session_context 'UsuarioId', ?"
                        )) {

                        stmt.setInt(1, usuarioId);
                        stmt.execute();
                        System.out.println("Interceptor ejecutó con usuarioId=" + usuarioId);
                    }
                }

            } catch (Exception ex) {
                System.out.println("Token inválido o expirado: " + ex.getMessage());
            }
        }

        return true;
    }
}



package com.copanet.api.controller;

import com.copanet.api.dtos.CrearUsuarioDTO;
import com.copanet.api.dtos.UsuarioListadoDTO;
import com.copanet.api.dtos.ModificarUsuarioDTO;
import com.copanet.api.dtos.UsuarioDetalleDTO;
import com.copanet.api.service.UsuarioService;
import com.copanet.api.service.BitacoraService;
import com.copanet.api.BitacoraRepository;

import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;

import com.copanet.api.seguridad.Usuario;
import com.copanet.api.model.Bitacora;

import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;


import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private BitacoraService bitacoraService;
    
    @Autowired
    private BitacoraRepository bitacoraRepository;



    @PostMapping("/crear")
    public ResponseEntity<?> crearUsuario(
            @RequestBody CrearUsuarioDTO dto,
            @RequestAttribute("usuarioId") Integer auditorId) {

        try {
            usuarioService.crearUsuario(dto, auditorId);
            return ResponseEntity.ok("Usuario creado exitosamente.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/listar")
    public ResponseEntity<List<UsuarioListadoDTO>> listarUsuarios() {
        try {
            List<UsuarioListadoDTO> usuarios = usuarioService.listarUsuarios();
            return ResponseEntity.ok(usuarios);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<?> eliminarUsuario(
            @PathVariable Integer id,
            @RequestAttribute("usuarioId") Integer auditorId) {

        try {
            usuarioService.eliminarUsuario(id, auditorId);
            return ResponseEntity.ok("Usuario eliminado correctamente.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/validar-password")
    public ResponseEntity<?> validarPassword(
            @RequestBody Map<String, String> body,
            @RequestAttribute("usuarioId") Integer usuarioId) {

        try {
            String password = body.get("password");
            boolean ok = usuarioService.validarPassword(usuarioId, password);

            if (!ok)
                return ResponseEntity.status(401).body("Contraseña incorrecta.");

            return ResponseEntity.ok("OK");

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/detalle/{identificacion}")
    public ResponseEntity<?> obtenerDetalle(
            @PathVariable String identificacion) {

        try {
            var dto = usuarioService.obtenerUsuarioPorIdentificacion(identificacion);

            if (dto == null)
                return ResponseEntity.badRequest().body("Usuario no existe");

            return ResponseEntity.ok(dto);

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/modificar")
    public ResponseEntity<?> modificarUsuario(
            @RequestBody ModificarUsuarioDTO dto,
            @RequestAttribute("usuarioId") Integer auditorId) {

        try {
            usuarioService.modificarUsuario(dto, auditorId);
            return ResponseEntity.ok("Usuario modificado exitosamente.");

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    
    @PutMapping("/solicitud/{usuarioId}")
    public ResponseEntity<?> procesarSolicitud(
            @PathVariable int usuarioId,
            @RequestParam String estado,
            HttpServletRequest request) {

        try {
            // Obtener ID del admin desde tu token (AJUSTA ESTO según tu proyecto)
            Integer adminId = (Integer) request.getAttribute("usuarioId");

            if (adminId == null)
                adminId = 0;

            usuarioService.actualizarEstadoUsuario(usuarioId, estado, adminId);

            return ResponseEntity.ok("Estado actualizado correctamente");

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError()
                    .body("Error al actualizar estado: " + e.getMessage());
        }
    }



}

package com.copanet.api.controller;

import com.copanet.api.dtos.CrearUsuarioDTO;
import com.copanet.api.dtos.UsuarioListadoDTO;
import com.copanet.api.dtos.ModificarUsuarioDTO;
import com.copanet.api.dtos.UsuarioDetalleDTO;
import com.copanet.api.service.UsuarioService;

import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

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


}

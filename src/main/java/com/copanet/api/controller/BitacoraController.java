package com.copanet.api.controller;

import com.copanet.api.dtos.BitacoraDTO;
import com.copanet.api.service.BitacoraService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bitacora")
@CrossOrigin(origins = {"http://localhost:5173"})
public class BitacoraController {

    @Autowired
    private BitacoraService service;

    @GetMapping("/listar")
    public ResponseEntity<List<BitacoraDTO>> listar() {
        try {
            List<BitacoraDTO> lista = service.listarBitacora();
            System.out.println("✔ Registros devueltos al frontend = " + lista.size());
            return ResponseEntity.ok(lista);

        } catch (Exception e) {
            System.out.println("❌ ERROR EN /bitacora/listar ===================");
            e.printStackTrace();
            System.out.println("===============================================");
            return ResponseEntity.internalServerError().build();
        }
    }

}



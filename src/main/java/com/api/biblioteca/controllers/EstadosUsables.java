package com.api.biblioteca.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.api.biblioteca.models.EstadoEjemplar;
import com.api.biblioteca.models.EstadoMulta;
import com.api.biblioteca.models.EstadoPrestamo;
import com.api.biblioteca.models.EstadoReserva;
import com.api.biblioteca.models.EstadoUsuario;
import com.api.biblioteca.repositorys.EstadoEjemplarRepository;
import com.api.biblioteca.repositorys.EstadoMultaRepository;
import com.api.biblioteca.repositorys.EstadoPrestamoRepository;
import com.api.biblioteca.repositorys.EstadoReservaRepository;
import com.api.biblioteca.repositorys.EstadoUsuarioRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
@RequestMapping("/api/estados")
@RequiredArgsConstructor
@Tag(name = "Tipos de estados", description = "Operaciones para consultar los distintos tipos de estados.")
public class EstadosUsables {

    private final EstadoReservaRepository estadoReservaRepository;
    private final EstadoPrestamoRepository estadoPrestamoRepository;
    private final EstadoEjemplarRepository estadoEjemplarRepository;
    private final EstadoMultaRepository estadoMultaRepository;
    private final EstadoUsuarioRepository estadoUsuarioRepository;

    @Operation(summary = "Obtener estados reservas")
    @GetMapping("/reservas")
    public ResponseEntity<List<EstadoReserva>> obtenerEstadosReserva() {
        return ResponseEntity.ok(estadoReservaRepository.findAll());
    }

    @Operation(summary = "Obtener estados prestamos")
    @GetMapping("/prestamos")
    public ResponseEntity<List<EstadoPrestamo>> obtenerEstadosPrestamo() {
        return ResponseEntity.ok(estadoPrestamoRepository.findAll());
    }

    @Operation(summary = "Obtener estados ejemplares")
    @GetMapping("/ejemplares")
    public ResponseEntity<List<EstadoEjemplar>> obtenerEstadosEjemplar() {
        return ResponseEntity.ok(estadoEjemplarRepository.findAll());
    }

    @Operation(summary = "Obtener estados multas")
    @GetMapping("/multas")
    public ResponseEntity<List<EstadoMulta>> obtenerEstadosMulta() {
        return ResponseEntity.ok(estadoMultaRepository.findAll());
    }
    
    @Operation(summary = "Obtener estados usuarios")
    @GetMapping("/usuarios")
    public ResponseEntity<List<EstadoUsuario>> obtenerEstadosUsuarios() {
        return ResponseEntity.ok(estadoUsuarioRepository.findAll());
    }
}

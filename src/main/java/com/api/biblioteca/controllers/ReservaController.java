package com.api.biblioteca.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.service.annotation.PatchExchange;
import com.api.biblioteca.configurations.CustomUserDetails;
import com.api.biblioteca.dtos.request.ReservaRequest;
import com.api.biblioteca.dtos.response.ReservaResponse;
import com.api.biblioteca.dtos.updates.EstadoRequest;
import com.api.biblioteca.services.ReservaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequestMapping("api/reservas")
@RequiredArgsConstructor
public class ReservaController {

    private final ReservaService reservaService;

    @PostMapping("/usuario")
    public ResponseEntity<ReservaResponse> crearNuevo(
        @Valid @RequestBody ReservaRequest request, @AuthenticationPrincipal CustomUserDetails usuario) {
        
        return new ResponseEntity<ReservaResponse>(reservaService.crearNuevo(request, usuario), HttpStatus.CREATED);
    }

    @GetMapping("/usuario")
    public ResponseEntity<List<ReservaResponse>> misReservas(@AuthenticationPrincipal CustomUserDetails usuario) {
        return ResponseEntity.ok(reservaService.misReservas(usuario));
    }

    @PatchMapping("/usuario/{id}")
    public ResponseEntity<ReservaResponse> cambiarEstadoCancelar(@AuthenticationPrincipal CustomUserDetails usuario, @PathVariable Long id){
        return ResponseEntity.ok(reservaService.cancelarReserva(usuario, id));
    }

    @GetMapping("/bibliotecario")
    public ResponseEntity<List<ReservaResponse>> obtenerReservas(
        @RequestParam(required = false) Long estadoId
    ) {
        return ResponseEntity.ok(reservaService.obtenerReservas(estadoId));
    }

    @GetMapping("/bibliotecario/{id}")
    public ResponseEntity<ReservaResponse> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(reservaService.obtenerReservaPorId(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarPorId(@PathVariable Long id){
        reservaService.eliminarReservaPorId(id);
        return ResponseEntity.noContent().build();
    }

    @PatchExchange("/bibliotecario/{id}")
    public ResponseEntity<ReservaResponse> cambiarEstado(@PathVariable Long id, @RequestBody EstadoRequest request, @AuthenticationPrincipal CustomUserDetails usuarioAdmin){
        return ResponseEntity.ok(reservaService.cambiarEstadoReserva(id, request, usuarioAdmin));
    }

}
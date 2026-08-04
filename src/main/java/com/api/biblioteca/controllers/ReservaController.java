package com.api.biblioteca.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.service.annotation.PatchExchange;
import com.api.biblioteca.configurations.CustomUserDetails;
import com.api.biblioteca.dtos.request.ReservaRequest;
import com.api.biblioteca.dtos.response.ReservaResponse;
import com.api.biblioteca.dtos.updates.EstadoRequest;
import com.api.biblioteca.enums.EstadoReservaNombre;
import com.api.biblioteca.services.ReservaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Reservas", description = "Operaciones Reserva de libros.")
public class ReservaController {

    private final ReservaService reservaService;

    /* PARA USUARIOS */
    @Operation(summary = "Crea nueva reserva", description = "Crea nueva reserva para usuarios autenticados. Pidiendo asi como unico valor el ID del libro.")
    @PostMapping("/usuario")
    public ResponseEntity<ReservaResponse> crearNuevo(
        @Valid @RequestBody ReservaRequest request, @AuthenticationPrincipal CustomUserDetails usuario) {
        
        return new ResponseEntity<ReservaResponse>(reservaService.crearNuevo(request, usuario), HttpStatus.CREATED);
    }

    @Operation(summary = "Obtener mis reserva", description = "Devuelve el historial de reserva para usuarios autenticados. Tanto pendiente, cancelados, expirado o disponibles.")
    @GetMapping("/usuario")
    public ResponseEntity<List<ReservaResponse>> misReservas(@AuthenticationPrincipal CustomUserDetails usuario, @RequestParam(required = false) EstadoReservaNombre estado) {
        return ResponseEntity.ok(reservaService.misReservas(usuario, estado));
    }

    @Operation(summary = "Cambiar estado cancelar reserva", description = """
                Cambias el estado de una reserva para usuario autenticado. 
                Deberas de indicar el ID de reserva. 
                No podras cambiar el estado de una reserva que no le pertenezca.
            """)
    @PatchMapping("/usuario/{id}")
    public ResponseEntity<ReservaResponse> cambiarEstadoCancelar(@AuthenticationPrincipal CustomUserDetails usuario, @PathVariable Long id){
        return ResponseEntity.ok(reservaService.cancelarReserva(usuario, id));
    }




    /* BIBLIOTECARIOS */
    @Operation(summary = "Obtener reservas", description = "Obtendras un listado de la reservas, solo para bibliotecarios y administradores.")
    @GetMapping("/bibliotecario")
    public ResponseEntity<List<ReservaResponse>> obtenerReservas(
        @RequestParam(required = false) Long estadoId
    ) {
        return ResponseEntity.ok(reservaService.obtenerReservas(estadoId));
    }

    @Operation(summary = "Obtener reserva por id", description = "Obtendras una reserva especifica por ID, solo para bibliotecarios y administradores.")
    @GetMapping("/bibliotecario/{id}")
    public ResponseEntity<ReservaResponse> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(reservaService.obtenerReservaPorId(id));
    }

    @Operation(summary = "Cambiar estado de una reserva por id", description = "Cambiaras el estado de una reserva especificando el ID de la reserva, solo para bibliotecarios y administradores.")
    @PatchExchange("/bibliotecario/{id}")
    public ResponseEntity<ReservaResponse> cambiarEstado(@PathVariable Long id, @RequestBody EstadoRequest request, @AuthenticationPrincipal CustomUserDetails usuarioAdmin){
        return ResponseEntity.ok(reservaService.cambiarEstadoReserva(id, request, usuarioAdmin));
    }


    

    /* ADMINISTRADORES */
    @Operation(summary = "Eliminar reserva por id", description = "Eliminaras reserva de la base de datos por ID, solo para administradores.")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarPorId(@PathVariable Long id){
        reservaService.eliminarReservaPorId(id);
        return ResponseEntity.noContent().build();
    }

}
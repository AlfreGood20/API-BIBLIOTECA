package com.api.biblioteca.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.api.biblioteca.dtos.response.EjemplarResponse;
import com.api.biblioteca.dtos.updates.EstadoRequest;
import com.api.biblioteca.services.EjemplarService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequestMapping("api/ejemplares")
@RequiredArgsConstructor
public class EjemplarController {

    private final EjemplarService ejemplarService;

    @PostMapping("/{id}")
    public ResponseEntity<EjemplarResponse> crearNuevo(@PathVariable Long id) {
        return new ResponseEntity<EjemplarResponse>(ejemplarService.crearNuevo(id), HttpStatus.CREATED);
    }

    @GetMapping("/")
    public ResponseEntity<List<EjemplarResponse>> obtenerEjemplares(
        @RequestParam(required = false) Long libroId,
        @RequestParam(required = false) String codigo,
        @RequestParam(required = false) Long estadoId
    ) {
        return ResponseEntity.ok().body(ejemplarService.obtenerEjemplares(libroId, codigo, estadoId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<EjemplarResponse> buscarEjemplarPorId(@PathVariable Long id) {
        return ResponseEntity.ok().body(ejemplarService.obtenerEjemplarPorId(id));
    }

    @PatchMapping("/{id}/estado")
    public ResponseEntity<EjemplarResponse> cambiarEstadoEjemplar(@Valid @RequestBody EstadoRequest request, @PathVariable Long id){
        return ResponseEntity.ok().body(ejemplarService.cambiarEstadoEjemplarPorId(request, id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarEjemplarPorId(@PathVariable Long id){
        ejemplarService.eliminarEjemplarPorId(id);
        return ResponseEntity.noContent().build();
    }
}
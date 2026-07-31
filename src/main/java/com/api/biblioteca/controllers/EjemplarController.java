package com.api.biblioteca.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.api.biblioteca.dtos.response.EjemplarResponse;
import com.api.biblioteca.dtos.updates.EstadoRequest;
import com.api.biblioteca.services.EjemplarService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@RequestMapping("/api/ejemplares")
@RequiredArgsConstructor
@Tag(name = "Ejemplares", description = "Operaciones para ejemplares de libros.")
public class EjemplarController {

    private final EjemplarService ejemplarService;


    /* PARA BILBIOTECARIOS */
    @Operation(summary = "Crear ejemplar", description = "Crearas ejemplar de un libro por id. Para bibliotecarios y administradores.")
    @PostMapping("/bibliotecario/{id}")
    public ResponseEntity<EjemplarResponse> crearNuevo(@PathVariable Long id) {
        return new ResponseEntity<EjemplarResponse>(ejemplarService.crearNuevo(id), HttpStatus.CREATED);
    }


    @Operation(summary = "Obtener ejemplares", description = """
                Obtendras ejemplares, podras filtrar por libro id, codigo y estado id. Para bibliotecarios y administradores.
            """)
    @GetMapping("/bibliotecario")
    public ResponseEntity<List<EjemplarResponse>> obtenerEjemplares(
        @RequestParam(required = false) Long libroId,
        @RequestParam(required = false) String codigo,
        @RequestParam(required = false) Long estadoId
    ) {
        return ResponseEntity.ok().body(ejemplarService.obtenerEjemplares(libroId, codigo, estadoId));
    }

    @Operation(summary = "Cambiar estado ejemplar por id", description = "Cambiaras el estado de un ejemplar por id, indicandole el id del estado. Para bibliotecarios y administradores.")
    @PatchMapping("/bibliotecario/{id}/estado")
    public ResponseEntity<EjemplarResponse> cambiarEstadoEjemplar(@Valid @RequestBody EstadoRequest request, @PathVariable Long id){
        return ResponseEntity.ok().body(ejemplarService.cambiarEstadoEjemplarPorId(request, id));
    }


    @GetMapping("/bibliotecario/{id}")
    public ResponseEntity<EjemplarResponse> buscarEjemplarPorId(@PathVariable Long id) {
        return ResponseEntity.ok().body(ejemplarService.obtenerEjemplarPorId(id));
    }


    /* ADMINISTRADORES */
    @Operation(summary = "Eliminar ejemplar por id", description = "Eliminaras ejemplar por id, solo para administradores.")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarEjemplarPorId(@PathVariable Long id){
        ejemplarService.eliminarEjemplarPorId(id);
        return ResponseEntity.noContent().build();
    }
}
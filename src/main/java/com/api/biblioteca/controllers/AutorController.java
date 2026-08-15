package com.api.biblioteca.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.api.biblioteca.dtos.request.AutorRequest;
import com.api.biblioteca.dtos.response.AutorResponse;
import com.api.biblioteca.services.AutorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PutMapping;

@RestController
@RequestMapping("/api/autores")
@RequiredArgsConstructor
@Tag(name = "Autores", description = "Operaciones para autores de libros.")
public class AutorController {

    private final AutorService autorService;

    @Operation(summary = "Crear nuevo autor para libro", description = "Creas nuevo autor para libro. Solo para administradores. ")
    @PostMapping
    public ResponseEntity<AutorResponse> crearNuevo(@Valid @RequestBody AutorRequest request) {
        return new ResponseEntity<AutorResponse>(autorService.crearNuevo(request), HttpStatus.CREATED);
    }

    @Operation(summary = "Obtener un autor para libro por id", description = "Obtienes un autor por id.")
    @GetMapping("/{id}")
    public ResponseEntity<AutorResponse> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok().body(autorService.obtenerAutorPorId(id));
    }

    @Operation(summary= "Obtener autores de libros por filtros", description = "Obtienes todos los autores por diferentes filtros.")
    @GetMapping
    public ResponseEntity<List<AutorResponse>> obetenerAutores(
        @RequestParam(required = false) String nombre,
        @RequestParam(required = false) String apellidoPaterno,
        @RequestParam(required = false) String apellidoMaterno,
        @RequestParam(required = false) Long nacionalidadId
    ) {
        return ResponseEntity.ok().body(autorService.obtenerAutores(nombre, apellidoPaterno, apellidoMaterno, nacionalidadId));
    }
    
    @Operation(summary = "Actualizar autor de libro por id", description = "Actualizas un autor por id. Solo para administradores.")
    @PutMapping("/{id}")
    public ResponseEntity<AutorResponse> actulizarAutorPorId(@PathVariable Long id,@Valid AutorRequest request) {
        return ResponseEntity.ok().body(autorService.actualizarAutorPorId(request, id));
    }

    @Operation(summary = "Eliminas autor de libro por id", description = "Eliminas un autor por id. Solo para administradores.")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarPorId(@PathVariable Long id){
        autorService.eliminarAutorPorId(id);
        return ResponseEntity.noContent().build();
    }    
}
package com.api.biblioteca.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.api.biblioteca.dtos.request.NacionalidadRequest;
import com.api.biblioteca.models.Nacionalidad;
import com.api.biblioteca.services.NacionalidadService;

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


@RestController
@RequestMapping("/api/nacionalidades")
@RequiredArgsConstructor
@Tag(name = "Nacionalidades", description = "Operaciones para nacionalidad de autores.")
public class NacionalidadController {

    private final NacionalidadService nacionalidadService;

    @Operation(summary = "Crear nuevo nacionalidad", description = "Creas una nueva nacionalidad, solo para bibliotecarios y administradores.")
    @PostMapping
    public ResponseEntity<Nacionalidad> crearNuevo(@Valid @RequestBody NacionalidadRequest request) {
        return new ResponseEntity<Nacionalidad>(nacionalidadService.crearNueva(request), HttpStatus.CREATED);
    }

    @Operation(summary = "Obtener nacionalidades", description = "Obtienes todos las nacionalidades registradas. Solo para biblioticarios y administradores.")
    @GetMapping
    public ResponseEntity<List<Nacionalidad>> obtenerNacionalidades() {
        return ResponseEntity.ok().body(nacionalidadService.obtenerNacionalidades());
    }
    
    @Operation(summary = "Obtener nacionalidad por id", description = "Obtienes una nacionalidad por id. Solo para biblioticarios y administradores.")
    @GetMapping("/{id}")
    public ResponseEntity<Nacionalidad> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok().body(nacionalidadService.obtenerNacionalidadPorId(id));
    }

    @Operation(summary = "Eliminar nacionalidad por id", description = "Eliminas una nacionalidas por id. Solo para administradores.")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarPorId(@PathVariable Long id){
        nacionalidadService.eliminarNacionalidadPorId(id);
        return ResponseEntity.noContent().build();
    }
    
}
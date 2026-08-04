package com.api.biblioteca.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.api.biblioteca.models.Municipio;
import com.api.biblioteca.services.MunicipioService;
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
@RequestMapping("api/municipios")
@RequiredArgsConstructor
@Tag(name = "Municipios", description = "Operaciones para municipios usuarios.")
public class MunicipioController {

    private final MunicipioService municipioService;

    /* PUBLICOS */
    @Operation(summary = "Obtener municipios", description = "Obtendras todos los municipios. Para publicos.")
    @GetMapping("/public")
    public ResponseEntity<List<Municipio>> obtenerMunicipios() {
        return ResponseEntity.ok().body(municipioService.obtenerMunicipios());
    }

    
    /* ADMINISTRADORES */
    @Operation(summary = "Crear nuevo municipio", description = "Crear nuevo municipio, solo para administradores.")
    @PostMapping
    public ResponseEntity<Municipio> crearNuevo(@Valid @RequestBody Municipio request) {
        return new ResponseEntity<Municipio>(municipioService.crearNuevo(request), HttpStatus.CREATED);
    }
    
    @Operation(summary = "Obtener municipio por id", description = "Obtendras un municipio por id, solo para administradores")
    @GetMapping("/{id}")
    public ResponseEntity<Municipio> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok().body(municipioService.obtenerMunicipioPorId(id));
    }

    @Operation(summary = "Eliminar municipio por id", description = "Eliminaras un municpio por id, solo para administradores")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarPorId(@PathVariable Long id){
        municipioService.eliminarMunicipioPorId(id);
        return ResponseEntity.noContent().build();
    }
}
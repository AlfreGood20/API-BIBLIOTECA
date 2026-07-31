package com.api.biblioteca.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.api.biblioteca.dtos.request.IdiomaRequest;
import com.api.biblioteca.models.Idioma;
import com.api.biblioteca.services.IdiomaService;

import io.swagger.v3.oas.annotations.Hidden;
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

@Hidden
@RestController
@RequestMapping("/api/idiomas")
@RequiredArgsConstructor
@Tag(name = "Idiomas", description = "Operaciones para idiomas de libros.")
public class IdiomaController {

    private final IdiomaService idiomaService;

    @Operation(summary = "Crear nuevo idioma", description = """
                Crearas nuevo idioma, indicandole el nombre. Ten en cuneta que no se puede tener idiomas duplicados.
                Para bibliotecarios y administradores.
            """)
    @PostMapping
    public ResponseEntity<Idioma> crearNuevo(@Valid @RequestBody IdiomaRequest request) {
        return new ResponseEntity<Idioma>(idiomaService.crearNuevo(request), HttpStatus.CREATED);
    }

    @Operation(summary = "Obtener idiomas", description = "Obtendras todos los idiomas. Para bibliotecarios y administradores.")
    @GetMapping
    public ResponseEntity<List<Idioma>> obtenerIdiomas() {
        return ResponseEntity.ok().body(idiomaService.obtenerIdiomas());
    }
    
    @Operation(summary = "Obtener idioma por id", description = "Obtendras un idioma especifico por id. Pra bibliotecarios y administradores.")
    @GetMapping("/{id}")
    public ResponseEntity<Idioma> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok().body(idiomaService.obtenerIdiomaPorId(id));
    }

    @Operation(summary = "Eliminar idioma por id", description = "Eliminaras un idiomas por id. Paea bibliotecarios y administradores")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarPorId(@PathVariable Long id){
        idiomaService.eliminarIdiomaPorId(id);
        return ResponseEntity.noContent().build();
    }
}
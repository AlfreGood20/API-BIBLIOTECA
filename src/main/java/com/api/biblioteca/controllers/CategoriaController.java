package com.api.biblioteca.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.api.biblioteca.dtos.request.CategoriaRequest;
import com.api.biblioteca.models.Categoria;
import com.api.biblioteca.services.CategoriaService;
import io.swagger.v3.oas.annotations.Operation;
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
@RequestMapping("/api/categorias")
@RequiredArgsConstructor
public class CategoriaController {

    private final CategoriaService categoriaService;

    /* PUBLICOS */
    @Operation(summary = "Obtener categorias", description = "Obtienes todos las categorias. Para publicos.")
    @GetMapping("/public")
    public ResponseEntity<List<Categoria>> obtenerCategorias() {
        return ResponseEntity.ok().body(categoriaService.obtenerCategorias());
    }

    /* ADMINISTRADORES O BIBLIOTECARIOS */
    @Operation(summary = "Crea nuevo editorial", description = "Creas nuevo editorial. Solo para administradores.")
    @PostMapping
    public ResponseEntity<Categoria> crearNuevo(@Valid @RequestBody CategoriaRequest request) {
        return new ResponseEntity<Categoria>(categoriaService.crearNuevo(request), HttpStatus.CREATED);
    }
    
    @Operation(summary = "Obtener categoria por id", description = "Obtienes categoria por id. Para bibliotecario y administradores.")
    @GetMapping("/{id}")
    public ResponseEntity<Categoria> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok().body(categoriaService.obtenerCategoriaPorId(id));
    }

    @Operation(summary = "Eliminar por categoria por id", description = "Eliminas categoria por id. Solo para administradores.")
    @DeleteMapping
    public ResponseEntity<Void> eliminarPorId(@PathVariable Long id){
        categoriaService.eliminarCategoriaPorId(id);
        return ResponseEntity.noContent().build();
    }
}
package com.api.biblioteca.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.api.biblioteca.dtos.request.EditorialRequest;
import com.api.biblioteca.models.Editorial;
import com.api.biblioteca.services.EditorialService;

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
@RequestMapping("/api/editoriales")
@RequiredArgsConstructor
@Tag(name = "Editoriales", description = "Operaciones editoriales de libros.")
public class EditorialController {

    private final EditorialService editorialService;

    @Operation(summary = "Crear nuevo editorial para libros", description = "Creas un nuevo editorial. Solo para administradores.")
    @PostMapping
    public ResponseEntity<Editorial> crearNuevo(@Valid @RequestBody EditorialRequest request) {
        return new ResponseEntity<Editorial>(editorialService.crearNuevo(request), HttpStatus.CREATED);
    }

    @Operation(summary = "Obtener editoriales para libros", description = "Obtienes los editoriales.")
    @GetMapping
    public ResponseEntity<List<Editorial>> obtenerEditoriales() {
        return ResponseEntity.ok().body(editorialService.obtenerEditoriales());
    }
    
    @Operation(summary = "Obtener editorial para libro por id", description = "Obtienes un editorial por id ingresado.")
    @GetMapping("/{id}")
    public ResponseEntity<Editorial> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok().body(editorialService.obtenerEditorialPorId(id));
    }

    @Operation(summary = "Eliminar editorial para libro por id", description = "Eliminas un editoial por id ingresado.")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarPorId(@PathVariable Long id){
        editorialService.eliminarEditorialPorId(id);
        return ResponseEntity.noContent().build();
    }
}
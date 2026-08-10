package com.api.biblioteca.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import com.api.biblioteca.dtos.request.LibroRequest;
import com.api.biblioteca.dtos.response.AutorResponse;
import com.api.biblioteca.dtos.response.EjemplarResponse;
import com.api.biblioteca.dtos.response.LibroCatalogoResponse;
import com.api.biblioteca.dtos.response.LibroResponse;
import com.api.biblioteca.dtos.response.PaginaResponse;
import com.api.biblioteca.services.LibroService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Encoding;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import java.util.List;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.PutMapping;

@RestController
@RequestMapping("/api/libros")
@RequiredArgsConstructor
@Tag(name = "Libros", description = "Operaciones para libros.")
public class LibroController {

    private final LibroService libroService;


    /* PARA PUBLICOS */
    @Operation(summary = "Obtener libro por id", description = "Obtendras un libro especifico por id. Para publicos.")
    @GetMapping("/public/{id}")
    public ResponseEntity<LibroResponse> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok().body(libroService.obtenerLibroPorId(id));
    }

    @Operation(summary = "Obtener libros de un autor por id", description = "Obtendras todos los libros de un autor ingresandole el id. Para publicos.")
    @GetMapping("/public/{id}/autores")
    public ResponseEntity<List<AutorResponse>> obtenerAutoresDeUnLibro(@PathVariable Long id) {
        return ResponseEntity.ok().body(libroService.obtenerAutoresDeUnLibro(id));
    }

    @Operation(summary = "Obtener libros", description = """
                Obtendras todos los libros, podras filtrar por titulo, isbn, id de la categoria, editorial y idiomas.
                para publicos.
            """)
    @GetMapping("/public")
    public ResponseEntity<PaginaResponse<LibroCatalogoResponse>> obtenerLibros(
        @RequestParam(required = false) String titulo,
        @RequestParam(required = false) String isbn,
        @RequestParam(required = false) Long categoriaId,
        @RequestParam(required = false) Long editorialId,
        @RequestParam(required = false) Long idiomaId,
        @ParameterObject
        @PageableDefault(page = 0, size = 10, sort = "id", direction = Sort.Direction.ASC) Pageable pageable
    ) {
        return ResponseEntity.ok().body(libroService.obtenerLibros(titulo, isbn, categoriaId, editorialId, idiomaId, pageable));
    }



    /* BIBLIOTECARIOS O ADMINISTRADORES */
    @Operation(summary = "Obtener ejemplares por libro id", description = "Obtendras todos los ejemplares de un libro, solo para bibliotecarios y administradores.")
    @GetMapping("/bibliotecario/{id}/ejemplares")
    public ResponseEntity<List<EjemplarResponse>> obtenerEjemplaresDeUnLibro(@PathVariable Long id) {
        return ResponseEntity.ok().body(libroService.obtenerEjemplaresLibroPorId(id));
    }



    /* ADMINISTRADORES */
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
        content = @Content(
            encoding = {
                @Encoding(name = "libro", contentType = MediaType.APPLICATION_JSON_VALUE),
                @Encoding(name = "imagen", contentType = MediaType.IMAGE_JPEG_VALUE+" , "+MediaType.IMAGE_PNG_VALUE)
            }
        )
    )
    @Operation(summary = "Crear nuevo libro", description = """
                Crearas nuevo libro especificando los datos, solo para administradores.
                Para la portada del libro solo se aceptara en formatos PNG y JPG (JPEG).
            """)
    @PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<LibroResponse> crearNuevo(@Valid @RequestPart(name = "libro") LibroRequest request, @RequestPart(required = false, name = "imagen")MultipartFile file) {
        return new ResponseEntity<LibroResponse>(libroService.crearNuevo(request, file), HttpStatus.CREATED);
    }

    @Operation(summary = "Eliminar libro por id", description = "Eliminaras libro por id, solo para administradores.")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarLibroPorId(@PathVariable Long id){
        libroService.eliminarLibroPorId(id);
        return ResponseEntity.noContent().build();
    }        

    @Operation(summary = "Actualizar libro por id", description = "Actulizaras libro por id, solo para administradores.")
    @PutMapping("/{id}")
    public ResponseEntity<LibroResponse> actualizarLibro(@Valid @RequestBody LibroRequest request, @PathVariable Long id,@RequestParam(required = false) MultipartFile file) {
        return ResponseEntity.ok().body(libroService.actualizarLibro(request, id, file));
    }
    
}


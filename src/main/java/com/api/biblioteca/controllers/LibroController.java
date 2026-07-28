package com.api.biblioteca.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import com.api.biblioteca.dtos.request.LibroRequest;
import com.api.biblioteca.dtos.response.AutorResponse;
import com.api.biblioteca.dtos.response.EjemplarResponse;
import com.api.biblioteca.dtos.response.LibroResponse;
import com.api.biblioteca.services.LibroService;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Encoding;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import java.util.List;
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
@RequestMapping("api/libros")
@RequiredArgsConstructor
public class LibroController {

    private final LibroService libroService;

    @io.swagger.v3.oas.annotations.parameters.RequestBody(
        content = @Content(
            encoding = {
                @Encoding(name = "libro", contentType = MediaType.APPLICATION_JSON_VALUE),
                @Encoding(name = "imagen", contentType = MediaType.IMAGE_JPEG_VALUE+" , "+MediaType.IMAGE_PNG_VALUE)
            }
        )
    )
    @PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<LibroResponse> crearNuevo(@Valid @RequestPart(name = "libro") LibroRequest request, @RequestPart(required = false, name = "imagen")MultipartFile file) {
        return new ResponseEntity<LibroResponse>(libroService.crearNuevo(request, file), HttpStatus.CREATED);
    }

    @GetMapping("/public")
    public ResponseEntity<List<LibroResponse>> obtenerLibros(
        @RequestParam(required = false) String titulo,
        @RequestParam(required = false) String isbn,
        @RequestParam(required = false) Long categoriaId,
        @RequestParam(required = false) Long editorialId,
        @RequestParam(required = false) Long idiomaid
    ) {
        return ResponseEntity.ok().body(libroService.obtenerLibros(titulo, isbn, categoriaId, editorialId, idiomaid));
    }

    @GetMapping("/public/{id}")
    public ResponseEntity<LibroResponse> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok().body(libroService.obtenerLibroPorId(id));
    }

    @GetMapping("/public/{id}/autores")
    public ResponseEntity<List<AutorResponse>> obtenerAutoresDeUnLibro(@PathVariable Long id) {
        return ResponseEntity.ok().body(libroService.obtenerAutoresDeUnLibro(id));
    }

    @GetMapping("/{id}/ejemplares")
    public ResponseEntity<List<EjemplarResponse>> obtenerEjemplaresDeUnLibro(@PathVariable Long id) {
        return ResponseEntity.ok().body(libroService.obtenerEjemplaresLibroPorId(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<LibroResponse> actualizarLibro(@Valid @RequestBody LibroRequest request, @PathVariable Long id,@RequestParam(required = false) MultipartFile file) {
        return ResponseEntity.ok().body(libroService.actualizarLibro(request, id, file));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarLibroPorId(@PathVariable Long id){
        libroService.eliminarLibroPorId(id);
        return ResponseEntity.noContent().build();
    }        
}

package com.api.biblioteca.services;

import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;
import com.api.biblioteca.dtos.request.LibroRequest;
import com.api.biblioteca.dtos.response.AutorResponse;
import com.api.biblioteca.dtos.response.EjemplarResponse;
import com.api.biblioteca.dtos.response.LibroResponse;
import com.api.biblioteca.dtos.response.PaginaResponse;

public interface LibroService {

    LibroResponse crearNuevo(LibroRequest request, MultipartFile file);

    PaginaResponse<LibroResponse> obtenerLibros(String titulo, String isbn, Long categoriaId, Long editorialId, Long idiomaId, Pageable pageable);

    LibroResponse obtenerLibroPorId(Long id);

    List<AutorResponse> obtenerAutoresDeUnLibro(Long id);

    List<EjemplarResponse> obtenerEjemplaresLibroPorId(Long id);

    LibroResponse actualizarLibro(LibroRequest request, Long id, MultipartFile file);

    void eliminarLibroPorId(Long id);
}
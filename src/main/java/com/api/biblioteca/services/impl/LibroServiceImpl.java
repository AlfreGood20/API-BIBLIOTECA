package com.api.biblioteca.services.impl;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.UUID;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.api.biblioteca.dtos.request.LibroRequest;
import com.api.biblioteca.dtos.response.AutorResponse;
import com.api.biblioteca.dtos.response.EjemplarResponse;
import com.api.biblioteca.dtos.response.LibroResponse;
import com.api.biblioteca.exceptions.BusinessExeption;
import com.api.biblioteca.exceptions.ResourceNotFoundException;
import com.api.biblioteca.mappers.AutorMapper;
import com.api.biblioteca.mappers.EjemplarMapper;
import com.api.biblioteca.mappers.LibroMapper;
import com.api.biblioteca.models.Autor;
import com.api.biblioteca.models.Categoria;
import com.api.biblioteca.models.Editorial;
import com.api.biblioteca.models.Idioma;
import com.api.biblioteca.models.Libro;
import com.api.biblioteca.repositorys.AutorRepository;
import com.api.biblioteca.repositorys.CategoriaRepository;
import com.api.biblioteca.repositorys.EditorialRepository;
import com.api.biblioteca.repositorys.IdiomaRepository;
import com.api.biblioteca.repositorys.LibroRepository;
import com.api.biblioteca.services.LibroService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LibroServiceImpl implements LibroService{
    
    private final LibroRepository libroRepository;
    private final CategoriaRepository categoriaRepository;
    private final EditorialRepository editorialRepository;
    private final AutorRepository autorRepository;
    private final IdiomaRepository idiomaRepository;
    private final LibroMapper libroMapper;
    private final AutorMapper autorMapper;
    private final EjemplarMapper ejemplarMapper;

    private final Path uploadPath;

    @Override
    public LibroResponse crearNuevo(LibroRequest request, MultipartFile file) {

        Categoria categoria = buscarCategoriaPorId(request.categoriaId());
        Editorial editorial = buscarEditorialPorId(request.editorialId());
        Idioma idioma = buscarIdiomaPorId(request.idiomaId());

        List<Autor> autores = request.autoresId()
            .stream()
            .map(id -> buscarAutorPorId(id))
            .toList();

        Libro libro = libroMapper.dtoToEntity(request);
        libro.setCategoria(categoria);
        libro.setEditorial(editorial);
        libro.setIdioma(idioma);
        libro.setAutores(autores);

        if(file != null && !file.isEmpty()){
            libro.setPortadaUrl(obtenerUrlPortada(file));
        }
        
        return libroMapper.entityToDto(libroRepository.save(libro));
    }

    @Override
    public List<LibroResponse> obtenerLibros(String titulo, String isbn, Long categoriaId, Long editorialId, Long idiomaId) {
        return libroMapper.listEntityToListDto(libroRepository.findByFiltros(titulo, isbn, categoriaId, editorialId, idiomaId));
    }

    @Override
    public LibroResponse obtenerLibroPorId(Long id) {
        Libro libro = libroRepository.findById(id)
            .orElseThrow(()->new ResourceNotFoundException("Libro no encontrado"));

        return libroMapper.entityToDto(libro);
    }

    @Override
    public List<AutorResponse> obtenerAutoresDeUnLibro(Long id) {
        Libro libro = libroRepository.findById(id)
            .orElseThrow(()->new ResourceNotFoundException("Libro no encontrado"));

        return autorMapper.listEntityToListDto(libro.getAutores());
    }

    @Override
    public List<EjemplarResponse> obtenerEjemplaresLibroPorId(Long id) {
        Libro libro = libroRepository.findById(id)
            .orElseThrow(()->new ResourceNotFoundException("Libro no encontrado"));

        return ejemplarMapper.listEntityToListDto(libro.getEjemplares());
    }

    @SuppressWarnings("null")
    @Override
    public LibroResponse actualizarLibro(LibroRequest request, Long id, MultipartFile file) {
        Libro libro = libroRepository.findById(id)
            .orElseThrow(()->new ResourceNotFoundException("Libro no encontrado"));

        Categoria categoria = buscarCategoriaPorId(request.categoriaId());
        Editorial editorial = buscarEditorialPorId(request.editorialId());
        Idioma idioma = buscarIdiomaPorId(request.idiomaId());

        List<Autor> autores = request.autoresId()
            .stream()
            .map(aId -> buscarAutorPorId(aId))
            .toList();

        if(file != null || !file.isEmpty()){
            libro.setPortadaUrl(obtenerUrlPortada(file));
        }
        
        libro.setTitulo(request.titulo());
        libro.setSinopsis(request.sinopsis());
        libro.setIsbn(request.isbn());
        libro.setNumeroPaginas(request.numeroPaginas());
        libro.setAnio(request.anio());
        libro.setCategoria(categoria);
        libro.setEditorial(editorial);
        libro.setIdioma(idioma);
        libro.setAutores(autores);

        return libroMapper.entityToDto(libroRepository.save(libro));
    }

    @Override
    public void eliminarLibroPorId(Long id) {
        Libro libro = libroRepository.findById(id)
            .orElseThrow(()->new ResourceNotFoundException("Libro no encontrado"));

        if(!libro.getAutores().isEmpty() || !libro.getEjemplares().isEmpty()){
            throw new BusinessExeption("Este libro tiene relaciones con autores o ejemplares, no se puede eliminar");
        }

        libroRepository.delete(libro);
    }


    //FUNCIONES REUTILIAZBLES
    private Categoria buscarCategoriaPorId(Long id){
        return categoriaRepository.findById(id)
            .orElseThrow(()->new ResourceNotFoundException("Categoria no encontrado"));
    }

    private Editorial buscarEditorialPorId(Long id){
        return editorialRepository.findById(id)
            .orElseThrow(()-> new ResourceNotFoundException("Editorial no encontrado"));
    }

    private Autor buscarAutorPorId(Long id){
        return autorRepository.findById(id)
            .orElseThrow(()-> new ResourceNotFoundException("Autor no encontrado"));
    }

    private Idioma buscarIdiomaPorId(Long id){
        return idiomaRepository.findById(id)
            .orElseThrow(()->new ResourceNotFoundException("Idioma no encontrado"));
    }

    private String obtenerUrlPortada(MultipartFile file){

        String contentType = file.getContentType();

        if (!MediaType.IMAGE_JPEG_VALUE.equals(contentType) && !MediaType.IMAGE_PNG_VALUE.equals(contentType)) {
            throw new BusinessExeption("Solo se permiten imágenes JPG o PNG.");
        }
        
        String nombreImagenOriginal = file.getOriginalFilename();

        String nombreImagen = 
            UUID.randomUUID().toString().substring(0,10) +
            nombreImagenOriginal.substring(nombreImagenOriginal.indexOf("."));

        try {
            Path rutaCompleta = uploadPath.resolve(nombreImagen);
            Files.copy(file.getInputStream(), rutaCompleta);
        } catch (IOException ex) {}

        return "/uploads/"+nombreImagen;
    }
}

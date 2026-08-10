package com.api.biblioteca.mappers;

import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import com.api.biblioteca.dtos.request.LibroRequest;
import com.api.biblioteca.dtos.response.LibroCatalogoResponse;
import com.api.biblioteca.dtos.response.LibroResponse;
import com.api.biblioteca.models.Libro;

@Mapper(componentModel = "spring", uses = {AutorMapper.class})
public interface LibroMapper {

    // Entity to dto
    @Mapping(source = "categoria.nombre", target = "categoria")
    @Mapping(source = "editorial.nombre", target = "editorial")
    @Mapping(source = "idioma.nombre", target = "idioma")
    LibroResponse entityToDto(Libro entity);

    // dto to entity
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "categoria", ignore = true)
    @Mapping(target = "editorial", ignore = true)
    @Mapping(target = "idioma", ignore = true)
    @Mapping(target = "autores", ignore = true)
    @Mapping(target = "ejemplares", ignore = true)
    @Mapping(target = "portadaUrl", ignore = true)
    Libro dtoToEntity(LibroRequest request);

    //PARA LA PAGINACION entity to dto
    LibroCatalogoResponse entityToDtoCatalogo(Libro entity);


    List<LibroResponse> listEntityToListDto(List<Libro> list); 
}

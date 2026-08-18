package com.api.biblioteca.mappers;

import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import com.api.biblioteca.dtos.request.PrestamoRequest;
import com.api.biblioteca.dtos.response.PrestamoResponse;
import com.api.biblioteca.models.Prestamo;

@Mapper(componentModel = "spring", uses = {EjemplarMapper.class, UsuarioMapper.class})
public interface PrestamoMapper {

    /* ============= ENTIDAD A DTO RESPONSE ============= */
    @Mapping(source = "estado.nombre", target = "estado")
    @Mapping(source = "usuarioAdmin", target = "autorizo")
    PrestamoResponse entityToDto(Prestamo entity);

    /* ============= DTO REQUEST A ENTIDAD ============= */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "fechaRegistro", ignore = true)
    @Mapping(target = "ejemplar", ignore = true)
    @Mapping(target = "fechaLimite", ignore = true)
    @Mapping(target = "fechaDevolucion", ignore = true)
    @Mapping(target = "estado", ignore = true)
    @Mapping(target = "usuario", ignore = true)
    @Mapping(target = "usuarioAdmin", ignore = true)
    Prestamo dtoToEntity(PrestamoRequest request);

    /* ===== LISTA DE ENTIDAD A LISTA DTO RESPONSE ======== */
    List<PrestamoResponse> listEntityToListDto(List<Prestamo> list);
}

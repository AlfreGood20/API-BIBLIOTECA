package com.api.biblioteca.mappers;

import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import com.api.biblioteca.dtos.request.UsuarioPublicRequest;
import com.api.biblioteca.dtos.request.UsuarioRequest;
import com.api.biblioteca.dtos.response.UsuarioResponse;
import com.api.biblioteca.dtos.response.UsuarioResumen;
import com.api.biblioteca.models.Usuario;

@Mapper(componentModel = "spring", uses = {TelefonoMapper.class, DireccionMapper.class})
public interface UsuarioMapper {

    /* ================== ENTIDAD A DTO RESPONSE ===================== */
    @Mapping(source = "estado.nombre", target = "estado")
    @Mapping(source = "rol.nombre", target = "rol")
    @Mapping(source = "credencial.correo", target = "correo")
    @Mapping(target = "direccion", ignore = true)
    UsuarioResponse entityToDto(Usuario entity);

    /* ================ ENTIDAD A DTO RESUMEN ========================= */
    @Mapping(source = "credencial.correo", target = "correo")
    UsuarioResumen entityToResumen(Usuario entity);

    /* ================ DTO REQUEST A ENTIDAD ========================= */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "credencial", ignore = true)
    @Mapping(target = "fechaRegistro", ignore = true)
    @Mapping(target = "estado", ignore = true)
    @Mapping(target = "rol", ignore = true)
    @Mapping(target = "tokens", ignore = true)
    Usuario dtoToEntity(UsuarioRequest request);

    /* ============= DTO REQUEST REGISTRO A ENTIDAD =================== */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "fechaRegistro", ignore = true)
    @Mapping(target = "credencial", ignore = true)
    @Mapping(target = "rol", ignore = true)
    @Mapping(target = "estado", ignore = true)
    @Mapping(target = "fotoUrl", ignore = true)
    @Mapping(target = "tokens", ignore = true)
    Usuario dtoPublicToEntity(UsuarioPublicRequest request);

    List<UsuarioResponse> listEntityToListDto(List<Usuario> list);
}

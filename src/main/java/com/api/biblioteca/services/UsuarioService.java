package com.api.biblioteca.services;

import java.util.List;
import org.springframework.web.multipart.MultipartFile;
import com.api.biblioteca.configurations.CustomUserDetails;
import com.api.biblioteca.dtos.request.UsuarioRequest;
import com.api.biblioteca.dtos.response.MultaResponse;
import com.api.biblioteca.dtos.response.PrestamoResponse;
import com.api.biblioteca.dtos.response.ReservaResponse;
import com.api.biblioteca.dtos.response.UsuarioResponse;
import com.api.biblioteca.dtos.updates.EstadoRequest;
import com.api.biblioteca.dtos.updates.PerfilUpdate;
import com.api.biblioteca.enums.EstadoUsuarioNombre;
import com.api.biblioteca.enums.RolNombre;

public interface UsuarioService {

    /* PARA ADMIN */
    UsuarioResponse crearNuevo(UsuarioRequest request);
    List<UsuarioResponse> obtenerUsuarios();
    UsuarioResponse obtenerPorId(Long id);
    UsuarioResponse cambiarEstadoUsuario(Long id, EstadoRequest request);
    List<UsuarioResponse> obtenerUsuariosPorParametros(String nombre, RolNombre rol, EstadoUsuarioNombre estado);
    UsuarioResponse actulizarDatos(Long id); // POR HACER
    List<PrestamoResponse> prestamosPorUsuario(Long id);
    List<MultaResponse> multasPorUsuario(Long id);
    List<ReservaResponse> reservasPorUsuario(Long id);

    /* PARA PERFIL DE USUARIOS */
    UsuarioResponse obtenerPerfil (CustomUserDetails usuario);
    UsuarioResponse actualizarFotoPerfil (CustomUserDetails usuario, MultipartFile file);
    UsuarioResponse actualizarDatosPerfil (CustomUserDetails usuario, PerfilUpdate request);
}

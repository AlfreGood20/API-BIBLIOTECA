package com.api.biblioteca.services;

import java.util.List;

import com.api.biblioteca.configurations.CustomUserDetails;
import com.api.biblioteca.dtos.request.PrestamoRequest;
import com.api.biblioteca.dtos.response.PrestamoResponse;
import com.api.biblioteca.models.Prestamo;

public interface PrestamoService {

    List<PrestamoResponse> crearNuevo(PrestamoRequest request, CustomUserDetails usuarioAdmin);

    List<PrestamoResponse> obtenerPrestamos(Long estadoId, Long usuarioAdminId, Long usuarioId);

    PrestamoResponse obtenerPrestamoPorId(Long id);

    PrestamoResponse devolverPrestamoPorId(Long id);
    
    List<PrestamoResponse> misPrestamos (CustomUserDetails usuario);

    List<Prestamo> actualizarPrestamosEstadoVencido();
}

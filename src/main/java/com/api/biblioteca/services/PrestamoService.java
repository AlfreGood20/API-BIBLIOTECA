package com.api.biblioteca.services;

import java.util.List;
import org.springframework.data.domain.Pageable;
import com.api.biblioteca.configurations.CustomUserDetails;
import com.api.biblioteca.dtos.request.PrestamoRequest;
import com.api.biblioteca.dtos.response.PaginaResponse;
import com.api.biblioteca.dtos.response.PrestamoResponse;
import com.api.biblioteca.enums.EstadoPrestamoNombre;
import com.api.biblioteca.models.Prestamo;
import com.api.biblioteca.models.Usuario;

public interface PrestamoService {

    /* PARA ADMINISTRADORES Y BIBLIOTECARIOS */
    List<PrestamoResponse> crearNuevo(PrestamoRequest request, CustomUserDetails usuarioAdmin);
    PaginaResponse<PrestamoResponse> obtenerPrestamos(EstadoPrestamoNombre estado, Long usuarioAdminId, Long usuarioId, Pageable pageable);
    PrestamoResponse obtenerPrestamoPorId(Long id);
    PrestamoResponse devolverPrestamoPorId(Long id);
    Prestamo generarPrestamo(Usuario usuario, Long ejemplarId, Usuario autoriza);
    
    /* PARA USUARIOS APP WEB RESERVAS */
    PaginaResponse<PrestamoResponse> misPrestamos (CustomUserDetails usuario, EstadoPrestamoNombre estado, Pageable pageable);

    /* PARA SCHEDULED */
    List<Prestamo> actualizarPrestamosEstadoVencido();
}

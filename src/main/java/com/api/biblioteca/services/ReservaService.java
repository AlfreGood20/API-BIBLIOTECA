package com.api.biblioteca.services;

import java.util.List;

import com.api.biblioteca.configurations.CustomUserDetails;
import com.api.biblioteca.dtos.request.ReservaRequest;
import com.api.biblioteca.dtos.response.ReservaResponse;
import com.api.biblioteca.dtos.updates.EstadoRequest;

public interface ReservaService {

    // Usuario
    ReservaResponse crearNuevo(ReservaRequest request, CustomUserDetails usuario);

    // Usuario sus reservas
    List<ReservaResponse> misReservas(CustomUserDetails usuario);

    ReservaResponse cancelarReserva(CustomUserDetails usuario, Long id);

    //Admin y bibliotecario
    List<ReservaResponse> obtenerReservas(Long estadoId);

    //Admin, bibliotecario y usuario
    ReservaResponse obtenerReservaPorId(Long id);

    //admin
    void eliminarReservaPorId(Long id);

    // admin y bibliotecario
    ReservaResponse cambiarEstadoReserva(Long id, EstadoRequest request, CustomUserDetails usuarioAdmin);
}
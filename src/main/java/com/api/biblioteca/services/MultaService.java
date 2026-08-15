package com.api.biblioteca.services;

import java.util.List;

import org.springframework.data.domain.Pageable;

import com.api.biblioteca.configurations.CustomUserDetails;
import com.api.biblioteca.dtos.response.MultaResponse;
import com.api.biblioteca.dtos.response.PaginaResponse;
import com.api.biblioteca.enums.EstadoMultaNombre;
import com.api.biblioteca.models.Multa;
import com.api.biblioteca.models.Prestamo;

public interface MultaService {

    List<MultaResponse> obtenerMultas(Long estadoId);
    MultaResponse pagarMulta(Long id);
    MultaResponse obtenerMultaPorId(Long id);
    List<Multa> generarMultas(List<Prestamo> prestamosVencidos);
    List<Multa> actualizarDiasRetraso();

    /* SERVICIO PARA USUARIOS */
    PaginaResponse<MultaResponse> misMultas(CustomUserDetails usuario, EstadoMultaNombre estado, Pageable pageable);
}
package com.api.biblioteca.services;

import java.util.List;
import com.api.biblioteca.dtos.response.MultaResponse;
import com.api.biblioteca.models.Multa;
import com.api.biblioteca.models.Prestamo;

public interface MultaService {

    List<MultaResponse> obtenerMultas(Long estadoId);

    MultaResponse pagarMulta(Long id);

    MultaResponse obtenerMultaPorId(Long id);

    List<Multa> generarMultas(List<Prestamo> prestamosVencidos);

    List<Multa> actualizarDiasRetraso();
}
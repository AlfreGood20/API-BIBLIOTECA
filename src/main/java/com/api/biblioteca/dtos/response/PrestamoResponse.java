package com.api.biblioteca.dtos.response;

import java.time.LocalDate;
import java.time.LocalDateTime;
import com.api.biblioteca.enums.EstadoPrestamoNombre;
import com.fasterxml.jackson.annotation.JsonProperty;

public record PrestamoResponse
(
    Long id,
    @JsonProperty("fecha_registro") LocalDateTime fechaRegistro,
    @JsonProperty("fecha_limite") LocalDate fechaLimite,
    @JsonProperty("fecha_devolucion") LocalDate fechaDevolucion,
    EstadoPrestamoNombre estado,
    UsuarioResumen autorizo,
    EjemplarResponse ejemplar
) {
}
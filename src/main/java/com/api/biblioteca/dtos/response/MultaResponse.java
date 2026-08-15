package com.api.biblioteca.dtos.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonProperty;

public record MultaResponse
(
    Long id,
    @JsonProperty("dias_retraso")
    int diasRetraso,
    @JsonProperty("fecha_registro")
    LocalDateTime fechaRegistro,
    @JsonProperty("costo_unitario")
    BigDecimal costoUnitario,
    @JsonProperty("fecha_pago")
    LocalDateTime fechaPago,
    BigDecimal importe,
    String estado
) {
}
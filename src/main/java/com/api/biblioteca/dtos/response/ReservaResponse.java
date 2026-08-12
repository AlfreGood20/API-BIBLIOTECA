package com.api.biblioteca.dtos.response;

import java.time.LocalDate;
import java.time.LocalDateTime;
import com.fasterxml.jackson.annotation.JsonProperty;

public record ReservaResponse
(
    Long id,
    @JsonProperty("fecha_reserva")
    LocalDateTime fechaReserva,
    @JsonProperty("fecha_limite_recoger")
    LocalDate fechaLimiteRecoger,
    LibroCatalogoResponse libro,
    String estado
) {
}
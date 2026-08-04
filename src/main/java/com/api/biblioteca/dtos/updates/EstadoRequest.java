package com.api.biblioteca.dtos.updates;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record EstadoRequest(

    @NotNull(message = "Es obligatorio")
    @Positive(message = "El valor tiene que ser positivo")
    @JsonProperty("estado_id")
    Long id
) {
}
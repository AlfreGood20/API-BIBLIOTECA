package com.api.biblioteca.dtos.updates;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record TelefonoUpdate
(
    @NotNull(message = "Es obligatorio")
    @Positive(message = "El valor tiene que ser positivo")
    @JsonProperty("telefono_id")
    Long id,
    @NotBlank(message = "Es obligatorio")
    String numero
) {
}
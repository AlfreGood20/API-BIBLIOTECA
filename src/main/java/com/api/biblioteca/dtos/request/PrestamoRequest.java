package com.api.biblioteca.dtos.request;

import java.util.Set;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record PrestamoRequest
(
    @NotNull(message = "Es obligatorio")
    @Positive(message = "El valor tiene que ser positivo")
    @JsonProperty("usuario_id")
    Long usuarioId,

    @NotEmpty(message = "Es obligatorio")
    @JsonProperty("ejemplar_ids")
    Set<@Positive(message = "Los valores tiene que ser positivos") Long> ejemplaresId
) {
}
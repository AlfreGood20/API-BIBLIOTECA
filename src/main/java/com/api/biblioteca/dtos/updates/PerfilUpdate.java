package com.api.biblioteca.dtos.updates;

import java.util.List;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

public record PerfilUpdate
(
    @NotBlank(message = "Es obligatorio")
    String nombre,
    @NotBlank(message = "Es obligatorio")
    String apellidoPaterno,
    @NotBlank(message = "Es obligatorio")
    String apellidoMaterno,
    @Valid
    @NotEmpty(message = "Es obligatorio")
    List<TelefonoUpdate> telefonos
) {
}
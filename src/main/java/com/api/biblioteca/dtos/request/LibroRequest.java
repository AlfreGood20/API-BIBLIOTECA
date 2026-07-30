package com.api.biblioteca.dtos.request;

import java.util.Set;
import org.hibernate.validator.constraints.UniqueElements;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record LibroRequest
(

    @NotBlank(message = "Es obligatorio")
    String titulo,

    String sinopsis,

    @NotBlank(message = "Es obligatorio")
    String isbn,

    @Positive(message = "El valor tiene que ser positivo")
    @JsonProperty("numero_paginas")
    int numeroPaginas,

    @Positive(message = "El valor tiene ser positivo")
    int anio,

    @NotNull(message = "Es obligatorio")
    @Positive(message = "El valor tiene que ser positivo")
    @JsonProperty("categoria_id")
    Long categoriaId,

    @NotNull(message = "Es obligatorio")
    @Positive(message = "El valor tiene que ser positivo")
    @JsonProperty("editorial_id")
    Long editorialId,

    @NotNull(message = "Es obligatorio")
    @Positive(message = "El valor tiene que ser positivo")
    @JsonProperty("idioma_id")
    Long idiomaId,

    @NotEmpty(message = "Es obligatorio")
    @JsonProperty("autores_id")
    @UniqueElements(message = "No se permiten valores duplicados")
    Set<@Positive(message = "Los valores tiene que ser positivo") Long> autoresId
) {
}

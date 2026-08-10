package com.api.biblioteca.dtos.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public record LibroCatalogoResponse(

    Long id,
    String titulo,
    String isbn,
    int anio,
    @JsonProperty("portada_url")
    String portadaUrl
) {
}

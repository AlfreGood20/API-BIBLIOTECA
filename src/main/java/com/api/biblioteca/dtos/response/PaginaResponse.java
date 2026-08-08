package com.api.biblioteca.dtos.response;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonProperty;

public record PaginaResponse<T>(

    List<T> contenido,
    @JsonProperty("pagina_actual")
    int paginaActual,
    @JsonProperty("total_paginas")
    int totalPaginas,
    @JsonProperty("total_elementos")
    long totalElementos,
    @JsonProperty("es_primera")
    boolean esPrimera,
    @JsonProperty("es_ultima")
    boolean esUltima
) {
}
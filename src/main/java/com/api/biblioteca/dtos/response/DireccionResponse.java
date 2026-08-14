package com.api.biblioteca.dtos.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public record DireccionResponse
(
    Long id,
    String calle,
    String colonia,
    @JsonProperty("codigo_postal")
    String codigoPostal,
    String municipio,
    @JsonProperty("numero_exterior")
    String numeroExterior,
    @JsonProperty("numero_interior")
    String numeroInterior
) {
}
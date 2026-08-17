package com.api.biblioteca.dtos.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public record UsuarioResumen
(
    Long id,
    String nombre, 
    @JsonProperty("apellido_paterno") String apellidoPaterno,
    @JsonProperty("apellido_materno") String apellidoMaterno,
    String correo
) {
}
package com.api.biblioteca.dtos.response;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class UsuarioResponse {

    private Long id;
    private String nombre;
    @JsonProperty("apellido_paterno")
    private String apellidoPaterno;
    @JsonProperty("apellido_materno")
    private String apellidoMaterno;
    @JsonProperty("fecha_nacimiento")
    private LocalDate fechaNacimiento;
    private String genero;
    private String curp;
    private String correo;
    @JsonProperty("fecha_registro")
    private LocalDateTime fechaRegistro;
    @JsonProperty("foto_url")
    private String fotoUrl;
    private String rol;
    private String estado;
    private List<TelefonoResponse> telefonos;
    private DireccionResponse direccion;
} 
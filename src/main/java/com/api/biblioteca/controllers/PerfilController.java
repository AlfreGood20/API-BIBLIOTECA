package com.api.biblioteca.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import com.api.biblioteca.configurations.CustomUserDetails;
import com.api.biblioteca.dtos.response.UsuarioResponse;
import com.api.biblioteca.dtos.updates.PerfilUpdate;
import com.api.biblioteca.services.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/api/perfil")
@RequiredArgsConstructor
@Tag(name = "Perfiles", description = "Operaciones de perfil, para autenticados.")
public class PerfilController {

    private final UsuarioService usuarioService;

    @Operation(summary = "Obtener perfil", description = "Obtendras el perfil autenticado, deberas de autenticarte con el token de acesso.")
    @GetMapping
    public ResponseEntity<UsuarioResponse> obtenerPerfil(@AuthenticationPrincipal CustomUserDetails usuario) {
        return ResponseEntity.ok(usuarioService.obtenerPerfil(usuario));
    }

    @Operation(summary = "Actualizar foto de perfil", description = "Actualizaras foto de perfil indicadole desde tus archivos, deberas de autenticarte con el token de acesso.")
    @PatchMapping(value = "/foto" ,consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<UsuarioResponse> actualizarFotoPerfil(@AuthenticationPrincipal CustomUserDetails usuario, @RequestParam("imagen") MultipartFile file) {
        return ResponseEntity.ok(usuarioService.actualizarFotoPerfil(usuario, file));
    }

    @Operation(summary = "Actualizar datos de perfil", description = "Actualizaras los datos del perfil. Para usuarios autenticados.")
    @PatchMapping
    public ResponseEntity<UsuarioResponse> actualizarDatos (@AuthenticationPrincipal CustomUserDetails usuario,@RequestBody PerfilUpdate request){
        return ResponseEntity.ok(usuarioService.actualizarDatosPerfil(usuario, request));
    }
}
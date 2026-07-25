package com.api.biblioteca.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import com.api.biblioteca.configurations.CustomUserDetails;
import com.api.biblioteca.dtos.response.UsuarioResponse;
import com.api.biblioteca.services.UsuarioService;
import lombok.RequiredArgsConstructor;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;


@RestController
@RequestMapping("/api/usuarios/perfil")
@RequiredArgsConstructor
public class PerfilController {

    private final UsuarioService usuarioService;

    @GetMapping
    public ResponseEntity<UsuarioResponse> obtenerPerfil(@AuthenticationPrincipal CustomUserDetails usuario) {
        return ResponseEntity.ok(usuarioService.obtenerPerfil(usuario));
    }

    @PostMapping(value = "/foto" ,consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<UsuarioResponse> actualizarFotoPerfil(
        @AuthenticationPrincipal CustomUserDetails usuario,@RequestParam("file") MultipartFile file
    ) {
        return ResponseEntity.ok(usuarioService.actulizarFotoPerfil(usuario, file));
    }
    
    
}
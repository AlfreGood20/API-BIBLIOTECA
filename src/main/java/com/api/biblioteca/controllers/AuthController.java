package com.api.biblioteca.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.api.biblioteca.dtos.request.LoginRequest;
import com.api.biblioteca.dtos.request.UsuarioPublicRequest;
import com.api.biblioteca.dtos.response.TokenResponse;
import com.api.biblioteca.dtos.response.UsuarioResponse;
import com.api.biblioteca.services.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("api/auth")
@RequiredArgsConstructor
@Tag(name = "Autenticación", description = "Endpoints públicos de login, registro y manejo de sesión")
public class AuthController {

    private final AuthService authService;

    

    /* PUBLICOS */
    @Operation(summary = "Iniciar session", description = "Te autenticaras especificando correo y contraseña. Y obtendras un token de acceso para endpoints protegidos.", security = {})
    @PostMapping("/login")
    public ResponseEntity<TokenResponse> autenticarse(@Valid @RequestBody LoginRequest request, HttpServletResponse response) {
        return ResponseEntity.ok(authService.iniciarSession(request, response));
    }

    @Operation(summary = "Refrescar token de acesso", description = "Obtendras nuevo token de acesso, invalido el token refresh anterior.", security = {})
    @PostMapping("/refresh")
    public ResponseEntity<TokenResponse> refrescarToken(HttpServletRequest request, HttpServletResponse response) {
        return ResponseEntity.ok(authService.refrescarToken(request, response));
    }

    @Operation(summary = "Registrarse", description = "Te registrara para entrar al sistema, indicandole los datos requeridos.", security = {})
    @PostMapping("/register")
    public ResponseEntity<UsuarioResponse> registrarse(@Valid @RequestBody UsuarioPublicRequest request) {
        return new ResponseEntity<UsuarioResponse>(authService.registrarse(request), HttpStatus.CREATED);
    }


    /* DEBERA ESTAR AUTENTICADO*/
    @Operation(summary = "Cerrar sesión", description = "Invalida el refresh token actual y limpia la cookie de sesión.")
    @PostMapping("/logout")
    public ResponseEntity<Void> salirSession(HttpServletRequest request, HttpServletResponse response) {
        authService.salirSession(request, response);
        return ResponseEntity.noContent().build();
    }
}
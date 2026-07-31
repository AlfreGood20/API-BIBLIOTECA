package com.api.biblioteca.controllers;

import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.api.biblioteca.configurations.CustomUserDetails;
import com.api.biblioteca.dtos.response.PrestamoResponse;
import com.api.biblioteca.services.PrestamoService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
@RequestMapping("api/prestamos")
@RequiredArgsConstructor
@Tag(name = "Prestamos", description = "Operaciones prestamos de libros.")
public class PrestamoController {

    private final PrestamoService prestamoService;

    @GetMapping("/usuario")
    public ResponseEntity<List<PrestamoResponse>> misPrestamos(@AuthenticationPrincipal CustomUserDetails usuario){
        return ResponseEntity.ok(prestamoService.misPrestamos(usuario));
    } 
}

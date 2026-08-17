package com.api.biblioteca.controllers;

import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.api.biblioteca.configurations.CustomUserDetails;
import com.api.biblioteca.dtos.response.PaginaResponse;
import com.api.biblioteca.dtos.response.PrestamoResponse;
import com.api.biblioteca.enums.EstadoPrestamoNombre;
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
    public ResponseEntity<PaginaResponse<PrestamoResponse>> misPrestamos(
        @AuthenticationPrincipal CustomUserDetails usuario, 
        @RequestParam(required = false) EstadoPrestamoNombre estado,
        @ParameterObject 
        @PageableDefault(page = 0, sort = "fechaLimite", direction = Direction.DESC)
        Pageable pageable
    ){
        return ResponseEntity.ok(prestamoService.misPrestamos(usuario, estado, pageable));
    } 
}

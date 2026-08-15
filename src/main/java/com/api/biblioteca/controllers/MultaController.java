package com.api.biblioteca.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.api.biblioteca.configurations.CustomUserDetails;
import com.api.biblioteca.dtos.response.MultaResponse;
import com.api.biblioteca.dtos.response.PaginaResponse;
import com.api.biblioteca.enums.EstadoMultaNombre;
import com.api.biblioteca.services.MultaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@Tag(name = "Multas", description = "Operaciones para multas de prestamos.")
@RequestMapping("/api/multas")
@RequiredArgsConstructor
public class MultaController {

    private final MultaService multaService;

    @Operation(summary = "Obtener mis multas", description = "Obteienes tus multas por diferentes filtros. Solo para usuarios.")
    @GetMapping("/usuario")
    public ResponseEntity<PaginaResponse<MultaResponse>> misMultas(
        @AuthenticationPrincipal CustomUserDetails usuario,
        @RequestParam(required = false) EstadoMultaNombre estado,
        @ParameterObject
        @PageableDefault(page = 0, sort = "diasRetraso", direction = Direction.ASC)
        Pageable pageable
    ) {
        return ResponseEntity.ok(multaService.misMultas(usuario, estado, pageable));
    }
    
}

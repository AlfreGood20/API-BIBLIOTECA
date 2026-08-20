package com.api.biblioteca.controllers;

import java.util.List;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.api.biblioteca.configurations.CustomUserDetails;
import com.api.biblioteca.dtos.request.PrestamoRequest;
import com.api.biblioteca.dtos.response.PaginaResponse;
import com.api.biblioteca.dtos.response.PrestamoResponse;
import com.api.biblioteca.enums.EstadoPrestamoNombre;
import com.api.biblioteca.services.PrestamoService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("api/prestamos")
@RequiredArgsConstructor
@Tag(name = "Prestamos", description = "Operaciones prestamos de libros.")
public class PrestamoController {

    private final PrestamoService prestamoService;

    /* ================================ PARA APP BIBLIOTECA ========================================== */
    @PostMapping("/bibliotecario")
    public ResponseEntity<List<PrestamoResponse>> crearNuevo(@RequestBody PrestamoRequest request,@AuthenticationPrincipal CustomUserDetails usuarioAdmin) {
        return new ResponseEntity<List<PrestamoResponse>>(prestamoService.crearNuevo(request, usuarioAdmin), HttpStatus.CREATED);
    }

    @GetMapping("/bibliotecario/{id}")
    public ResponseEntity<PrestamoResponse> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(prestamoService.obtenerPrestamoPorId(id));
    }
    
    @PatchMapping("/bibliotecario/{id}")
    public ResponseEntity<PrestamoResponse> marcarDevolverPorId(@PathVariable Long id){
        return ResponseEntity.ok(prestamoService.devolverPrestamoPorId(id));
    }

    @GetMapping("/bibliotecario")
    public ResponseEntity<PaginaResponse<PrestamoResponse>> obtenerPrestamos(
        @RequestParam(required = false) EstadoPrestamoNombre estado,
        @RequestParam(required = false) Long usuarioAdminId,
        @RequestParam(required = false) Long usuarioId,
        @ParameterObject @PageableDefault(page = 0, size = 10, sort = "id", direction = Direction.ASC)
        Pageable pageable
    ) {
        return ResponseEntity.ok(prestamoService.obtenerPrestamos(estado, usuarioAdminId, usuarioId, pageable));
    }
    

    

    /* =================================PARA APP WEB RESERVAS ========================================= */
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

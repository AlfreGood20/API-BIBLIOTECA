package com.api.biblioteca.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.api.biblioteca.dtos.request.UsuarioRequest;
import com.api.biblioteca.dtos.response.MultaResponse;
import com.api.biblioteca.dtos.response.PrestamoResponse;
import com.api.biblioteca.dtos.response.ReservaResponse;
import com.api.biblioteca.dtos.response.UsuarioResponse;
import com.api.biblioteca.dtos.updates.EstadoRequest;
import com.api.biblioteca.enums.EstadoUsuarioNombre;
import com.api.biblioteca.enums.RolNombre;
import com.api.biblioteca.services.UsuarioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PutMapping;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;

    @PostMapping("/")
    public ResponseEntity<UsuarioResponse> crearNuevo(@RequestBody @Valid UsuarioRequest request) {
        return new ResponseEntity<UsuarioResponse>(usuarioService.crearNuevo(request), HttpStatus.CREATED);
    }

    @GetMapping("/")
    public ResponseEntity<List<UsuarioResponse>> obtenerUsuarios() {
        return ResponseEntity.ok().body(usuarioService.obtenerUsuarios());
    }

    @GetMapping("/{id}")
    public ResponseEntity<UsuarioResponse> obtenerUsuarioPorId(@PathVariable Long id) {
        return ResponseEntity.ok().body(usuarioService.obtenerPorId(id));
    }
    
    @PatchMapping("/{id}/estado")
    public ResponseEntity<UsuarioResponse> cambiarEstadoUsuario(@PathVariable Long id, @RequestBody EstadoRequest request){
        return ResponseEntity.ok().body(usuarioService.cambiarEstadoUsuario(id, request));
    }

    @GetMapping
    public ResponseEntity<List<UsuarioResponse>> buscarUsuarioPorParametros(
        @RequestParam(required = false) String nombre,
        @RequestParam(required = false) RolNombre rol,
        @RequestParam(required = false) EstadoUsuarioNombre estado 
    ) {
        return ResponseEntity.ok().body(usuarioService.obtenerUsuariosPorParametros(nombre, rol, estado));
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<UsuarioResponse> actualizarUsuarioPorId(@PathVariable Long id, @RequestBody String entity) {
        return ResponseEntity.ok().body(usuarioService.actulizarDatos(id)); // POR COMPLETAR AUN
    }

    @GetMapping("/{id}/prestamos")
    public ResponseEntity<List<PrestamoResponse>> obtenerPrestamosDeUnUsuario(@PathVariable Long id) {
        return ResponseEntity.ok().body(usuarioService.prestamosPorUsuario(id));
    }

    @GetMapping("/{id}/multas")
    public ResponseEntity<List<MultaResponse>> obtenerMultasDeUnUsuario(@PathVariable Long id) {
        return ResponseEntity.ok().body(usuarioService.multasPorUsuario(id));
    }

    @GetMapping("/{id}/reservas")
    public ResponseEntity<List<ReservaResponse>> getMethodName(@PathVariable Long id) {
        return ResponseEntity.ok().body(usuarioService.reservasPorUsuario(id));
    }
}
package com.api.biblioteca.configurations;

import java.time.LocalDate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import com.api.biblioteca.enums.EstadoUsuarioNombre;
import com.api.biblioteca.enums.RolNombre;
import com.api.biblioteca.exceptions.ResourceNotFoundException;
import com.api.biblioteca.models.Credencial;
import com.api.biblioteca.models.Usuario;
import com.api.biblioteca.repositorys.CredencialRepository;
import com.api.biblioteca.repositorys.EstadoUsuarioRepository;
import com.api.biblioteca.repositorys.RolRepository;
import com.api.biblioteca.repositorys.UsuarioRepository;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class AdminSeeder implements CommandLineRunner{

    private final UsuarioRepository usuarioRepository;
    private final CredencialRepository credencialRepository;
    private final RolRepository rolRepository;
    private final EstadoUsuarioRepository estadoUsuarioRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${app.admin.correo}")
    private String CORREO_ADMIN;

    @Value("${app.admin.contrasena}")
    private String CONTRASENA_ADMIN;

    @Override
    public void run(String... args) throws Exception {

        if(credencialRepository.existsByCorreo(CORREO_ADMIN)){
            return;
        }

        Credencial credencial = Credencial.builder()
            .correo(CORREO_ADMIN)
            .contrasena(passwordEncoder.encode(CONTRASENA_ADMIN))
            .build();

        Usuario admin = Usuario.builder()
            .nombre("SUPER")
            .apellidoPaterno("ADMIN")
            .apellidoMaterno("ONE")
            .fechaNacimiento(LocalDate.of(2006, 1, 4))
            .genero("Otro")
            .rol(rolRepository.findByNombre(RolNombre.ADMINISTRADOR)
                .orElseThrow(() -> new ResourceNotFoundException("Rol ADMINISTRADOR no encontrado."))
            )
            .estado(estadoUsuarioRepository.findByNombre(EstadoUsuarioNombre.ACTIVO)
                .orElseThrow(() -> new ResourceNotFoundException("Estado usuario ACTIVO no encontrado."))
            )
            .credencial(credencial)
            .build();

        credencial.setUsuario(admin);
            
        usuarioRepository.save(admin);
    }

}

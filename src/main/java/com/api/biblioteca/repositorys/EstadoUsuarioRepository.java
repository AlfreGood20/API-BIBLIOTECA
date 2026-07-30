package com.api.biblioteca.repositorys;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import com.api.biblioteca.models.EstadoUsuario;
import com.api.biblioteca.enums.EstadoUsuarioNombre;


public interface EstadoUsuarioRepository extends JpaRepository<EstadoUsuario, Long> {

    Optional<EstadoUsuario> findByNombre(EstadoUsuarioNombre nombre);
}

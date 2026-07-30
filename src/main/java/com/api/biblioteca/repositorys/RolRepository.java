package com.api.biblioteca.repositorys;

import org.springframework.data.jpa.repository.JpaRepository;
import com.api.biblioteca.models.Rol;
import java.util.Optional;
import com.api.biblioteca.enums.RolNombre;


public interface RolRepository extends JpaRepository<Rol, Long> {
    Optional<Rol> findByNombre(RolNombre nombre);
}

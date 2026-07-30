package com.api.biblioteca.repositorys;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import com.api.biblioteca.models.EstadoMulta;
import com.api.biblioteca.enums.EstadoMultaNombre;



public interface EstadoMultaRepository extends JpaRepository<EstadoMulta, Long> {

    Optional<EstadoMulta> findByNombre(EstadoMultaNombre nombre);
}

package com.api.biblioteca.repositorys;

import org.springframework.data.jpa.repository.JpaRepository;
import com.api.biblioteca.models.EstadoEjemplar;
import java.util.Optional;
import com.api.biblioteca.enums.EstadoEjemplarNombre;


public interface EstadoEjemplarRepository extends JpaRepository<EstadoEjemplar, Long> {

    Optional<EstadoEjemplar> findByNombre(EstadoEjemplarNombre nombre);
}

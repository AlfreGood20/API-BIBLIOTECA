package com.api.biblioteca.repositorys;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import com.api.biblioteca.enums.EstadoReservaNombre;
import com.api.biblioteca.models.EstadoReserva;

public interface EstadoReservaRepository extends JpaRepository<EstadoReserva, Long> {

    Optional<EstadoReserva> findByNombre(EstadoReservaNombre nombre);
}

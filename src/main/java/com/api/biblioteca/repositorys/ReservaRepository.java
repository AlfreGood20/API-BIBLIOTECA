package com.api.biblioteca.repositorys;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.api.biblioteca.models.Reserva;
import com.api.biblioteca.models.Usuario;

public interface ReservaRepository extends JpaRepository<Reserva, Long> {

    List<Reserva> findByUsuarioOrderByFechaReservaDesc(Usuario usuario);

    @Query("""
        SELECT r FROM Reserva r
        WHERE (:estadoId IS NULL OR r.estado.id = :estadoId)
        """)
    List<Reserva> buscarPorParametros(@Param("estadoId") Long estadoId);
}
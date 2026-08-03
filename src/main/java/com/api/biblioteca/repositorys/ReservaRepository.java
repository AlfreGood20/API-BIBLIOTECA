package com.api.biblioteca.repositorys;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.api.biblioteca.enums.EstadoReservaNombre;
import com.api.biblioteca.models.Reserva;
import com.api.biblioteca.models.Usuario;

public interface ReservaRepository extends JpaRepository<Reserva, Long> {

    @Query("""
        SELECT r FROM Reserva r
        WHERE r.usuario = :usuario
        AND (:estado IS NULL OR r.estado.nombre = :estado)
        ORDER BY r.fechaReserva DESC
        """)
    List<Reserva> buscarMisReservas(@Param("usuario") Usuario usuario, @Param("estado") EstadoReservaNombre estado);

    @Query("""
        SELECT r FROM Reserva r
        WHERE (:estadoId IS NULL OR r.estado.id = :estadoId)
        """)
    List<Reserva> buscarPorParametros(@Param("estadoId") Long estadoId);
}
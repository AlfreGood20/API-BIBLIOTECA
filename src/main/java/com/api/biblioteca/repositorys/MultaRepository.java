package com.api.biblioteca.repositorys;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.api.biblioteca.models.EstadoMulta;
import com.api.biblioteca.models.Multa;

public interface MultaRepository extends JpaRepository<Multa, Long> {
    // MultaRepository
    @Query("""
        SELECT m FROM Multa m
        WHERE (:estadoId IS NULL OR m.estado.id = :estadoId)
        """)
    List<Multa> buscarPorParametros(@Param("estadoId") Long estadoId);

    List<Multa> findByEstado(EstadoMulta estado);
}
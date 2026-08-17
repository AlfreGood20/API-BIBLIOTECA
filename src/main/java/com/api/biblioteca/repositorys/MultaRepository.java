package com.api.biblioteca.repositorys;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.api.biblioteca.enums.EstadoMultaNombre;
import com.api.biblioteca.models.EstadoMulta;
import com.api.biblioteca.models.Multa;
import com.api.biblioteca.models.Usuario;

public interface MultaRepository extends JpaRepository<Multa, Long> {
    
    @Query("""
        SELECT m FROM Multa m
        WHERE (:estadoId IS NULL OR m.estado.id = :estadoId)
        """)
    List<Multa> buscarPorParametros(@Param("estadoId") Long estadoId);

    @Query("""
        SELECT COUNT(m) FROM Multa m
        WHERE m.prestamo.usuario = :usuario
        AND m.estado = :estado
        """)
    long countByUsuarioAndEstado(@Param("usuario") Usuario usuario, @Param("estado") EstadoMulta estado);
    
    List<Multa> findByEstado(EstadoMulta estado);

    @Query(
        """
        SELECT m FROM Multa m
        WHERE m.prestamo.usuario = :usuario
        AND (:estado IS NULL OR m.estado.nombre = :estado)       
        """
    )
    Page<Multa> bsucarMisReservas(@Param("usuario") Usuario usuario,@Param("estado") EstadoMultaNombre estado, Pageable pageable);
}
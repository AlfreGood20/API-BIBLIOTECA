package com.api.biblioteca.repositorys;

import java.time.LocalDate;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.api.biblioteca.enums.EstadoPrestamoNombre;
import com.api.biblioteca.models.EstadoPrestamo;
import com.api.biblioteca.models.Prestamo;
import com.api.biblioteca.models.Usuario;

public interface PrestamoRepository extends JpaRepository<Prestamo, Long> {

    @EntityGraph(attributePaths = {"ejemplar", "estado", "usuarioAdmin"})
    @Query("""
        SELECT p FROM Prestamo p
        WHERE p.usuario = :usuario
        AND (:estado IS NULL OR p.estado.nombre = :estado )
        """)
    Page<Prestamo> misPrestamos(
        @Param("usuario") Usuario usuario,
        @Param("estado") EstadoPrestamoNombre estado,
        Pageable Pageable
    );

    @EntityGraph(attributePaths = {"ejemplar", "estado", "usuarioAdmin"})
    @Query("""
            SELECT p FROM Prestamo p
            WHERE (:estado IS NULL OR p.estado.nombre = :estado)
            AND (:usuarioAdminId IS NULL OR p.usuarioAdmin.id = :usuarioAdminId)
            AND (:usuarioId IS NULL OR p.usuario.id = :usuarioId)
            """)
    Page<Prestamo> prestamosPorFiltro(
        @Param("estado") EstadoPrestamoNombre estado, 
        @Param("usuarioAdminId") Long usuarioAdminId, 
        @Param("usuarioId") Long usuarioId, 
        Pageable pageable
    );


    List<Prestamo> findByUsuario(Usuario usuario);

    List<Prestamo> findByEstadoAndFechaLimiteBefore(EstadoPrestamo estado, LocalDate fecha);

    List<Prestamo> findByEstado_Nombre(EstadoPrestamoNombre estado);
}
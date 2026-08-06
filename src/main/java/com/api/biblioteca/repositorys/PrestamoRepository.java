package com.api.biblioteca.repositorys;

import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.api.biblioteca.enums.EstadoPrestamoNombre;
import com.api.biblioteca.models.EstadoPrestamo;
import com.api.biblioteca.models.Prestamo;
import com.api.biblioteca.models.Usuario;

public interface PrestamoRepository extends JpaRepository<Prestamo, Long> {

    List<Prestamo> findByUsuario(Usuario usuario);

    long countByUsuarioAndMultaIsNotNull(Usuario usuario);

    @Query("""
        SELECT p FROM Prestamo p
        WHERE (:estadoId IS NULL OR p.estado.id = :estadoId)
        AND (:usuarioAdminId IS NULL OR p.usuarioAdmin.id = :usuarioAdminId)
        AND (:usuarioId IS NULL OR p.usuario.id = :usuarioId)
        """)
    List<Prestamo> buscarPorParametros(
        @Param("estadoId") Long estadoId,
        @Param("usuarioAdminId") Long usuarioAdminId,
        @Param("usuarioId") Long usuarioId
    );

    List<Prestamo> findByEstadoAndFechaLimiteBefore(EstadoPrestamo estado, LocalDate fecha);

    List<Prestamo> findByEstado_Nombre(EstadoPrestamoNombre estado);
}
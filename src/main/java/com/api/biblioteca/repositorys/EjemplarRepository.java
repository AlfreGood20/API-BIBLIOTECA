package com.api.biblioteca.repositorys;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.api.biblioteca.models.Ejemplar;
import com.api.biblioteca.models.EstadoEjemplar;
import com.api.biblioteca.models.Libro;


public interface EjemplarRepository extends JpaRepository<Ejemplar, Long> {

    Optional<Ejemplar> findFirstByLibroAndEstadoOrderByIdAsc(Libro libro, EstadoEjemplar estado);

    @Query("""
        SELECT e FROM Ejemplar e
        WHERE (:libroId IS NULL OR e.libro.id = :libroId)
        AND (:codigo IS NULL OR LOWER(e.codigo) LIKE LOWER(CONCAT('%', :codigo, '%')))
        AND (:estadoId IS NULL OR e.estado.id = :estadoId)
    """)
    List<Ejemplar> findByFiltros(
        @Param("libroId") Long libroId,
        @Param("codigo") String codigo,
        @Param("estadoId") Long estadoId
    );
}

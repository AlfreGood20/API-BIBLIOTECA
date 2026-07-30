package com.api.biblioteca.repositorys;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.api.biblioteca.models.Autor;


public interface AutorRepository extends JpaRepository<Autor, Long> {

    // AutorRepository.java
    @Query("""
        SELECT a FROM Autor a
        WHERE (:nombre IS NULL OR LOWER(a.nombre) LIKE LOWER(CONCAT('%', :nombre, '%')))
        AND (:apellidoPaterno IS NULL OR LOWER(a.apellidoPaterno) LIKE LOWER(CONCAT('%', :apellidoPaterno, '%')))
        AND (:apellidoMaterno IS NULL OR LOWER(a.apellidoMaterno) LIKE LOWER(CONCAT('%', :apellidoMaterno, '%')))
        AND (:nacionalidadId IS NULL OR a.nacionalidad.id = :nacionalidadId)
    """)
    List<Autor> findByFiltros(
        @Param("nombre") String nombre,
        @Param("apellidoPaterno") String apellidoPaterno,
        @Param("apellidoMaterno") String apellidoMaterno,
        @Param("nacionalidadId") Long nacionalidadId);
}

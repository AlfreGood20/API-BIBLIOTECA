package com.api.biblioteca.repositorys;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.api.biblioteca.models.Libro;

public interface LibroRepository extends JpaRepository<Libro, Long> {

    // LibroRepository.java
    @Query("""
        SELECT l FROM Libro l
        WHERE (:titulo IS NULL OR LOWER(l.titulo) LIKE LOWER(CONCAT('%', :titulo, '%')))
        AND (:isbn IS NULL OR l.isbn = :isbn)
        AND (:categoriaId IS NULL OR l.categoria.id = :categoriaId)
        AND (:editorialId IS NULL OR l.editorial.id = :editorialId)
        AND (:idiomaId IS NULL OR l.idioma.id = :idiomaId)
    """)
    List<Libro> findByFiltros(
        @Param("titulo") String titulo,
        @Param("isbn") String isbn,
        @Param("categoriaId") Long categoriaId,
        @Param("editorialId") Long editorialId,
        @Param("idiomaId") Long idiomaId
    );
}

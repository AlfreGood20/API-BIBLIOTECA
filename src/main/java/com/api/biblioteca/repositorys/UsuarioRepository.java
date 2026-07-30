package com.api.biblioteca.repositorys;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.api.biblioteca.models.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    @Query(value = """
        SELECT * FROM usuarios u
        WHERE (:nombre IS NULL OR LOWER(u.nombre) LIKE LOWER(CONCAT('%', :nombre, '%')))
        AND (:rol IS NULL OR u.fk_rol = :rol)
        AND (:estado IS NULL OR u.fk_estado = :estado)
    """, 
    nativeQuery = true)
    List<Usuario> buscarPorParametros(@Param("nombre") String nombre,@Param("rol") String rol,@Param("estado") String estado);
}

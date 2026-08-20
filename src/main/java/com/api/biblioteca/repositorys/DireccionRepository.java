package com.api.biblioteca.repositorys;

import org.springframework.data.jpa.repository.JpaRepository;
import com.api.biblioteca.models.Direccion;
import com.api.biblioteca.models.Usuario;
import java.util.Optional;



public interface DireccionRepository extends JpaRepository<Direccion, Long> {

    Optional<Direccion> findByUsuario(Usuario usuario);
}

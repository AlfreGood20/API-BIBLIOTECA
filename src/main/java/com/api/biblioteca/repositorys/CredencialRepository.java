package com.api.biblioteca.repositorys;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import com.api.biblioteca.models.Credencial;


public interface CredencialRepository extends JpaRepository<Credencial, Long> {
    boolean existsByCorreo(String correo);
    Optional<Credencial> findByCorreo(String correo);
}

package com.api.biblioteca.repositorys;

import org.springframework.data.jpa.repository.JpaRepository;
import com.api.biblioteca.models.Telefono;

public interface TelefonoRepository extends JpaRepository<Telefono, Long> {

}

package com.api.biblioteca.repositorys;

import org.springframework.data.jpa.repository.JpaRepository;
import com.api.biblioteca.models.Direccion;


public interface DireccionRepository extends JpaRepository<Direccion, Long> {

}

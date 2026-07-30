package com.api.biblioteca.repositorys;

import org.springframework.data.jpa.repository.JpaRepository;
import com.api.biblioteca.models.Municipio;

public interface MunicipioRepository extends JpaRepository<Municipio, Long> {

}

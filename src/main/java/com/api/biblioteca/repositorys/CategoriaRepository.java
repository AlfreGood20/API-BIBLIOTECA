package com.api.biblioteca.repositorys;

import org.springframework.data.jpa.repository.JpaRepository;
import com.api.biblioteca.models.Categoria;


public interface CategoriaRepository extends JpaRepository<Categoria, Long> {

}

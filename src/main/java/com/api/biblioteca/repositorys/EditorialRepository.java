package com.api.biblioteca.repositorys;

import org.springframework.data.jpa.repository.JpaRepository;
import com.api.biblioteca.models.Editorial;


public interface EditorialRepository extends JpaRepository<Editorial, Long> {

}

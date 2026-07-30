package com.api.biblioteca.repositorys;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import com.api.biblioteca.models.Token;

public interface TokenRepository extends JpaRepository<Token, Long>{

    Optional<Token> findByTokenRefresh(String tokenRefresh);

}
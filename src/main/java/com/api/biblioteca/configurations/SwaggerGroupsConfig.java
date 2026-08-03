package com.api.biblioteca.configurations;

import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerGroupsConfig {

    @Bean
    public GroupedOpenApi autenticacionUsuarioApi(){
        return GroupedOpenApi.builder()
            .group("Autenticación y usuario")
            .pathsToMatch(
                "/api/auth/**",
                "/api/perfil/**",
                "/api/municipios/**"
            )
            .build();
    }

    @Bean
    public GroupedOpenApi libroApi(){
        return GroupedOpenApi.builder()
            .group("Libro y complementos")
            .pathsToMatch(
                "/api/libros/**",
                "/api/categorias/**",
                "/api/editoriales/**",
                "/api/idiomas/**",
                "/api/autores/**",
                "/api/nacionalidades/**"
            )
            .build();
    }

    @Bean
    public GroupedOpenApi circulacionApi(){
        return GroupedOpenApi.builder()
            .group("Circulación")
            .pathsToMatch(
                "/api/prestamos/**",
                "/api/multas/**",
                "/api/reservas/**",
                "/api/ejemplares/**",
                "/api/estados/**"
            )
            .build();
    }

    @Bean
    public GroupedOpenApi administracion(){
        return GroupedOpenApi.builder()
            .group("Administración de usuarios")
            .pathsToMatch("/api/usuarios/**")
            .build();
    }

}
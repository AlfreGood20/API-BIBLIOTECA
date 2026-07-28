package com.api.biblioteca.configurations;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@OpenAPIDefinition(
    info = @Info(
        title = "API - BIBLIOTECA",
        version = "1.0.0",
        description = "",
        contact = @Contact(name = "José Alfredo", email = "josealfredolopezdelacruz2@gmail.com")
    ),
    servers = {@Server(url = "http://localhost:8080/", description = "ENTORNO LOCAL")},

    security = @SecurityRequirement(name = "Bearer")
)

@SecurityScheme(
    name = "Bearer",
    type = SecuritySchemeType.HTTP,
    scheme = "bearer",
    bearerFormat = "JWT"
)

@Configuration
@RequiredArgsConstructor
@Slf4j
public class ConfigMain implements WebMvcConfigurer{

    private final CustomUserDetailsService userDetailsService;

    @Value("${app.upload.dir}")
    private String uploads;

    @Bean
    public Path uploadPath () throws IOException{
        Path obtenerDiretorio =  Paths.get(uploads);
            
            if(!Files.exists(obtenerDiretorio)){
                Files.createDirectory(obtenerDiretorio);
            }
        return obtenerDiretorio;
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations("file:" + uploads + "/");
    }

    @Bean
    public PasswordEncoder passwordEncoder (){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager (AuthenticationConfiguration configuracion) throws Exception{
        return configuracion.getAuthenticationManager();
    }

    @Bean
    public AuthenticationProvider authenticationProvider (){
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());

        return authProvider;
    }

}
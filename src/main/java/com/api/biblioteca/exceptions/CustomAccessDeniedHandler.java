package com.api.biblioteca.exceptions;

import java.io.IOException;
import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class CustomAccessDeniedHandler implements AccessDeniedHandler{

    private final ObjectMapper objectMapper;

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {

        response.setContentType("application/json");
        response.setStatus(HttpStatus.FORBIDDEN.value());

        ResponseExeption responseHtpp = ResponseExeption.builder()
            .status(403)
            .error(HttpStatus.FORBIDDEN.getReasonPhrase())
            .menssaje("Acceso denegado a este recurso.")
            .uri(request.getRequestURI())
            .timestamp(LocalDateTime.now())
            .build();

        response.getWriter().write(objectMapper.writeValueAsString(responseHtpp));
    }
}
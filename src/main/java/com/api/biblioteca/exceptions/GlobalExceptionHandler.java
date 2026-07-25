package com.api.biblioteca.exceptions;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.http.HttpServletRequest;

@RestControllerAdvice
public class GlobalExceptionHandler {

    //400 VALIDACION DE NEGOCIO
    @ExceptionHandler(BusinessExeption.class)
    public ResponseEntity<?> busineesProblem(BusinessExeption ex, HttpServletRequest request){

        ResponseExeption response = ResponseExeption.builder()
            .status(HttpStatus.BAD_REQUEST.value())
            .error("Error de validacion")
            .menssaje(ex.getMessage())
            .timestamp(LocalDateTime.now())
            .build();


        return ResponseEntity.badRequest().body(response);
    }

    // 401 NO ENVIO DATO IMPORTANTE (TOKENS ETC)
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<?> llegaArgumentIsEmpety(HttpServletRequest request, IllegalArgumentException ex){

        ResponseExeption response = ResponseExeption.builder()
            .status(HttpStatus.UNAUTHORIZED.value())
            .error("Dato no recibidos")
            .menssaje(ex.getMessage())
            .uri(request.getRequestURI())
            .timestamp(LocalDateTime.now())
            .build();

        return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
    }

    // 401 TOKEN ESTA EXPIRADO
    @ExceptionHandler(ExpiredJwtException.class)
    public ResponseEntity<?> tokenIsExpired(HttpServletRequest request){
        
        ResponseExeption response = ResponseExeption.builder()
            .status(HttpStatus.UNAUTHORIZED.value())
            .error("Token expirado")
            .menssaje("Token recibido se encuntra expirado.")
            .uri(request.getRequestURI())
            .timestamp(LocalDateTime.now())
            .build();

        return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
    }

    // 401 TOKEN MALFORMADO, ESTRUCTURA DEL TOKEN ESTA MAL
    @ExceptionHandler(MalformedJwtException.class)
    public ResponseEntity<?> tokenMalformed(HttpServletRequest request){
        
        ResponseExeption response = ResponseExeption.builder()
            .status(HttpStatus.UNAUTHORIZED.value())
            .error("Token malformado")
            .menssaje("Token recibido esta malformado. No cumple con la estructura.")
            .uri(request.getRequestURI())
            .timestamp(LocalDateTime.now())
            .build();

        return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
    }

    // 401 TOKEN NO COINCIDE CON LA FIRMA DEL SERVIDOR
    @ExceptionHandler(SignatureException.class)
    public ResponseEntity<?> tokenSignature(HttpServletRequest request){
        
        ResponseExeption response = ResponseExeption.builder()
            .status(HttpStatus.UNAUTHORIZED.value())
            .error("Token no coincide")
            .menssaje("Token enviado no coincide con la clave secreta valida del servidor.")
            .uri(request.getRequestURI())
            .timestamp(LocalDateTime.now())
            .build();

        return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
    }

    // 401 FORMATO DEL TOKEN NO ES COMPATIBLE O UTILIZA UN ALGORITMO NO SOPORTADO
    @ExceptionHandler(UnsupportedJwtException.class)
    public ResponseEntity<?> tokenUnsupported(HttpServletRequest request){
        
        ResponseExeption response = ResponseExeption.builder()
            .status(HttpStatus.UNAUTHORIZED.value())
            .error("Token no compatible")
            .menssaje("Token enviado no es compatible y/o utiliza otro algoritmo no soportado.")
            .uri(request.getRequestURI())
            .timestamp(LocalDateTime.now())
            .build();

        return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(CredentialsExpiredException.class)
    public ResponseEntity<?> credentialsExpired(CredentialsExpiredException ex, HttpServletRequest request){

        ResponseExeption response = ResponseExeption.builder()
            .status(HttpStatus.UNAUTHORIZED.value())
            .error("Credenciales invalida")
            .menssaje(ex.getMessage())
            .uri(request.getRequestURI())
            .timestamp(LocalDateTime.now())
            .build();

        return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<?> badCredentials(HttpServletRequest request) {

        ResponseExeption response = ResponseExeption.builder()
            .status(HttpStatus.UNAUTHORIZED.value())
            .error("Credenciales invalida")
            .menssaje("Correo y/o contraseña inavalidas.")
            .uri(request.getRequestURI())
            .timestamp(LocalDateTime.now())
            .build();
        
        return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
    }


    //400 VALIDACIONES DE FORMULARIO
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> badRequest(MethodArgumentNotValidException ex, HttpServletRequest request){

        Map<String, String> mensaje = new HashMap<>();
        ex.getFieldErrors().stream().forEach(error -> mensaje.put(error.getField(), error.getDefaultMessage()));

        ResponseExeption response = ResponseExeption.builder()
            .status(HttpStatus.BAD_REQUEST.value())
            .error("Datos invalidos")
            .menssaje(mensaje)
            .uri(request.getRequestURI())
            .timestamp(LocalDateTime.now())
            .build();

        return ResponseEntity.badRequest().body(response);
    }



    //404 PARA MANEJAR USERNAME NO ENCONTRADO (CORREO NO ENCONTRADO)
    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<?> usernameNotFound(UsernameNotFoundException ex, HttpServletRequest request){

        ResponseExeption response = ResponseExeption.builder()
            .status(HttpStatus.NOT_FOUND.value())
            .error("Correo no encontrado")
            .menssaje("Correo aun no se encuentra registrado en el sistema.")
            .uri(request.getRequestURI())
            .timestamp(LocalDateTime.now())
            .build();
            
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    //404
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<?> notFound(ResourceNotFoundException ex, HttpServletRequest request){

        ResponseExeption response = ResponseExeption.builder()
            .status(HttpStatus.NOT_FOUND.value())
            .error("Recurso No Encontrado")
            .menssaje(ex.getMessage())
            .uri(request.getRequestURI())
            .timestamp(LocalDateTime.now())
            .build();

        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    //405
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<?> methodNotAllowed(HttpServletRequest request){

        ResponseExeption response = ResponseExeption.builder()
            .status(HttpStatus.METHOD_NOT_ALLOWED.value())
            .error("Metodo no soportado")
            .menssaje("Este enpoint no soporta"+ request.getMethod())
            .uri(request.getRequestURI())
            .timestamp(LocalDateTime.now())
            .build();

        return new ResponseEntity<>(response, HttpStatus.METHOD_NOT_ALLOWED);
    }

    @ExceptionHandler(ConflictExeption.class)
    public ResponseEntity<?> conflict(ConflictExeption ex, HttpServletRequest request){
        
        ResponseExeption response = ResponseExeption.builder()
            .status(HttpStatus.CONFLICT.value())
            .error("Conflicto de recurso")
            .menssaje(ex.getMessage())
            .uri(request.getRequestURI())
            .timestamp(LocalDateTime.now())
            .build();

        return new ResponseEntity<>(response, HttpStatus.CONFLICT);
    }

    //500
    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> manejarErrorInesperado(Exception ex, HttpServletRequest request){

        ResponseExeption mensaje= ResponseExeption.builder()
            .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
            .error("Error inesperado")
            .menssaje(ex.getMessage())
            .uri(request.getRequestURI())
            .timestamp(LocalDateTime.now())
            .build();
        
        return ResponseEntity.internalServerError().body(mensaje);
    }

    //500
    @ExceptionHandler(IOException.class)
    public ResponseEntity<?> manejarErrorIO(HttpServletRequest request){

        ResponseExeption mensaje= ResponseExeption.builder()
            .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
            .error("Error inesperado")
            .menssaje("Ocurrio un error en el fichero uploads o otro tipo de error.")
            .uri(request.getRequestURI())
            .timestamp(LocalDateTime.now())
            .build();
        
        return ResponseEntity.internalServerError().body(mensaje);
    }


}
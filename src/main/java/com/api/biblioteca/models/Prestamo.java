package com.api.biblioteca.models;

import java.time.LocalDate;
import java.time.LocalDateTime;
import org.hibernate.annotations.CreationTimestamp;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "prestamos")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class Prestamo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pk_prestamo")
    private Long id;

    @CreationTimestamp
    @Column(name = "fecha_registro", updatable = false)
    private LocalDateTime fechaRegistro;

    @Column(name = "fecha_limite", nullable = false)
    private LocalDate fechaLimite;

    @Column(name = "fecha_devolucion")
    private LocalDate fechaDevolucion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_usuario", nullable = false)
    private Usuario usuario;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_usuario_admin")
    private Usuario usuarioAdmin;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "fk_estado", nullable = false)
    private EstadoPrestamo estado;

    @ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_ejemplar", nullable = false)
    private Ejemplar ejemplar;
    
}
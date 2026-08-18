package com.api.biblioteca.models;

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
@Table(name = "ejemplares")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Ejemplar {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pk_ejemplar")
    private Long id;

    @Column(nullable = false, unique = true)
    private String codigo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_libro", nullable = false)
    private Libro libro;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "fk_estado", nullable = false)
    private EstadoEjemplar estado;
}

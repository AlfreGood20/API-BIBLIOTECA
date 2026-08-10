package com.api.biblioteca.models;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "libros")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Libro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pk_libro")
    private Long id;

    @Column(nullable = false)
    private String titulo;

    @Column(length = 5000)
    private String sinopsis;

    @Column(nullable = false, unique = true)
    private String isbn;

    @Column(name = "numero_paginas",nullable = false)
    private int numeroPaginas;

    @Column(nullable = false)
    private int anio;

    @Column(name = "portada_url")
    private String portadaUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_categoria",nullable = false)
    private Categoria categoria;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_editorial",nullable = false)
    private Editorial editorial;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_idioma",nullable = false)
    private Idioma idioma;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "libro_autor",
        joinColumns = @JoinColumn(name = "fk_libro"),
        inverseJoinColumns = @JoinColumn(name = "fk_autor")
    )
    private List<Autor> autores;

    @OneToMany(mappedBy = "libro", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Ejemplar> ejemplares;
}

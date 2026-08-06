package com.api.biblioteca.models;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import org.hibernate.annotations.CreationTimestamp;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "multas")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Multa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pk_multa")
    private Long id;

    @Column(name = "dias_retraso", nullable = false)
    private int diasRetraso;

    @CreationTimestamp
    @Column(name = "fecha_registro", updatable = false)
    private LocalDateTime fechaRegistro;

    @Column(name = "fecha_pago")
    private LocalDateTime fechaPago;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal importe;

    @OneToOne
    @JoinColumn(name = "fk_prestamo", nullable = false, unique = true)
    private Prestamo prestamo;

    @ManyToOne
    @JoinColumn(name = "fk_estado")
    private EstadoMulta estado;
}

package com.api.biblioteca.services.impl;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.api.biblioteca.configurations.CustomUserDetails;
import com.api.biblioteca.dtos.response.MultaResponse;
import com.api.biblioteca.dtos.response.PaginaResponse;
import com.api.biblioteca.enums.EstadoMultaNombre;
import com.api.biblioteca.exceptions.BusinessExeption;
import com.api.biblioteca.exceptions.ResourceNotFoundException;
import com.api.biblioteca.mappers.MultaMapper;
import com.api.biblioteca.models.EstadoMulta;
import com.api.biblioteca.models.Multa;
import com.api.biblioteca.models.Prestamo;
import com.api.biblioteca.repositorys.EstadoMultaRepository;
import com.api.biblioteca.repositorys.MultaRepository;
import com.api.biblioteca.services.MultaService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class MultaServiceImpl implements MultaService{
    
    private final MultaRepository multaRepository;
    private final EstadoMultaRepository estadoMultaRepository;

    private final MultaMapper multaMapper;

    @Value("${app.multa-costo-por-dia}")
    private BigDecimal COSTO_POR_DIA;

    
    /* ================================= SERVICIOS PARA ADMINISTRADOR =============================== */
    @Override
    @Transactional
    public MultaResponse pagarMulta(Long id) {
        Multa multa = multaRepository.findById(id)
            .orElseThrow(()-> new ResourceNotFoundException("Multa no encontrada"));

        if(multa.getEstado().getNombre() != EstadoMultaNombre.PENDIENTE){
            throw new BusinessExeption("Esta multa se encuentra "+multa.getEstado().getNombre().toString());
        }

        multa.setFechaPago(LocalDateTime.now());
        multa.setEstado(null);

        EstadoMulta estadoPagado = estadoMultaRepository.findByNombre(EstadoMultaNombre.PAGADA)
            .orElseThrow(() -> new ResourceNotFoundException("Estado multa no encontrada"));

        multa.setEstado(estadoPagado);

        return multaMapper.entityToDto(multaRepository.save(multa));
    }

    @Override
    public MultaResponse obtenerMultaPorId(Long id) {
        return multaMapper.entityToDto(multaRepository.findById(id)
            .orElseThrow(()-> new ResourceNotFoundException("Multa no encontrada.")));
    }

    @Override
    public List<MultaResponse> obtenerMultas(Long estadoId) {
        return multaMapper.listEntityToListDto(multaRepository.buscarPorParametros(estadoId));
    }



    /* ============================================== SERVICIO PARA EL SCHEDULED =========================================== */
    @Override
    @Transactional
    public List<Multa> generarMultas(List<Prestamo> prestamosVencidos) {
        log.info("GENERANDO MULTAS");

        EstadoMulta estado = buscarEstadoPorNombre(EstadoMultaNombre.PENDIENTE);

        List<Multa> multasGeneradas = prestamosVencidos
            .stream()
            .map(prestamo -> {

                long diasRetraso = ChronoUnit.DAYS.between(prestamo.getFechaLimite(), LocalDate.now());
                BigDecimal importe = COSTO_POR_DIA.multiply(BigDecimal.valueOf(diasRetraso));

                Multa multa = Multa.builder()
                    .diasRetraso((int) diasRetraso)
                    .importe(importe)
                    .prestamo(prestamo)
                    .estado(estado)
                    .build();

                return multa;
            })
            .toList();

        log.info("TOTAL DE MULTAS GENERADAS: {}",multasGeneradas.size());
        return multaRepository.saveAll(multasGeneradas);
    }

    @Override
    @Transactional
    public List<Multa> actualizarDiasRetraso() {

        EstadoMulta estadoPendiente = buscarEstadoPorNombre(EstadoMultaNombre.PENDIENTE);
        List<Multa> multas = multaRepository.findByEstado(estadoPendiente);

        multas.forEach(multa -> {
            LocalDate fechaRetraso = multa.getPrestamo().getFechaLimite();
            long diasRetrasoActulizado = ChronoUnit.DAYS.between(fechaRetraso , LocalDate.now());

            BigDecimal importeActulizado = COSTO_POR_DIA.multiply(BigDecimal.valueOf(diasRetrasoActulizado));

            multa.setDiasRetraso((int) diasRetrasoActulizado);
            multa.setImporte(importeActulizado);
        });

        return multaRepository.saveAll(multas);
    }



    /* ================================= SERVICIOS PARA APP WEB RESERVA =============================================== */
    @Override
    public PaginaResponse<MultaResponse> misMultas(CustomUserDetails usuario, EstadoMultaNombre estado, Pageable pageable) {
        Page<Multa> multas = multaRepository.bsucarMisReservas(usuario.getUsuario(), estado, pageable);
        Page<MultaResponse> paginaResponse = multas.map(multaMapper::entityToDto);

        return new PaginaResponse<>(
            paginaResponse.getContent(), 
            paginaResponse.getNumber(), 
            paginaResponse.getTotalPages(), 
            paginaResponse.getTotalElements(), 
            paginaResponse.isFirst(), 
            paginaResponse.isLast()
        );
    }




    
    /*================================= FUNCIONES REUTILIZABLES ===================================== */
    private EstadoMulta buscarEstadoPorNombre (EstadoMultaNombre estado){
        return estadoMultaRepository.findByNombre(estado)
            .orElseThrow(() -> new ResourceNotFoundException("Estado multa no encontrado."));
    } 
}

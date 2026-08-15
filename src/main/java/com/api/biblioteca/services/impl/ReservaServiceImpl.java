package com.api.biblioteca.services.impl;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.api.biblioteca.configurations.CustomUserDetails;
import com.api.biblioteca.dtos.request.PrestamoRequest;
import com.api.biblioteca.dtos.request.ReservaRequest;
import com.api.biblioteca.dtos.response.PaginaResponse;
import com.api.biblioteca.dtos.response.ReservaResponse;
import com.api.biblioteca.dtos.updates.EstadoRequest;
import com.api.biblioteca.enums.EstadoEjemplarNombre;
import com.api.biblioteca.enums.EstadoReservaNombre;
import com.api.biblioteca.exceptions.AccessDeniedException;
import com.api.biblioteca.exceptions.ConflictExeption;
import com.api.biblioteca.exceptions.ResourceNotFoundException;
import com.api.biblioteca.mappers.ReservaMapper;
import com.api.biblioteca.models.Ejemplar;
import com.api.biblioteca.models.EstadoEjemplar;
import com.api.biblioteca.models.EstadoReserva;
import com.api.biblioteca.models.Libro;
import com.api.biblioteca.models.Reserva;
import com.api.biblioteca.repositorys.EjemplarRepository;
import com.api.biblioteca.repositorys.EstadoEjemplarRepository;
import com.api.biblioteca.repositorys.EstadoReservaRepository;
import com.api.biblioteca.repositorys.LibroRepository;
import com.api.biblioteca.repositorys.ReservaRepository;
import com.api.biblioteca.services.PrestamoService;
import com.api.biblioteca.services.ReservaService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("api/reservas")
@RequiredArgsConstructor
@Slf4j
public class ReservaServiceImpl implements ReservaService{
    
    private final ReservaRepository reservaRepository;
    private final LibroRepository libroRepository;
    private final EstadoReservaRepository estadoReservaRepository;
    private final PrestamoService prestamoService;
    private final ReservaMapper reservaMapper;

    private final EjemplarRepository ejemplarRepository;
    private final EstadoEjemplarRepository estadoEjemplarRepository;


    /* =============================== SERVICIOS PARA APP WEB RESERVAS ====================================*/
   @Override
    public PaginaResponse<ReservaResponse> misReservas(CustomUserDetails usuario, EstadoReservaNombre estado, Pageable pageable) {
        Page<Reserva> reservas = reservaRepository.buscarMisReservas(usuario.getUsuario(), estado, pageable);
        Page<ReservaResponse> paginaResponse = reservas.map(reservaMapper::entityToDto);
        
        return new PaginaResponse<>(
            paginaResponse.getContent(), 
            paginaResponse.getNumber(), 
            paginaResponse.getTotalPages(), 
            paginaResponse.getTotalElements(), 
            paginaResponse.isFirst(), 
            paginaResponse.isLast());
    }

    
    @Override
    @Transactional
    public ReservaResponse cancelarReserva(CustomUserDetails usuario, Long id) {

        Reserva reserva = buscarReservaPorId(id);

        if(!reserva.getUsuario().getId().equals(usuario.id())){
            throw new AccessDeniedException("No tienes permiso para esta accion.");
        }

        if(reserva.getEstado().getNombre() != EstadoReservaNombre.DISPONIBLE && reserva.getEstado().getNombre() != EstadoReservaNombre.PENDIENTE){
            throw new ConflictExeption("Esta reserva ya se encuentra "+reserva.getEstado().getNombre().name());
        }

        reserva.setEstado(buscarEstadoReservaPorNombre(EstadoReservaNombre.CANCELADA));
        return reservaMapper.entityToDto(reservaRepository.save(reserva));
    }


    
    /* ======================== SERVICIOS PARA ADMIN USUARIO ================================ */
    @Override
    @Transactional
    public ReservaResponse crearNuevo(ReservaRequest request, CustomUserDetails usuario) {

        EstadoReserva estadoReserva = buscarEstadoReservaPorNombre(EstadoReservaNombre.PENDIENTE);

        if(reservaRepository.countByUsuarioAndEstado(usuario.getUsuario(), estadoReserva) >= 10){
            throw new ConflictExeption("No puedes tener más de 10 reservas en estado PENDIENTE.");
        }

        Libro libro = libroRepository.findById(request.libroId())
            .orElseThrow(() -> new ResourceNotFoundException("Libro no encontrado."));

        Reserva reserva = Reserva.builder()
            .usuario(usuario.getUsuario())
            .libro(libro)
            .estado(estadoReserva)
            .build();

        return reservaMapper.entityToDto(reservaRepository.save(reserva));
    }


    @Override
    @Transactional
    public ReservaResponse cambiarEstadoReserva(Long id, EstadoRequest request, CustomUserDetails usuarioAdmin) {

        Reserva reserva = reservaRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Reserva no encontrada."));

        EstadoReserva estadoReserva = estadoReservaRepository.findById(request.id())
            .orElseThrow(() -> new ResourceNotFoundException("Estado reserva no encontrado."));

         if(reserva.getEstado().getNombre() == EstadoReservaNombre.CANCELADA || reserva.getEstado().getNombre() == EstadoReservaNombre.EXPIRADA || reserva.getEstado().getNombre() == EstadoReservaNombre.ENTREGADA){
            throw new ConflictExeption("Esta reserva ya se encuentra en estado "+reserva.getEstado().getNombre()+".");
        }

        if(estadoReserva.getNombre() != EstadoReservaNombre.ENTREGADA){
            reserva.setFechaLimiteRecoger(LocalDate.now().plusDays(15));
            reserva.setEstado(estadoReserva);
            return reservaMapper.entityToDto(reservaRepository.save(reserva));
        }

        reserva.setFechaLimiteRecoger(null);

        Ejemplar ejemplarDisponible = 
            ejemplarRepository.findFirstByLibroAndEstadoOrderByIdAsc(reserva.getLibro(), buscarEstadoEjemplarPorNombre(EstadoEjemplarNombre.DISPONIBLE))
                .orElseThrow(() -> new ConflictExeption("No hay ejemplares disponibles."));
        
        PrestamoRequest prestamo = new PrestamoRequest(
            reserva.getUsuario().getId(), 
            Set.of(ejemplarDisponible.getId())
        );

        prestamoService.crearNuevo(prestamo, usuarioAdmin);
        reserva.setEstado(estadoReserva);
        
        return reservaMapper.entityToDto(reservaRepository.save(reserva));
    }

    @Override
    @Transactional
    public void eliminarReservaPorId(Long id) {

        Reserva reserva = reservaRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Reserva no encontrada."));

        reservaRepository.delete(reserva);
    }

     @Override
    public ReservaResponse obtenerReservaPorId(Long id) {
        return reservaMapper.entityToDto(reservaRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Reserva no encontrada"))
        );
    }

    @Override
    public List<ReservaResponse> obtenerReservas(Long estadoId) {
        return reservaMapper.listEntityToListDto(reservaRepository.buscarPorParametros(estadoId));
    }






    /* ======================== FUNCIONES REUTILIZABLES =======================================*/
    private Reserva buscarReservaPorId(Long id){
        return reservaRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Reserva no encontrado."));
    }

    private EstadoReserva buscarEstadoReservaPorNombre(EstadoReservaNombre nombre){
        return estadoReservaRepository.findByNombre(nombre)
            .orElseThrow(() -> new ResourceNotFoundException("Estado reserva no encontrado."));
    }

    private EstadoEjemplar buscarEstadoEjemplarPorNombre(EstadoEjemplarNombre nombre){
        return estadoEjemplarRepository.findByNombre(nombre)
            .orElseThrow(() -> new ResourceNotFoundException("Estado ejemplar no encontrado."));
    }

}

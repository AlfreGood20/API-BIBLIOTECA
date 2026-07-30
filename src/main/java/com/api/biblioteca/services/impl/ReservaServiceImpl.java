package com.api.biblioteca.services.impl;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.api.biblioteca.configurations.CustomUserDetails;
import com.api.biblioteca.dtos.request.PrestamoRequest;
import com.api.biblioteca.dtos.request.ReservaRequest;
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


    @Override
    public ReservaResponse crearNuevo(ReservaRequest request, CustomUserDetails usuario) {

        Libro libro = libroRepository.findById(request.libroId())
            .orElseThrow(() -> new ResourceNotFoundException("Libro no encontrado."));

        EstadoReserva estadoReserva = estadoReservaRepository.findByNombre(EstadoReservaNombre.PENDIENTE)
            .orElseThrow(() -> new ResourceNotFoundException("Estado reserva no encontrado."));

        Reserva reserva = Reserva.builder()
            .usuario(usuario.getUsuario())
            .libro(libro)
            .estado(estadoReserva)
            .build();

        return reservaMapper.entityToDto(reservaRepository.save(reserva));
    }

    @Override
    public List<ReservaResponse> misReservas(CustomUserDetails usuario){
        return reservaMapper.listEntityToListDto(reservaRepository.findByUsuarioOrderByFechaReservaDesc(usuario.getUsuario()));
    }

    
    @Override
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

    @Override
    public List<ReservaResponse> obtenerReservas(Long estadoId) {
        return reservaMapper.listEntityToListDto(reservaRepository.buscarPorParametros(estadoId));
    }
    
    @Override
    public ReservaResponse obtenerReservaPorId(Long id) {
        return reservaMapper.entityToDto(reservaRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Reserva no encontrada"))
        );
    }

    @Override
    public void eliminarReservaPorId(Long id) {

        Reserva reserva = reservaRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Reserva no encontrada."));

        reservaRepository.delete(reserva);
    }

    @Override
    @Transactional
    public ReservaResponse cambiarEstadoReserva(Long id, EstadoRequest request, CustomUserDetails usuarioAdmin) {

        Reserva reserva = reservaRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Reserva no encontrada."));

        EstadoReserva estadoReserva = estadoReservaRepository.findById(request.id())
            .orElseThrow(() -> new ResourceNotFoundException("Estado reserva no encontrado."));

         if(reserva.getEstado().getNombre() == EstadoReservaNombre.CANCELADA || reserva.getEstado().getNombre() == EstadoReservaNombre.EXPIRADA){
            throw new ConflictExeption("Esta reserva ya se encuentra en estado "+reserva.getEstado().getNombre()+".");
        }

        if(estadoReserva.getNombre() != EstadoReservaNombre.ENTREGADA){
            reserva.setEstado(estadoReserva);
            return reservaMapper.entityToDto(reservaRepository.save(reserva));
        }

        reserva.setFechaLimiteRecoger(LocalDate.now().plusDays(15));

        Ejemplar ejemplarDisponible = 
            ejemplarRepository.findFirtByLibroAndEstadoOrderByAsc(reserva.getLibro(), buscarEstadoEjemplarPorNombre(EstadoEjemplarNombre.DISPONIBLE))
                .orElseThrow(() -> new ConflictExeption("No hay ejemplares disponibles."));
        
        PrestamoRequest prestamo = new PrestamoRequest(
            reserva.getUsuario().getId(), 
            Set.of(ejemplarDisponible.getId())
        );

        prestamoService.crearNuevo(prestamo, usuarioAdmin);
        reserva.setEstado(estadoReserva);
        
        return reservaMapper.entityToDto(reservaRepository.save(reserva));
    }

    /* FUNCIONES REUTILIZABLES */
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

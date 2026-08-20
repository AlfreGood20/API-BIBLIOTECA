package com.api.biblioteca.services.impl;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.api.biblioteca.configurations.CustomUserDetails;
import com.api.biblioteca.dtos.request.PrestamoRequest;
import com.api.biblioteca.dtos.response.PaginaResponse;
import com.api.biblioteca.dtos.response.PrestamoResponse;
import com.api.biblioteca.enums.EstadoEjemplarNombre;
import com.api.biblioteca.enums.EstadoMultaNombre;
import com.api.biblioteca.enums.EstadoPrestamoNombre;
import com.api.biblioteca.exceptions.BusinessExeption;
import com.api.biblioteca.exceptions.ResourceNotFoundException;
import com.api.biblioteca.mappers.PrestamoMapper;
import com.api.biblioteca.models.Ejemplar;
import com.api.biblioteca.models.EstadoEjemplar;
import com.api.biblioteca.models.EstadoMulta;
import com.api.biblioteca.models.EstadoPrestamo;
import com.api.biblioteca.models.Prestamo;
import com.api.biblioteca.models.Usuario;
import com.api.biblioteca.repositorys.EjemplarRepository;
import com.api.biblioteca.repositorys.EstadoEjemplarRepository;
import com.api.biblioteca.repositorys.EstadoMultaRepository;
import com.api.biblioteca.repositorys.EstadoPrestamoRepository;
import com.api.biblioteca.repositorys.MultaRepository;
import com.api.biblioteca.repositorys.PrestamoRepository;
import com.api.biblioteca.repositorys.UsuarioRepository;
import com.api.biblioteca.services.PrestamoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class PrestamoServiceImpl implements PrestamoService{
    
    private final PrestamoRepository prestamoRepository;
    private final MultaRepository multaRepository;
    private final UsuarioRepository usuarioRepository;
    private final EjemplarRepository ejemplarRepository;
    private final EstadoPrestamoRepository estadoPrestamoRepository;
    private final EstadoEjemplarRepository estadoEjemplarRepository;
    private final EstadoMultaRepository estadoMultaRepository;

    private final PrestamoMapper prestamoMapper;

    @Value("${app.prestamo-dias-limite}")
    private int DIAS_LIMITE;
    

    /* =========================== SERVICIO PARA APP WEB RESERVAS ======================================= */
    @Override
    public PaginaResponse<PrestamoResponse> misPrestamos(CustomUserDetails usuario, EstadoPrestamoNombre estado, Pageable pageable) {
        Page<Prestamo> prestamos = prestamoRepository.misPrestamos(usuario.getUsuario(), estado, pageable);
        Page<PrestamoResponse> paginaResponse = prestamos.map(prestamoMapper::entityToDto);

        return new PaginaResponse<>(
            paginaResponse.getContent(), 
            paginaResponse.getNumber(), 
            paginaResponse.getTotalPages(), 
            paginaResponse.getTotalElements(), 
            paginaResponse.isFirst(), 
            paginaResponse.isLast()
        );
    }

    /* ============================ SERIVICIO PARA SCHEDULED  =========================================== */
    @Override
    @Transactional
    public List<Prestamo> actualizarPrestamosEstadoVencido() {
        log.info("BUSCANDO PRESTAMOS VENCIDOS...");
        List<Prestamo> prestamosVencidos = prestamoRepository.findByEstadoAndFechaLimiteBefore(buscarPorNombre(EstadoPrestamoNombre.ACTIVO), LocalDate.now());
        log.info("TOTALES PRESTAMOS VENCIDOS: {}",prestamosVencidos.size());

        prestamosVencidos.forEach(p -> p.setEstado(buscarPorNombre(EstadoPrestamoNombre.VENCIDO)));

        prestamoRepository.saveAll(prestamosVencidos);
        log.info("ACTUALIZADO CON EXITO LOS PRESTAMOS VENCIDOS.");

        return prestamosVencidos;
    }

    /* ========================== SERVICIOS PARA ADMIN Y BIBLIOTECARIOS ============================= */
    @Override
    @Transactional
    public List<PrestamoResponse> crearNuevo(PrestamoRequest request, CustomUserDetails usuarioAdmin) {
        Usuario usuario = buscarUsuarioPorId(request.usuarioId());

        List<Prestamo> prestamosPrestados = new ArrayList<>();

        for (Long ejemplarId : request.ejemplaresId()) {
            prestamosPrestados.add(generarPrestamo(usuario, ejemplarId, usuarioAdmin.getUsuario()));
       }
       return prestamoMapper.listEntityToListDto(prestamosPrestados);
    }


    @Override
    @Transactional(readOnly = true)
    public PaginaResponse<PrestamoResponse> obtenerPrestamos(EstadoPrestamoNombre estado, Long usuarioAdminId, Long usuarioId, Pageable pageable) {
        Page<Prestamo> prestamos = prestamoRepository.prestamosPorFiltro(estado, usuarioAdminId, usuarioId, pageable);
        Page<PrestamoResponse> paginaResponse = prestamos.map(prestamoMapper::entityToDto);

        return new PaginaResponse<>(
            paginaResponse.getContent(), 
            paginaResponse.getNumber(), 
            paginaResponse.getTotalPages(), 
            paginaResponse.getTotalElements(), 
            paginaResponse.isFirst(), 
            paginaResponse.isLast()
        );
    }

    @Override
    public PrestamoResponse obtenerPrestamoPorId(Long id) {
        return prestamoMapper.entityToDto(buscarPrestamoPorId(id));
    }

    @Override
    @Transactional
    public PrestamoResponse devolverPrestamoPorId(Long id) {

        Prestamo prestamo = buscarPrestamoPorId(id);

        if(prestamo.getEstado().getNombre() == EstadoPrestamoNombre.DEVUELTO){
            throw new BusinessExeption("Prestamo ya se encuntra devuelto.");
        }

        prestamo.setFechaDevolucion(LocalDate.now());
        prestamo.setEstado(buscarPorNombre(EstadoPrestamoNombre.DEVUELTO));
        prestamo.getEjemplar().setEstado(buscarEstadoEjemplarPorNombre(EstadoEjemplarNombre.DISPONIBLE));

        return prestamoMapper.entityToDto(prestamoRepository.save(prestamo));
    }




    /* ============================ FUNCIONES REUTILIZABLES ============================================== */
    private EstadoPrestamo buscarPorNombre(EstadoPrestamoNombre nombre){
        return estadoPrestamoRepository.findByNombre(nombre)
            .orElseThrow(()-> new ResourceNotFoundException("Estado prestamo no encontrado."));
    }

    private EstadoEjemplar buscarEstadoEjemplarPorNombre(EstadoEjemplarNombre nombre){
        return estadoEjemplarRepository.findByNombre(nombre)
            .orElseThrow(()-> new ResourceNotFoundException("Estado ejemplar no encontrado."));
    }

    private EstadoMulta buscarEstadoMultaPorNombre(EstadoMultaNombre nombre){
        return estadoMultaRepository.findByNombre(nombre)
            .orElseThrow(()-> new ResourceNotFoundException("Estado multa no encontrado."));
    }

    private Prestamo buscarPrestamoPorId(Long id){
        return prestamoRepository.findById(id)
            .orElseThrow(()-> new ResourceNotFoundException("Prestamo no encontrado."));
    }

    private Usuario buscarUsuarioPorId(Long id){
        return usuarioRepository.findById(id)
            .orElseThrow(()-> new ResourceNotFoundException("Usuario no encontrado."));
    }

    private Ejemplar buscarEjemplarPorId(Long id){
        return ejemplarRepository.findById(id)
            .orElseThrow(()-> new ResourceNotFoundException("Ejemplar no encontrado."));
    }

    @Override
    @Transactional
    public Prestamo generarPrestamo(Usuario usuario, Long ejemplarId, Usuario autoriza){

        if(multaRepository.countByUsuarioAndEstado(usuario, buscarEstadoMultaPorNombre(EstadoMultaNombre.PENDIENTE)) >= 2){
            throw new BusinessExeption("No se puede prestar a usuarios con mas de 2 multas.");
        }

        Ejemplar ejemplar = buscarEjemplarPorId(ejemplarId);
   
        if(ejemplar.getEstado().getNombre() != EstadoEjemplarNombre.DISPONIBLE){
            throw new BusinessExeption("Ejemplar con id: "+ejemplar.getId()+" se encuentra "+ejemplar.getEstado().getNombre().toString());
        }

        Prestamo prestamo = Prestamo.builder()
                .fechaLimite(LocalDate.now().plusDays(DIAS_LIMITE))
                .fechaDevolucion(null)
                .usuario(usuario)
                .usuarioAdmin(autoriza)
                .estado(buscarPorNombre(EstadoPrestamoNombre.ACTIVO))
                .ejemplar(ejemplar)
                .build();

        ejemplar.setEstado(buscarEstadoEjemplarPorNombre(EstadoEjemplarNombre.PRESTADO));

        return prestamoRepository.save(prestamo);
    }
}
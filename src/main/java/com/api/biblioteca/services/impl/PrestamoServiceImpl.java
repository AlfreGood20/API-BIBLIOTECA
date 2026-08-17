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
import com.api.biblioteca.enums.EstadoUsuarioNombre;
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
    

    /* ========================== SERVICIOS PARA ADMIN Y BIBLIOTECARIOS ============================= */
    @Override
    @Transactional
    public List<PrestamoResponse> crearNuevo(PrestamoRequest request, CustomUserDetails usuarioAdmin) {

        Usuario usuario = usuarioRepository.findById(request.usuarioId())
            .orElseThrow(()->new ResourceNotFoundException("Usuario no encontrado."));

        if(usuario.getEstado().getNombre() != EstadoUsuarioNombre.ACTIVO){
            throw new BusinessExeption("No se puede hacer prestamos a usuario, INACTIVO, SUSPENDIDO O BLOQUEADO.");
        }

        if(multaRepository.countByUsuarioAndEstado(usuario, buscarEstadoMultaPorNombre(EstadoMultaNombre.PENDIENTE)) >= 2){
            throw new BusinessExeption("No se puede prestar a usuarios con mas de 2 multas.");
        }

        List<Ejemplar> ejemplares = request.ejemplaresId()
            .stream()
            .map(id -> {
                return ejemplarRepository.findById(id)
                    .orElseThrow(()-> new ResourceNotFoundException("Ejemplar con ID:"+id+" no encontrado."));
            })
            .map(ejemplar -> {
                if(ejemplar.getEstado().getNombre() != EstadoEjemplarNombre.DISPONIBLE){
                    throw new BusinessExeption("Ejemplar con ID:"+ejemplar.getId()+" se encuentra "+ejemplar.getEstado().getNombre().toString());
                }
                return ejemplar;
            })
            .toList();

        List<Prestamo> prestamosPrestados = new ArrayList<>();

       for (Ejemplar ejemplar : ejemplares) {

            Prestamo prestamo = Prestamo.builder()
                .fechaLimite(LocalDate.now().plusDays(DIAS_LIMITE))
                .fechaDevolucion(null)
                .usuario(usuario)
                .usuarioAdmin(usuarioAdmin.getUsuario())
                .estado(buscarPorNombre(EstadoPrestamoNombre.ACTIVO))
                .ejemplar(ejemplar)
                .build();
            
            ejemplar.setEstado(buscarEstadoEjemplarPorNombre(EstadoEjemplarNombre.PRESTADO));
            prestamosPrestados.add(prestamoRepository.save(prestamo));
       }

       return prestamoMapper.listEntityToListDto(prestamosPrestados);
    }

    @Override
    public List<PrestamoResponse> obtenerPrestamos(Long estadoId, Long usuarioAdminId, Long usuarioId) {
        return null;
    }

    @Override
    public PrestamoResponse obtenerPrestamoPorId(Long id) {
        return prestamoMapper.entityToDto(prestamoRepository.findById(id)
            .orElseThrow(()-> new ResourceNotFoundException("Prestamo no encontrado")));
    }

    @Override
    @Transactional
    public PrestamoResponse devolverPrestamoPorId(Long id) {

        Prestamo prestamo = prestamoRepository.findById(id)
            .orElseThrow(()-> new ResourceNotFoundException("Prestamo no encontrado"));

        if(prestamo.getEstado().getNombre() == EstadoPrestamoNombre.DEVUELTO){
            throw new BusinessExeption("Prestamo ya se encuntra devuelto.");
        }

        prestamo.setFechaDevolucion(LocalDate.now());
        prestamo.setEstado(buscarPorNombre(EstadoPrestamoNombre.DEVUELTO));
        prestamo.getEjemplar().setEstado(buscarEstadoEjemplarPorNombre(EstadoEjemplarNombre.DISPONIBLE));

        return prestamoMapper.entityToDto(prestamoRepository.save(prestamo));
    }

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
}
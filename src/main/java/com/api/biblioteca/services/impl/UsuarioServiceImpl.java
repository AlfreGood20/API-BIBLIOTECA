package com.api.biblioteca.services.impl;

import com.api.biblioteca.repositorys.ReservaRepository;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import com.api.biblioteca.configurations.CustomUserDetails;
import com.api.biblioteca.dtos.request.UsuarioRequest;
import com.api.biblioteca.dtos.response.MultaResponse;
import com.api.biblioteca.dtos.response.PrestamoResponse;
import com.api.biblioteca.dtos.response.ReservaResponse;
import com.api.biblioteca.dtos.response.UsuarioResponse;
import com.api.biblioteca.dtos.updates.EstadoRequest;
import com.api.biblioteca.enums.EstadoUsuarioNombre;
import com.api.biblioteca.enums.RolNombre;
import com.api.biblioteca.exceptions.BusinessExeption;
import com.api.biblioteca.exceptions.ConflictExeption;
import com.api.biblioteca.exceptions.ResourceNotFoundException;
import com.api.biblioteca.mappers.DireccionMapper;
import com.api.biblioteca.mappers.MultaMapper;
import com.api.biblioteca.mappers.PrestamoMapper;
import com.api.biblioteca.mappers.ReservaMapper;
import com.api.biblioteca.mappers.TelefonoMapper;
import com.api.biblioteca.mappers.UsuarioMapper;
import com.api.biblioteca.models.Credencial;
import com.api.biblioteca.models.Direccion;
import com.api.biblioteca.models.EstadoUsuario;
import com.api.biblioteca.models.Multa;
import com.api.biblioteca.models.Municipio;
import com.api.biblioteca.models.Prestamo;
import com.api.biblioteca.models.Rol;
import com.api.biblioteca.models.Telefono;
import com.api.biblioteca.models.TipoTelefono;
import com.api.biblioteca.models.Usuario;
import com.api.biblioteca.repositorys.CredencialRepository;
import com.api.biblioteca.repositorys.EstadoUsuarioRepository;
import com.api.biblioteca.repositorys.MunicipioRepository;
import com.api.biblioteca.repositorys.PrestamoRepository;
import com.api.biblioteca.repositorys.RolRepository;
import com.api.biblioteca.repositorys.TipoTelefonoRepository;
import com.api.biblioteca.repositorys.UsuarioRepository;
import com.api.biblioteca.services.UsuarioService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class UsuarioServiceImpl implements UsuarioService{


    private final UsuarioMapper usuarioMapper;
    private final DireccionMapper direccionMapper;
    private final UsuarioRepository usuarioRepository;
    private final TelefonoMapper telefonoMapper;
    private final PrestamoMapper prestamoMapper;
    private final MultaMapper multaMapper;
    private final ReservaMapper reservaMapper;

    private final RolRepository rolRepository;
    private final EstadoUsuarioRepository estadoUsuarioRepository;
    private final MunicipioRepository municipioRepository;
    private final TipoTelefonoRepository tipoTelefonoRepository;
    private final CredencialRepository credencialRepository;
    private final PrestamoRepository prestamoRepository;
    private final ReservaRepository reservaRepository;

    private final PasswordEncoder encoder;
    private final Path uploadPath;

    @Override
    @Transactional
    public UsuarioResponse crearNuevo(UsuarioRequest request) {

        Usuario nuevo = usuarioMapper.dtoToEntity(request);

        Rol rol = buscarRolPorId(request.rolId());
        nuevo.setRol(rol);

        EstadoUsuario estado = buscarEstadoUsuario(EstadoUsuarioNombre.ACTIVO);
        nuevo.setEstado(estado);

        if(credencialRepository.existsByCorreo(request.credencial().correo())){
            throw new ConflictExeption("Correo ya existente registrado.");
        }

        Credencial credencial = Credencial.builder()
            .contrasena(encoder.encode(request.credencial().contrasena()))
            .correo(request.credencial().correo())
            .usuario(nuevo).build();

        nuevo.setCredencial(credencial);

        List<Telefono> telefonos = request.telefonos()
            .stream()
            .map(t -> {
                Telefono telefono = telefonoMapper.dtoToEntity(t);
                telefono.setUsuario(nuevo);
                telefono.setTipo(buscarTipoTelefonoPorId(t.tipoId()));

                return telefono;
            })
            .toList();
        nuevo.setTelefonos(telefonos);

        Direccion direccion = direccionMapper.dtoToEntity(request.direccion());
        direccion.setUsuario(nuevo);
        direccion.setMunicipio(buscarMunicipioPorId(request.direccion().municipioId()));

        nuevo.setDireccion(direccion);

        return usuarioMapper.entityToDto(usuarioRepository.save(nuevo));
    }

    @Override
    public List<UsuarioResponse> obtenerUsuarios() {
        return usuarioMapper.listEntityToListDto(usuarioRepository.findAll());
    }

    @Override
    public UsuarioResponse obtenerPorId(Long id) {
        return usuarioMapper.entityToDto(buscarUsuarioPorId(id));
    }

    @Override
    @Transactional
    public UsuarioResponse cambiarEstadoUsuario(Long id, EstadoRequest request) {
        Usuario usuario = buscarUsuarioPorId(id);
        EstadoUsuario estado = buscarEstadoUsuarioPorId(request.id());
        usuario.setEstado(buscarEstadoUsuario(estado.getNombre()));

        return usuarioMapper.entityToDto(usuarioRepository.save(usuario));
    }

    @Override
    public List<UsuarioResponse> obtenerUsuariosPorParametros(String nombre, RolNombre rol, EstadoUsuarioNombre estado) {
        Rol encontrar = buscarPorNombreRol(rol);
        EstadoUsuario estadoEncontrar = buscarEstadoUsuario(estado);

        return usuarioMapper.listEntityToListDto(usuarioRepository.buscarPorParametros(nombre, encontrar.getNombre().name(), estadoEncontrar.getNombre().name()));
    }

    @Override
    public UsuarioResponse actulizarDatos(Long id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'actulizarDatos'");
    }

    @Override
    public List<PrestamoResponse> prestamosPorUsuario(Long id) {
        Usuario usuario = buscarUsuarioPorId(id);
        return prestamoMapper.listEntityToListDto(prestamoRepository.findByUsuario(usuario));
    }

    @Override
    public List<MultaResponse> multasPorUsuario(Long id) {

        Usuario usuario = buscarUsuarioPorId(id);
        
        List<Prestamo> prestamos = prestamoRepository.findByUsuario(usuario);
        List<Multa> multas = prestamos.stream()
            .map(Prestamo::getMulta)
            .filter(multa -> multa != null)
            .toList();

        return multaMapper.listEntityToListDto(multas);
    }

    @Override
    public List<ReservaResponse> reservasPorUsuario(Long id) {
        Usuario usuario = buscarUsuarioPorId(id);
        return reservaMapper.listEntityToListDto(reservaRepository.findByUsuarioOrderByFechaReservaDesc(usuario));
    }

    
    @Override
    public UsuarioResponse obtenerPerfil(CustomUserDetails usuario) {

        Usuario usuarioFinal = buscarUsuarioPorId(usuario.id());
        
        return usuarioMapper.entityToDto(usuarioFinal);
    }

    @Override
    @Transactional
    public UsuarioResponse actulizarFotoPerfil(CustomUserDetails usuario, MultipartFile file) {

        if(file == null || file.isEmpty()){
            throw new BusinessExeption("Imagen no recibida, es obligatorio.");
        }

        /* VALIDAR EL FORMATO CORRECTO */
        String contentType = file.getContentType();

        if (!MediaType.IMAGE_JPEG_VALUE.equals(contentType) && !MediaType.IMAGE_PNG_VALUE.equals(contentType)) {
            throw new BusinessExeption("Solo se permiten imágenes JPG o PNG.");
        }

        Usuario usuarioObtenido = usuario.getUsuario();
        String fotoVieja = usuarioObtenido.getFotoUrl();
        
        usuarioObtenido.setFotoUrl(guardarFotoAlDirectorio(file));

        /* BORRAR FOTO ANTERIOR PARA NO OCUPAR ESPACIO EN DISCO */
        if(fotoVieja != null && !fotoVieja.isBlank()){
            borrarFotoDelDirectorio(fotoVieja);
        }

        return usuarioMapper.entityToDto(usuarioRepository.save(usuarioObtenido));
    }


    
    //FUNCIONES REUTILIZABLE
    private Usuario buscarUsuarioPorId(Long id){
        return usuarioRepository.findById(id)
            .orElseThrow(()-> new ResourceNotFoundException("Usuario no encontrado"));
    }

    private Municipio buscarMunicipioPorId(Long id){
        return municipioRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Municipio no encontrado"));
    }

    private Rol buscarRolPorId(Long id){
        return rolRepository.findById(id)
            .orElseThrow(()->  new ResourceNotFoundException("Rol no encontrado"));
    }

    private Rol buscarPorNombreRol(RolNombre rol){
        return rolRepository.findByNombre(rol)
            .orElseThrow(() -> new ResourceNotFoundException("Rol no encontrado"));
    }

    private EstadoUsuario buscarEstadoUsuario(EstadoUsuarioNombre estado){
        return estadoUsuarioRepository.findByNombre(estado)
            .orElseThrow(() -> new ResourceNotFoundException("Estado usuario no encontrado"));
    }

    private EstadoUsuario buscarEstadoUsuarioPorId(Long id){
        return estadoUsuarioRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Estado usuario no encontrado"));
    }

    private TipoTelefono buscarTipoTelefonoPorId(Long id){
        return tipoTelefonoRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Tipo de telefono no encontrado"));
    }

    private String guardarFotoAlDirectorio(MultipartFile file){
        String nombreImagenOriginal = file.getOriginalFilename();

        String nombreImagen =
            UUID.randomUUID().toString().substring(0,10) +
            nombreImagenOriginal.substring(nombreImagenOriginal.indexOf("."));

        try {
            Path rutaCompleta = uploadPath.resolve(nombreImagen);
            Files.copy(file.getInputStream(), rutaCompleta);
        } catch (IOException ex) {}

        return "/uploads/"+nombreImagen;
    }

    private void borrarFotoDelDirectorio(String ruta){

        try {
            String nombreArchivo = Paths.get(ruta).getFileName().toString();
            Path rutaAnterior = uploadPath.resolve(nombreArchivo);
            Files.deleteIfExists(rutaAnterior);
        } catch (IOException ex) {}
    
    }
}
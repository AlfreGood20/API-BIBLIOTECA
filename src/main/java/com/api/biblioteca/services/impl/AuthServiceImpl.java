package com.api.biblioteca.services.impl;

import java.util.List;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.api.biblioteca.configurations.CustomUserDetails;
import com.api.biblioteca.configurations.JwtService;
import com.api.biblioteca.dtos.request.LoginRequest;
import com.api.biblioteca.dtos.request.UsuarioPublicRequest;
import com.api.biblioteca.dtos.response.TokenResponse;
import com.api.biblioteca.dtos.response.UsuarioResponse;
import com.api.biblioteca.enums.EstadoUsuarioNombre;
import com.api.biblioteca.enums.RolNombre;
import com.api.biblioteca.exceptions.ConflictExeption;
import com.api.biblioteca.exceptions.ResourceNotFoundException;
import com.api.biblioteca.mappers.DireccionMapper;
import com.api.biblioteca.mappers.TelefonoMapper;
import com.api.biblioteca.mappers.UsuarioMapper;
import com.api.biblioteca.models.Credencial;
import com.api.biblioteca.models.Direccion;
import com.api.biblioteca.models.EstadoUsuario;
import com.api.biblioteca.models.Municipio;
import com.api.biblioteca.models.Rol;
import com.api.biblioteca.models.Telefono;
import com.api.biblioteca.models.TipoTelefono;
import com.api.biblioteca.models.Token;
import com.api.biblioteca.models.Usuario;
import com.api.biblioteca.repositorys.CredencialRepository;
import com.api.biblioteca.repositorys.DireccionRepository;
import com.api.biblioteca.repositorys.EstadoUsuarioRepository;
import com.api.biblioteca.repositorys.MunicipioRepository;
import com.api.biblioteca.repositorys.RolRepository;
import com.api.biblioteca.repositorys.TipoTelefonoRepository;
import com.api.biblioteca.repositorys.TokenRepository;
import com.api.biblioteca.repositorys.UsuarioRepository;
import com.api.biblioteca.services.AuthService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService{

    private final TokenRepository tokenRepository;
    private final DireccionRepository direccionRepository;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final UsuarioMapper usuarioMapper;
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder encoder;
    private final CredencialRepository credencialRepository;
    private final RolRepository rolRepository;
    private final EstadoUsuarioRepository estadoUsuarioRepository;
    private final MunicipioRepository municipioRepository;
    private final DireccionMapper direccionMapper;
    private final TelefonoMapper telefonoMapper;
    private final TipoTelefonoRepository tipoTelefonoRepository;

    @Override
    @Transactional
    public TokenResponse iniciarSession(LoginRequest request, HttpServletResponse response) {

        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(request.correo(), request.contrasena())
        );

        CustomUserDetails usuario = (CustomUserDetails) authentication.getPrincipal();
        List<Token> tokens = usuario.getUsuario().getTokens();

        tokens.stream()
            .filter(token -> !token.isRevocado())
            .forEach(token -> token.setRevocado(true));

        tokenRepository.saveAll(tokens);
        
        return obtenerTokenAccess(usuario, response);
    }

    @Override
    @Transactional
    public void salirSession(HttpServletRequest request, HttpServletResponse response) {

        String tokenRefresh = getTokenRefresh(request);

        Token token = tokenRepository.findByTokenRefresh(tokenRefresh)
            .orElseThrow(()-> new CredentialsExpiredException("Token no encontrado."));

        CustomUserDetails usuario = new CustomUserDetails(token.getUsuario());

        if (token.isRevocado() || !jwtService.esValidoToken(tokenRefresh, usuario)) {
            throw new CredentialsExpiredException("Token invalido, expirado o revocado.");
        }

        token.setRevocado(true);
        tokenRepository.save(token);

        response.addCookie(obtenerCookie(null, 0));
    }

    @Override
    @Transactional
    public UsuarioResponse registrarse(UsuarioPublicRequest request) {

        if(credencialRepository.existsByCorreo(request.credencial().correo())){
            throw new ConflictExeption("Correo ya se encuentra registrado.");
        }
        
        Usuario usuario = usuarioMapper.dtoPublicToEntity(request);

        Credencial credencial = Credencial.builder()
            .correo(request.credencial().correo())
            .contrasena(encoder.encode(request.credencial().contrasena()))
            .usuario(usuario)
            .build();

        Rol rol = rolRepository.findByNombre(RolNombre.USUARIO)
            .orElseThrow();

        EstadoUsuario estado = estadoUsuarioRepository.findByNombre(EstadoUsuarioNombre.ACTIVO)
            .orElseThrow();

        List<Telefono> telefonos = request.telefonos()
            .stream()
            .map(t -> {
                Telefono telefono = telefonoMapper.dtoToEntity(t);
                telefono.setUsuario(usuario);
                TipoTelefono tipo = tipoTelefonoRepository.findById(t.tipoId())
                    .orElseThrow(()-> new ResourceNotFoundException("Tipo telefono no encontrado"));

                telefono.setTipo(tipo);

                return telefono;
            })
            .toList();
        usuario.setTelefonos(telefonos);

        Direccion direccion = direccionMapper.dtoToEntity(request.direccion());
        direccion.setUsuario(usuario);

        Municipio municipio = municipioRepository.findById(request.direccion().municipioId())
            .orElseThrow(() -> new ResourceNotFoundException("Municipio no encontrado."));
        direccion.setMunicipio(municipio);
        
        usuario.setCredencial(credencial);
        usuario.setRol(rol);
        usuario.setEstado(estado);
        direccionRepository.save(direccion);

        return usuarioMapper.entityToDto(usuarioRepository.save(usuario));
    }

    @Override
    @Transactional
    public TokenResponse refrescarToken(HttpServletRequest request, HttpServletResponse response) {

        String tokenRefresh = getTokenRefresh(request);
        
        Token token = tokenRepository.findByTokenRefresh(tokenRefresh)
            .orElseThrow(()-> new CredentialsExpiredException("Token no encontrado."));

        CustomUserDetails usuario = new CustomUserDetails(token.getUsuario());

        if (token.isRevocado() || !jwtService.esValidoToken(tokenRefresh, usuario)) {
            throw new CredentialsExpiredException("Token invalido, expirado o revocado.");
        }

        token.setRevocado(true);
        tokenRepository.save(token);

        return obtenerTokenAccess(usuario, response);
    }

    

    /* ===================================== FUNCIONES REUTILIZABLES ======================================= */
    private TokenResponse obtenerTokenAccess(CustomUserDetails usuario, HttpServletResponse response){

        String tokenAccess = jwtService.generarTokenAccess(usuario);
        String tokenRefresh = jwtService.generarTokenRefresh(usuario);

        Token tokenNuevo = Token.builder()
            .tokenRefresh(tokenRefresh)
            .fechaExpiracion(jwtService.obtenerFechaExpiracion(tokenRefresh))
            .usuario(usuario.getUsuario())
            .build();

        tokenRepository.save(tokenNuevo);

        response.addCookie(obtenerCookie(tokenRefresh, 24 * 60 *60));
        return new TokenResponse(tokenAccess, "Bearer");
    }

    private Cookie obtenerCookie(String token, int maxAge){

        Cookie cookie = new Cookie("TOKEN_REFRESH", token);
        cookie.setHttpOnly(true);
        cookie.setSecure(false); /* RECUERDA PONERLO EN TRUE EN PRODUCCIÓN */
        cookie.setPath("/api/auth");
        cookie.setMaxAge(maxAge);

        return cookie;
    }

     private String getTokenRefresh(HttpServletRequest request){
        if(request.getCookies() == null){
            throw new CredentialsExpiredException("Token refresh no encontrado.");
        }

        for(Cookie cookie : request.getCookies()){
            if("TOKEN_REFRESH".equals(cookie.getName())){
                return cookie.getValue();
            }
        }

        throw new CredentialsExpiredException("Token refresh no encontrado.");
    }

    
}

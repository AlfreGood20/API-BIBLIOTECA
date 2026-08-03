package com.api.biblioteca.configurations;

import java.io.IOException;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class FilterJwt extends OncePerRequestFilter{

    private final JwtService jwtService;
    private final CustomUserDetailsService userDetailsService;
    private final HandlerExceptionResolver handlerExceptionResolver;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        try{

            final String token = getToken(request);

            if(token == null){
                throw new IllegalArgumentException("Token vacío, no recibimos token.");
            }else{

                final String correo = jwtService.obtenerSuject(token);

                if(correo != null && SecurityContextHolder.getContext().getAuthentication() == null){
                    CustomUserDetails usuario = (CustomUserDetails) userDetailsService.loadUserByUsername(correo);

                    if(usuario.getUsername().equals(correo) && jwtService.esValidoToken(token, usuario)){
                        Authentication authentication = new UsernamePasswordAuthenticationToken(usuario, null, usuario.getAuthorities());

                        SecurityContextHolder.getContext().setAuthentication(authentication);
                    }
                }
            }

        }catch(Exception ex){
            handlerExceptionResolver.resolveException(request, response, filterChain, ex);
        }finally{
            filterChain.doFilter(request, response);
        }
    }

    private String getToken(HttpServletRequest request){
        String authorization = request.getHeader(HttpHeaders.AUTHORIZATION);

        if(authorization != null && authorization.startsWith("Bearer ")){
            return authorization.substring(7);
        }

        return null;
    }
}
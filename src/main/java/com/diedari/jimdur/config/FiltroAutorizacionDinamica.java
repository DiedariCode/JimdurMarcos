package com.diedari.jimdur.config;

import com.diedari.jimdur.model.Rol;
import com.diedari.jimdur.model.Vista;
import com.diedari.jimdur.service.VistaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class FiltroAutorizacionDinamica extends OncePerRequestFilter {

    @Autowired
    private VistaService vistaService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String uri = request.getRequestURI();

        // Permitir recursos estáticos y rutas públicas
        if (uri.startsWith("/css/") || uri.startsWith("/js/") || uri.startsWith("/image/") ||
            uri.equals("/") || uri.startsWith("/user/login") || uri.startsWith("/user/registro") ||
            uri.startsWith("/productos") || uri.startsWith("/user/contacto") || uri.startsWith("/user/nosotros") ||
            uri.startsWith("/uploads/") || uri.startsWith("/api/verification/")) {
            filterChain.doFilter(request, response);
            return;
        }

        // Buscar la vista correspondiente a la URI
        List<Vista> vistas = vistaService.findAllWithRoles();
        Vista vistaMatch = null;
        for (Vista vista : vistas) {
            if (vista.getPath() != null && coincideRuta(vista.getPath(), uri)) {
                vistaMatch = vista;
                break;
            }
        }

        if (vistaMatch != null && !vistaMatch.getRoles().isEmpty()) {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            if (auth == null || !auth.isAuthenticated()) {
                response.sendRedirect("/user/login");
                return;
            }
            Set<String> rolesUsuario = auth.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .collect(Collectors.toSet());
            boolean autorizado = vistaMatch.getRoles().stream()
                    .map(Rol::getNombre)
                    .map(nombre -> "ROLE_" + nombre)
                    .anyMatch(rolesUsuario::contains);

            if (!autorizado) {
                //response.sendError(HttpServletResponse.SC_FORBIDDEN, "No tienes permisos para acceder a esta ruta.");
                response.sendRedirect("/error/403");
                return;
            }
        }
        filterChain.doFilter(request, response);
    }

    // Soporte para patrones tipo "/admin/**"
    private boolean coincideRuta(String patron, String uri) {
        if (patron.endsWith("/**")) {
            String base = patron.substring(0, patron.length() - 3);
            return uri.startsWith(base);
        }
        return patron.equals(uri);
    }
} 
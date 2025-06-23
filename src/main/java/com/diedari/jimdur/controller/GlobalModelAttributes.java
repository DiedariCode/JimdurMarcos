package com.diedari.jimdur.controller;

import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.diedari.jimdur.dto.FavoritoDTO;
import com.diedari.jimdur.model.Usuario;
import com.diedari.jimdur.service.FavoritoService;
import com.diedari.jimdur.service.UsuarioService;

import jakarta.servlet.http.HttpServletRequest;

@ControllerAdvice
public class GlobalModelAttributes {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private FavoritoService favoritoService;

    @ModelAttribute
    public void addDatosGlobales(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        try {
            if (auth != null && auth.isAuthenticated() && !auth.getName().equals("anonymousUser")) {
                String email = auth.getName();
                Usuario usuario = usuarioService.buscarPorEmail(email);

                if (usuario != null) {
                    model.addAttribute("usuario", usuario);

                    Long idUsuario = usuario.getId();
                    List<FavoritoDTO> favoritos = favoritoService.listarFavoritosPorUsuario(idUsuario);
                    model.addAttribute("favoritosNavbar", favoritos != null ? favoritos : Collections.emptyList());
                }
            } else {
                model.addAttribute("favoritosNavbar", Collections.emptyList());
            }
        } catch (Exception e) {
            model.addAttribute("favoritosNavbar", Collections.emptyList());
        }
    }

    // PARA LA SIDERBAR
    @ModelAttribute
    public void setClaseActiva(Model model, HttpServletRequest request) {
        String uri = request.getRequestURI();
        String claseActiva = "";

        if (uri.contains("/admin/usuarios")) {
            claseActiva = "usuarios";
        } else if (uri.contains("/admin/productos/nuevo")) {
            claseActiva = "agregar";
        } else if (uri.contains("/admin/productos")) {
            claseActiva = "productos";
        } else if (uri.contains("/admin/proveedor")) {
            claseActiva = "proveedor";
        } else if (uri.contains("/admin/categorias")) {
            claseActiva = "categoria";
        } else if (uri.contains("/admin/marca")) {
            claseActiva = "marca";
        } else if (uri.contains("/admin/ubicacion")) {
            claseActiva = "ubicaciones";
        } else if (uri.contains("/admin/estados")) {
            claseActiva = "estados";
        } else if (uri.contains("/admin/reportes/ventas")) {
            claseActiva = "ventas";
        } else if (uri.contains("/admin/reportes/stock")) {
            claseActiva = "stock";
        } else if (uri.contains("/admin/vistas")) {
            claseActiva = "vistas";
        } else if (uri.contains("/admin/roles")) {
            claseActiva = "roles";
        } else if (uri.equals("/") || uri.contains("/admin")) {
            claseActiva = "inicio";
        }

        model.addAttribute("claseActiva", claseActiva);
    }
}

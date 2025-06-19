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
}

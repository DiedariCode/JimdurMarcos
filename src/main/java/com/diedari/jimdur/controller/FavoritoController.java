package com.diedari.jimdur.controller;

import com.diedari.jimdur.dto.FavoritoDTO;
import com.diedari.jimdur.service.FavoritoService;
import com.diedari.jimdur.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.util.List;

@Controller
@RequestMapping("/user/favoritos")
public class FavoritoController {
    @Autowired
    private FavoritoService favoritoService;
    @Autowired
    private UsuarioService usuarioService;

    @GetMapping
    public String verFavoritos(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Long idUsuario = usuarioService.obtenerPerfilPorEmail(auth.getName()).getId();
        List<FavoritoDTO> favoritos = favoritoService.listarFavoritosPorUsuario(idUsuario);
        model.addAttribute("favoritos", favoritos);
        return "user/favoritos";
    }

    @PostMapping("/eliminar")
    public String eliminarFavorito(@RequestParam("idFavorito") Long idFavorito) {
        favoritoService.eliminarFavorito(idFavorito);
        return "redirect:/user/favoritos";
    }

    @PostMapping("/agregar")
    public String agregarFavorito(@RequestParam("idProducto") Long idProducto, RedirectAttributes redirectAttributes) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Long idUsuario = usuarioService.obtenerPerfilPorEmail(auth.getName()).getId();
        FavoritoDTO dto = new FavoritoDTO();
        dto.setIdUsuario(idUsuario);
        dto.setIdProducto(idProducto);
        try {
            favoritoService.guardarFavorito(dto);
        } catch (IllegalStateException e) {
            redirectAttributes.addFlashAttribute("errorFavorito", e.getMessage());
        }
        return "redirect:/productos";
    }

    @PostMapping("/agregar-detalle")
    public String agregarFavoritoDesdeDetalle(@RequestParam("idProducto") Long idProducto, RedirectAttributes redirectAttributes) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Long idUsuario = usuarioService.obtenerPerfilPorEmail(auth.getName()).getId();
        FavoritoDTO dto = new FavoritoDTO();
        dto.setIdUsuario(idUsuario);
        dto.setIdProducto(idProducto);
        try {
            favoritoService.guardarFavorito(dto);
        } catch (IllegalStateException e) {
            redirectAttributes.addFlashAttribute("errorFavorito", e.getMessage());
        }
        return "redirect:/user/productos/" + idProducto + "/detalle";
    }

    @PostMapping("/eliminar-detalle")
    public String eliminarFavoritoDesdeDetalle(@RequestParam("idProducto") Long idProducto) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Long idUsuario = usuarioService.obtenerPerfilPorEmail(auth.getName()).getId();
        // Buscar el favorito por usuario y producto
        List<FavoritoDTO> favoritos = favoritoService.listarFavoritosPorUsuario(idUsuario);
        favoritos.stream()
            .filter(f -> f.getIdProducto().equals(idProducto))
            .findFirst()
            .ifPresent(f -> favoritoService.eliminarFavorito(f.getId()));
        return "redirect:/user/productos/" + idProducto + "/detalle";
    }

    @PostMapping("/eliminar-navbar")
    public String eliminarFavoritoNavbar(@RequestParam("idFavorito") Long idFavorito, @RequestHeader(value = "referer", required = false) String referer) {
        favoritoService.eliminarFavorito(idFavorito);
        // Redirige a la página anterior (referer) o a inicio si no hay referer
        if (referer != null && !referer.isEmpty()) {
            return "redirect:" + referer;
        }
        return "redirect:/";
    }
} 
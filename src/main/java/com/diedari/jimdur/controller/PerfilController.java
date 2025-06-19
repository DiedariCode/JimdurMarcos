package com.diedari.jimdur.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.diedari.jimdur.dto.PerfilUsuarioDTO;

@Controller
@RequestMapping("/user")
public class PerfilController {

    // Configura el data binder ANTES de procesar la petición
    @InitBinder("usuario")
    // "No intentes asignar los valores del formulario llamados imagenPerfil y
    // imagenPortada al objeto usuario (PerfilUsuarioDTO), porque esos campos no le
    // pertenecen."
    public void initBinder(WebDataBinder binder) {
        binder.setDisallowedFields("imagenPerfil", "imagenPortada");
    }

    @GetMapping("/perfil")
    public String perfil(Model model) {
        return "fragments/perfil";
    }

    @PostMapping("/perfil")
    public String actualizarConfiguracion(
            @ModelAttribute("usuario") PerfilUsuarioDTO perfilDto,
            @RequestParam(value = "imagenPerfil", required = false) MultipartFile imagenPerfil,
            @RequestParam(value = "imagenPortada", required = false) MultipartFile imagenPortada,
            Model model) {

        model.addAttribute("mensaje", "Perfil actualizado correctamente");

        return "user/configuracion";
    }
}

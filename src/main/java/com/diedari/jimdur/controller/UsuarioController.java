package com.diedari.jimdur.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.diedari.jimdur.dto.RegistroUsuarioDTO;
import com.diedari.jimdur.service.UsuarioService;

@Controller
@RequestMapping("/user")
public class UsuarioController {
    // Registrar usuario
    @Autowired
    private UsuarioService usuarioService;

    @GetMapping("/registro")
    public String mostrarFormularioRegistro(Model model) {
        model.addAttribute("usuario", new RegistroUsuarioDTO());
        return "auth/register";
    }

    @PostMapping("/registro")
    public String procesarFormulario(@ModelAttribute("usuario") RegistroUsuarioDTO dto,
            RedirectAttributes redirectAttributes,
            Model model) {

        // Validar que contrasena y confirmContrasena sean iguales
        if (!dto.getContrasena().equals(dto.getConfirmContrasena())) {
            model.addAttribute("error", "Las contraseñas no coinciden");
            return "auth/register";
        }

        // Si todo está bien, crear el usuario
        usuarioService.crearUsuario(dto);
        redirectAttributes.addFlashAttribute("success", "Registro exitoso. Por favor, inicia sesión.");
        return "redirect:/auth/login";
    }
}
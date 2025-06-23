package com.diedari.jimdur.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.diedari.jimdur.dto.RegistroUsuarioDTO;
import com.diedari.jimdur.service.UsuarioService;

@Controller
public class UsuarioController {
    
    @Autowired
    private UsuarioService usuarioService;

    // Métodos para el registro de clientes
    @GetMapping("/user/registro")
    public String mostrarFormularioRegistro(Model model) {
        model.addAttribute("usuario", new RegistroUsuarioDTO());
        return "auth/register";
    }

    @PostMapping("/user/registro")
    public String procesarFormulario(@ModelAttribute("usuario") RegistroUsuarioDTO dto,
            RedirectAttributes redirectAttributes,
            Model model) {

        // Validar que contrasena y confirmContrasena sean iguales
        if (!dto.getContrasena().equals(dto.getConfirmContrasena())) {
            model.addAttribute("error", "Las contraseñas no coinciden");
            return "auth/register";
        }

        // Si todo está bien, crear el usuario cliente
        usuarioService.crearUsuario(dto);
        redirectAttributes.addFlashAttribute("success", "Registro exitoso. Por favor, inicia sesión.");
        return "redirect:/user/login"; 
    }

    // --- Métodos para la gestión de usuarios en el panel de admin ---

    @GetMapping("/admin/usuarios")
    public String listarUsuarios(Model model) {
        model.addAttribute("trabajadores", usuarioService.findAllTrabajadores());
        model.addAttribute("clientes", usuarioService.findAllClientes());
        return "admin/usuarios/listar";
    }

    @GetMapping("/admin/usuarios/nuevo")
    public String mostrarFormularioNuevoTrabajador(Model model) {
        model.addAttribute("trabajador", new RegistroUsuarioDTO());
        model.addAttribute("roles", usuarioService.findAllTrabajadorRoles());
        return "admin/usuarios/nuevo";
    }

    @PostMapping("/admin/usuarios/nuevo")
    public String crearNuevoTrabajador(@ModelAttribute("trabajador") RegistroUsuarioDTO dto, RedirectAttributes redirectAttributes) {
        // Simple validación de contraseña
        if (!dto.getContrasena().equals(dto.getConfirmContrasena())) {
            redirectAttributes.addFlashAttribute("error", "Las contraseñas no coinciden.");
            return "redirect:/admin/usuarios/nuevo";
        }
        usuarioService.crearTrabajador(dto);
        redirectAttributes.addFlashAttribute("success", "Trabajador creado exitosamente.");
        return "redirect:/admin/usuarios";
    }

    @GetMapping("/admin/usuarios/editar/{id}")
    public String mostrarFormularioEditarTrabajador(@PathVariable Long id, Model model) {
        model.addAttribute("trabajador", usuarioService.obtenerUsuarioParaEditar(id));
        model.addAttribute("roles", usuarioService.findAllTrabajadorRoles());
        return "admin/usuarios/editar";
    }

    @PostMapping("/admin/usuarios/editar/{id}")
    public String actualizarTrabajador(@PathVariable Long id, @ModelAttribute("trabajador") RegistroUsuarioDTO dto, RedirectAttributes redirectAttributes) {
        // Validar contraseñas solo si se está cambiando
        if (dto.getContrasena() != null && !dto.getContrasena().isEmpty() && !dto.getContrasena().equals(dto.getConfirmContrasena())) {
            redirectAttributes.addFlashAttribute("error", "Las contraseñas no coinciden.");
            return "redirect:/admin/usuarios/editar/" + id;
        }
        
        usuarioService.actualizarTrabajador(id, dto);
        redirectAttributes.addFlashAttribute("success", "Trabajador actualizado exitosamente.");
        return "redirect:/admin/usuarios";
    }
}
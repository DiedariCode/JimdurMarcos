package com.diedari.jimdur.controller;

import com.diedari.jimdur.model.Rol;
import com.diedari.jimdur.service.RolService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

@Controller
@RequestMapping("/admin/roles")
public class RolController {

    @Autowired
    private RolService rolService;

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("roles", rolService.listarTodos());
        return "admin/roles/listar";
    }

    @GetMapping("/nuevo")
    public String nuevo(Model model) {
        model.addAttribute("rol", new Rol());
        return "admin/roles/nuevo";
    }

    @PostMapping("/guardar")
    public String guardar(@ModelAttribute Rol rol, BindingResult result, Model model) {
        if (rolService.existePorNombre(rol.getNombre())) {
            result.rejectValue("nombre", "error.rol", "El nombre ya existe");
            return "admin/roles/nuevo";
        }
        rolService.guardar(rol);
        return "redirect:/admin/roles";
    }

    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Long id, Model model) {
        Optional<Rol> rolOpt = rolService.buscarPorId(id);
        if (rolOpt.isEmpty()) {
            return "redirect:/admin/roles";
        }
        model.addAttribute("rol", rolOpt.get());
        return "admin/roles/editar";
    }

    @PostMapping("/actualizar")
    public String actualizar(@ModelAttribute Rol rol, BindingResult result, Model model) {
        if (rolService.existePorNombreExcluyendoId(rol.getNombre(), rol.getId())) {
            result.rejectValue("nombre", "error.rol", "El nombre ya existe");
            return "admin/roles/editar";
        }
        rolService.guardar(rol);
        return "redirect:/admin/roles";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        if (rolService.puedeEliminar(id)) {
            rolService.eliminarPorId(id);
            redirectAttributes.addFlashAttribute("mensaje", "Rol eliminado correctamente.");
            redirectAttributes.addFlashAttribute("tipo", "success");
        } else {
            redirectAttributes.addFlashAttribute("mensaje", "No se puede eliminar este rol. Es un rol base o está en uso.");
            redirectAttributes.addFlashAttribute("tipo", "error");
        }
        return "redirect:/admin/roles";
    }
} 
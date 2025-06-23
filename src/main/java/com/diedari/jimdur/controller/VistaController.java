package com.diedari.jimdur.controller;

import com.diedari.jimdur.model.Rol;
import com.diedari.jimdur.model.Vista;
import com.diedari.jimdur.service.RolService;
import com.diedari.jimdur.service.VistaService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/admin/vistas")
public class VistaController {

    private final VistaService vistaService;
    private final RolService rolService;

    public VistaController(VistaService vistaService, RolService rolService) {
        this.vistaService = vistaService;
        this.rolService = rolService;
    }

    @GetMapping
    public String listarVistas(Model model) {
        List<Vista> vistas = vistaService.findAllWithRoles();
        model.addAttribute("vistas", vistas);
        model.addAttribute("claseActiva", "vistas");
        return "admin/vistas/listar";
    }

    @GetMapping("/editar/{id}")
    public String editarVistaForm(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        Optional<Vista> vistaOpt = vistaService.findByIdWithRoles(id);
        if (vistaOpt.isPresent()) {
            model.addAttribute("vista", vistaOpt.get());
            model.addAttribute("allRoles", rolService.listarTodos());
            return "admin/vistas/editar";
        } else {
            redirectAttributes.addFlashAttribute("mensaje", "Vista no encontrada");
            redirectAttributes.addFlashAttribute("tipo", "error");
            return "redirect:/admin/vistas";
        }
    }

    @PostMapping("/editar/{id}")
    public String editarVista(@PathVariable Long id, @RequestParam(name = "roles", required = false) List<Long> rolesIds, RedirectAttributes redirectAttributes) {
        Optional<Vista> vistaOpt = vistaService.findByIdWithRoles(id);
        if (vistaOpt.isPresent()) {
            Vista vista = vistaOpt.get();
            Set<Rol> roles = new HashSet<>();
            if (rolesIds != null) {
                List<Rol> allRoles = rolService.listarTodos();
                roles = rolesIds.stream()
                        .map(rolId -> allRoles.stream().filter(r -> r.getId().equals(rolId)).findFirst())
                        .filter(Optional::isPresent)
                        .map(Optional::get)
                        .collect(Collectors.toSet());
            }
            vista.setRoles(roles);
            vistaService.save(vista);
            redirectAttributes.addFlashAttribute("mensaje", "Roles de la vista actualizados exitosamente.");
            redirectAttributes.addFlashAttribute("tipo", "success");
        } else {
            redirectAttributes.addFlashAttribute("mensaje", "Vista no encontrada");
            redirectAttributes.addFlashAttribute("tipo", "error");
        }
        return "redirect:/admin/vistas";
    }
} 
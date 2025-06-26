package com.diedari.jimdur.controller;

import com.diedari.jimdur.model.Rol;
import com.diedari.jimdur.service.RolService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.validation.Valid;
import java.util.List;
import java.util.Set;

@Controller
@RequestMapping("/admin/roles")
@RequiredArgsConstructor
@PreAuthorize("hasAuthority('GESTIONAR_ROLES')")
public class RolController {

    private final RolService rolService;

    @GetMapping
    public String listar(Model model) {
        List<Rol> roles = rolService.findAll();
        model.addAttribute("roles", roles);
        return "admin/roles/listar";
    }

    @GetMapping("/nuevo")
    public String mostrarFormularioNuevo(Model model) {
        model.addAttribute("rol", new Rol());
        model.addAttribute("permisos", rolService.getPermisosDisponibles());
        return "admin/roles/nuevo";
    }

    @PostMapping("/guardar")
    public String guardar(@Valid @ModelAttribute Rol rol, 
                         BindingResult result, 
                         @RequestParam(required = false) Set<Long> permisos,
                         RedirectAttributes redirectAttributes,
                         Model model) {
        
        if (result.hasErrors()) {
            model.addAttribute("permisos", rolService.getPermisosDisponibles());
            return "admin/roles/nuevo";
        }

        // Verificar si el nombre ya existe
        if (rolService.existsByNombre(rol.getNombre())) {
            result.rejectValue("nombre", "error.rol", "Ya existe un rol con este nombre");
            model.addAttribute("permisos", rolService.getPermisosDisponibles());
            return "admin/roles/nuevo";
        }

        try {
            Rol rolGuardado = rolService.save(rol);
            
            // Asignar permisos si se proporcionaron
            if (permisos != null && !permisos.isEmpty()) {
                rolService.asignarPermisos(rolGuardado.getId(), permisos);
            }
            
            redirectAttributes.addFlashAttribute("mensaje", "Rol creado exitosamente");
            redirectAttributes.addFlashAttribute("tipoMensaje", "success");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensaje", "Error al crear el rol: " + e.getMessage());
            redirectAttributes.addFlashAttribute("tipoMensaje", "error");
        }

        return "redirect:/admin/roles";
    }

    @GetMapping("/editar/{id}")
    public String mostrarFormularioEditar(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        return rolService.findById(id)
                .map(rol -> {
                    model.addAttribute("rol", rol);
                    model.addAttribute("permisos", rolService.getPermisosDisponibles());
                    return "admin/roles/editar";
                })
                .orElseGet(() -> {
                    redirectAttributes.addFlashAttribute("mensaje", "Rol no encontrado");
                    redirectAttributes.addFlashAttribute("tipoMensaje", "error");
                    return "redirect:/admin/roles";
                });
    }

    @PostMapping("/actualizar/{id}")
    public String actualizar(@PathVariable Long id,
                           @Valid @ModelAttribute Rol rol,
                           BindingResult result,
                           @RequestParam(required = false) Set<Long> permisos,
                           RedirectAttributes redirectAttributes,
                           Model model) {
        
        if (result.hasErrors()) {
            model.addAttribute("permisos", rolService.getPermisosDisponibles());
            return "admin/roles/editar";
        }

        // Verificar si el nombre ya existe para otro rol
        if (rolService.existsByNombreAndIdNot(rol.getNombre(), id)) {
            result.rejectValue("nombre", "error.rol", "Ya existe otro rol con este nombre");
            model.addAttribute("permisos", rolService.getPermisosDisponibles());
            return "admin/roles/editar";
        }

        try {
            rol.setId(id);
            Rol rolActualizado = rolService.save(rol);
            
            // Actualizar permisos
            rolService.asignarPermisos(rolActualizado.getId(), permisos != null ? permisos : Set.of());
            
            redirectAttributes.addFlashAttribute("mensaje", "Rol actualizado exitosamente");
            redirectAttributes.addFlashAttribute("tipoMensaje", "success");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensaje", "Error al actualizar el rol: " + e.getMessage());
            redirectAttributes.addFlashAttribute("tipoMensaje", "error");
        }

        return "redirect:/admin/roles";
    }

    @PostMapping("/activar/{id}")
    public String activar(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            rolService.activar(id);
            redirectAttributes.addFlashAttribute("mensaje", "Rol activado exitosamente");
            redirectAttributes.addFlashAttribute("tipoMensaje", "success");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensaje", "Error al activar el rol: " + e.getMessage());
            redirectAttributes.addFlashAttribute("tipoMensaje", "error");
        }
        return "redirect:/admin/roles";
    }

    @PostMapping("/desactivar/{id}")
    public String desactivar(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            rolService.desactivar(id);
            redirectAttributes.addFlashAttribute("mensaje", "Rol desactivado exitosamente");
            redirectAttributes.addFlashAttribute("tipoMensaje", "success");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensaje", "Error al desactivar el rol: " + e.getMessage());
            redirectAttributes.addFlashAttribute("tipoMensaje", "error");
        }
        return "redirect:/admin/roles";
    }

    @GetMapping("/permisos/{id}")
    public String mostrarPermisos(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        return rolService.findById(id)
                .map(rol -> {
                    model.addAttribute("rol", rol);
                    model.addAttribute("todosLosPermisos", rolService.getPermisosDisponibles());
                    return "admin/roles/permisos";
                })
                .orElseGet(() -> {
                    redirectAttributes.addFlashAttribute("mensaje", "Rol no encontrado");
                    redirectAttributes.addFlashAttribute("tipoMensaje", "error");
                    return "redirect:/admin/roles";
                });
    }

    @PostMapping("/asignar-permisos/{id}")
    public String asignarPermisos(@PathVariable Long id,
                                @RequestParam(required = false) Set<Long> permisos,
                                RedirectAttributes redirectAttributes) {
        try {
            rolService.asignarPermisos(id, permisos != null ? permisos : Set.of());
            redirectAttributes.addFlashAttribute("mensaje", "Permisos asignados exitosamente");
            redirectAttributes.addFlashAttribute("tipoMensaje", "success");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensaje", "Error al asignar permisos: " + e.getMessage());
            redirectAttributes.addFlashAttribute("tipoMensaje", "error");
        }
        return "redirect:/admin/roles";
    }

    // API REST para uso con JavaScript
    @GetMapping("/api")
    @ResponseBody
    public ResponseEntity<List<Rol>> listarApi() {
        return ResponseEntity.ok(rolService.findAllActivos());
    }

    @GetMapping("/api/{id}")
    @ResponseBody
    public ResponseEntity<Rol> obtenerApi(@PathVariable Long id) {
        return rolService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
} 
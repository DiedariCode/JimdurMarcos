package com.diedari.jimdur.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.diedari.jimdur.dto.UbicacionDTO;
import com.diedari.jimdur.model.Ubicaciones;
import com.diedari.jimdur.service.UbicacionService;

@Controller
@RequestMapping("/admin/ubicacion")
public class UbicacionController {

    @Autowired
    private UbicacionService ubicacionService;

    @GetMapping
    public String listarUbicaciones(Model model) {
        List<Ubicaciones> ubicaciones = ubicacionService.listarUbicaciones();
        model.addAttribute("ubicaciones", ubicaciones);
        model.addAttribute("claseActiva", "ubicaciones");
        return "/admin/ubicacion/listar";
    }

    @GetMapping("/agregar")
    public String mostrarFormularioNuevo(Model model) {
        model.addAttribute("ubicacion", new Ubicaciones());
        model.addAttribute("claseActiva", "ubicaciones");
        return "/admin/ubicacion/nuevo";
    }

    @PostMapping("/agregar")
    public String guardarUbicacion(@ModelAttribute Ubicaciones ubicacion, RedirectAttributes redirectAttributes) {
        try {
            ubicacionService.guardarUbicacion(ubicacion);
            redirectAttributes.addFlashAttribute("mensaje", "Ubicación guardada exitosamente");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al guardar la ubicación: " + e.getMessage());
        }
        return "redirect:/admin/ubicacion";
    }

    @GetMapping("/editar/{id}")
    public String mostrarFormularioEditar(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        try {
            Ubicaciones ubicacion = ubicacionService.obtenerUbicacionPorId(id);
            if (ubicacion == null) {
                redirectAttributes.addFlashAttribute("error", "No se encontró la ubicación con ID: " + id);
                return "redirect:/admin/ubicacion";
            }
            model.addAttribute("ubicacion", ubicacion);
            model.addAttribute("claseActiva", "ubicaciones");
            return "/admin/ubicacion/editar";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al cargar la ubicación: " + e.getMessage());
            return "redirect:/admin/ubicacion";
        }
    }

    @PostMapping("/actualizar")
    public String actualizarUbicacion(@ModelAttribute Ubicaciones ubicacion, RedirectAttributes redirectAttributes) {
        try {
            ubicacionService.actualizarUbicacion(ubicacion);
            redirectAttributes.addFlashAttribute("mensaje", "Ubicación actualizada exitosamente");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al actualizar la ubicación: " + e.getMessage());
        }
        return "redirect:/admin/ubicacion";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminarUbicacion(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            ubicacionService.eliminarUbicacion(id);
            redirectAttributes.addFlashAttribute("mensaje", "Ubicación eliminada exitosamente");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al eliminar la ubicación: " + e.getMessage());
        }
        return "redirect:/admin/ubicacion";
    }

    // API REST para obtener todas las ubicaciones
    @GetMapping("/api")
    @ResponseBody
    @PreAuthorize("hasAuthority('LEER_INVENTARIO')")
    public ResponseEntity<List<UbicacionDTO>> obtenerTodasLasUbicaciones() {
        try {
            List<UbicacionDTO> ubicacionesDTO = ubicacionService.listarUbicacionesConOcupacion();
            return ResponseEntity.ok(ubicacionesDTO);
        } catch (Exception e) {
            e.printStackTrace(); // Para debug
            return ResponseEntity.internalServerError().build();
        }
    }

    // API REST para obtener una ubicación específica con información de ocupación
    @GetMapping("/api/{id}")
    @ResponseBody
    @PreAuthorize("hasAuthority('LEER_INVENTARIO')")
    public ResponseEntity<UbicacionDTO> obtenerUbicacionConOcupacion(@PathVariable Long id) {
        try {
            UbicacionDTO ubicacion = ubicacionService.obtenerUbicacionConOcupacion(id);
            return ubicacion != null ? ResponseEntity.ok(ubicacion) : ResponseEntity.notFound().build();
        } catch (Exception e) {
            e.printStackTrace(); // Para debug
            return ResponseEntity.internalServerError().build();
        }
    }

    // API REST para validar capacidad antes de asignar stock
    @GetMapping("/api/{id}/validar-capacidad/{cantidad}")
    @ResponseBody
    @PreAuthorize("hasAuthority('LEER_INVENTARIO')")
    public ResponseEntity<Map<String, Object>> validarCapacidad(@PathVariable Long id, @PathVariable Integer cantidad) {
        try {
            boolean esValido = ubicacionService.validarCapacidadDisponible(id, cantidad);
            Integer espacioDisponible = ubicacionService.obtenerEspacioDisponible(id);
            Double porcentajeOcupacion = ubicacionService.calcularPorcentajeOcupacion(id);
            
            Map<String, Object> respuesta = new HashMap<>();
            respuesta.put("esValido", esValido);
            respuesta.put("espacioDisponible", espacioDisponible);
            respuesta.put("porcentajeOcupacion", porcentajeOcupacion);
            respuesta.put("cantidad", cantidad);
            
            return ResponseEntity.ok(respuesta);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }
}

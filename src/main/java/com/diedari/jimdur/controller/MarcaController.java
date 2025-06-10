package com.diedari.jimdur.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.diedari.jimdur.model.Marca;
import com.diedari.jimdur.service.MarcaService;

@Controller
@RequestMapping("/admin/marca")
public class MarcaController {
    @Autowired
    private MarcaService marcaService;

    @GetMapping
    public String listarMarcas(
        Model model,
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "5") int size,
        @RequestParam(defaultValue = "nombreMarca") String sortField,
        @RequestParam(defaultValue = "asc") String sortDirection,
        @RequestParam(required = false) String nombreMarca,
        @RequestParam(required = false) String estadoMarca
    ) {
        try {

            // Convertir strings vacíos a null para los filtros
            nombreMarca = (nombreMarca != null && !nombreMarca.trim().isEmpty()) ? nombreMarca.trim() : null;
            estadoMarca = (estadoMarca != null && !estadoMarca.trim().isEmpty()) ? estadoMarca.trim() : null;

            Sort sort = Sort.by(sortDirection.equals("asc") ? Sort.Direction.ASC : Sort.Direction.DESC, sortField);
            Pageable pageable = PageRequest.of(page, size, sort);
            
            Page<Marca> pageMarca = marcaService.obtenerMarcasFiltradas(nombreMarca, estadoMarca, pageable);

            model.addAttribute("marcas", pageMarca.getContent());
            model.addAttribute("currentPage", page);
            model.addAttribute("totalPages", pageMarca.getTotalPages());
            model.addAttribute("totalItems", pageMarca.getTotalElements());
            model.addAttribute("sortField", sortField);
            model.addAttribute("sortDirection", sortDirection);
            model.addAttribute("reverseSortDirection", sortDirection.equals("asc") ? "desc" : "asc");
            model.addAttribute("size", size);
            
            // Mantener los filtros en el modelo
            model.addAttribute("nombreMarca", nombreMarca != null ? nombreMarca : "");
            model.addAttribute("estadoMarca", estadoMarca != null ? estadoMarca : "");

            return "admin/marca/listar";
        } catch (Exception e) {
            model.addAttribute("mensaje", "Error al cargar la lista de marcas: " + e.getMessage());
            model.addAttribute("tipo", "error");
            return "admin/marca/listar";
        }
    }
    
    @GetMapping("/agregar")
    public String agregarMarca(Model model) {
        model.addAttribute("marca", new Marca());
        return "admin/marca/nueva";
    }

    @PostMapping("/agregar")
    public String guardarMarca(@ModelAttribute Marca marca, RedirectAttributes redirectAttributes) {
        try {
            marcaService.guardarMarcaNuevo(marca);
            redirectAttributes.addFlashAttribute("mensaje", "Marca agregada exitosamente");
            redirectAttributes.addFlashAttribute("tipo", "success");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensaje", "Error al guardar la marca: " + e.getMessage());
            redirectAttributes.addFlashAttribute("tipo", "error");
        }
        return "redirect:/admin/marca";
    } 

    @GetMapping("/editar/{id}")
    public String editarMarcaForm(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        try {
            Marca marca = marcaService.obtenerMarcaPorId(id);
            if (marca != null) {
                model.addAttribute("marca", marca);
                return "admin/marca/editar";
            } else {
                redirectAttributes.addFlashAttribute("mensaje", "Marca no encontrada");
                redirectAttributes.addFlashAttribute("tipo", "error");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensaje", "Error al cargar la marca: " + e.getMessage());
            redirectAttributes.addFlashAttribute("tipo", "error");
        }
        return "redirect:/admin/marca";
    }

    @PostMapping("/editar/{id}")
    public String editarMarca(@PathVariable Long id, @ModelAttribute Marca marca, RedirectAttributes redirectAttributes) {
        try {
            Marca actual = marcaService.obtenerMarcaPorId(id);
            if (actual != null) {
                actual.setNombreMarca(marca.getNombreMarca());
                actual.setDescripcionMarca(marca.getDescripcionMarca());
                actual.setLogourlMarca(marca.getLogourlMarca());
                actual.setPaisOrigenMarca(marca.getPaisOrigenMarca());
                actual.setSitioWebMarca(marca.getSitioWebMarca());
                actual.setEstadoMarca(marca.getEstadoMarca());
                marcaService.actualizarMarca(actual);
                redirectAttributes.addFlashAttribute("mensaje", "Marca actualizada exitosamente");
                redirectAttributes.addFlashAttribute("tipo", "success");
            } else {
                redirectAttributes.addFlashAttribute("mensaje", "Marca no encontrada");
                redirectAttributes.addFlashAttribute("tipo", "error");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensaje", "Error al actualizar la marca: " + e.getMessage());
            redirectAttributes.addFlashAttribute("tipo", "error");
        }
        return "redirect:/admin/marca";
    }
    
    @PostMapping("/eliminar/{id}")
    public String eliminarMarca(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            marcaService.eliminarMarca(id);
            redirectAttributes.addFlashAttribute("mensaje", "Marca eliminada exitosamente");
            redirectAttributes.addFlashAttribute("tipo", "success");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensaje", "Error al eliminar la marca: " + e.getMessage());
            redirectAttributes.addFlashAttribute("tipo", "error");
        }
        return "redirect:/admin/marca";
    }
}

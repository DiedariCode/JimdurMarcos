package com.diedari.jimdur.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.diedari.jimdur.model.Categoria;
import com.diedari.jimdur.service.CategoriaService;
import com.diedari.jimdur.service.ProductoService;

@Controller
@RequestMapping("/admin/categorias")
@PreAuthorize("hasAuthority('LEER_CATEGORIAS')")
public class CategoriaController {

    private static final Logger logger = LoggerFactory.getLogger(CategoriaController.class);

    @Autowired
    private ProductoService productoService;

    @Autowired
    private CategoriaService categoriaService;

    @GetMapping
    public String listarCategorias(
        Model model,
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "5") int size,
        @RequestParam(defaultValue = "nombreCategoria") String sortField,
        @RequestParam(defaultValue = "asc") String sortDirection,
        @RequestParam(required = false) String nombreCategoria,
        @RequestParam(required = false) String estadoCategoria
    ) {
        try {
            logger.debug("Listando categorías con parámetros: page={}, size={}, sortField={}, sortDirection={}", 
                page, size, sortField, sortDirection);
            logger.debug("Filtros: nombre={}, estado={}", nombreCategoria, estadoCategoria);

            // Convertir strings vacíos a null para los filtros
            nombreCategoria = (nombreCategoria != null && !nombreCategoria.trim().isEmpty()) ? nombreCategoria.trim() : null;
            estadoCategoria = (estadoCategoria != null && !estadoCategoria.trim().isEmpty()) ? estadoCategoria.trim() : null;

            Sort sort = Sort.by(sortDirection.equals("asc") ? Sort.Direction.ASC : Sort.Direction.DESC, sortField);
            Pageable pageable = PageRequest.of(page, size, sort);
            
            Page<Categoria> pageCategoria = categoriaService.obtenerCategoriasFiltradas(nombreCategoria, estadoCategoria, pageable);

            logger.debug("Total de categorías encontradas: {}", pageCategoria.getTotalElements());
            logger.debug("Número de páginas: {}", pageCategoria.getTotalPages());

            model.addAttribute("categorias", pageCategoria.getContent());
            model.addAttribute("currentPage", page);
            model.addAttribute("totalPages", pageCategoria.getTotalPages());
            model.addAttribute("totalItems", pageCategoria.getTotalElements());
            model.addAttribute("sortField", sortField);
            model.addAttribute("sortDirection", sortDirection);
            model.addAttribute("reverseSortDirection", sortDirection.equals("asc") ? "desc" : "asc");
            model.addAttribute("size", size);
            
            // Mantener los filtros en el modelo
            model.addAttribute("nombreCategoria", nombreCategoria != null ? nombreCategoria : "");
            model.addAttribute("estadoCategoria", estadoCategoria != null ? estadoCategoria : "");
            model.addAttribute("claseActiva", "categoria");

            return "admin/categoria/listar";
        } catch (Exception e) {
            logger.error("Error al listar categorías", e);
            model.addAttribute("mensaje", "Error al cargar la lista de categorías: " + e.getMessage());
            model.addAttribute("tipo", "error");
            return "admin/categoria/listar";
        }
    }

    // Esto es para agregar una nueva categoria
    @GetMapping("/agregar")
    @PreAuthorize("hasAuthority('CREAR_CATEGORIAS')")
    public String nuevaCategoriaForm(Model model) {
        model.addAttribute("categoria", new Categoria());
        model.addAttribute("productos", productoService.obtenerTodosLosProductos()); // Vista para agregar categoria
        return "admin/categoria/nueva";
    }

    // Esto es para guardar una nueva categoria
    @PostMapping("/agregar")
    @PreAuthorize("hasAuthority('CREAR_CATEGORIAS')")
    public String guardarCategoria(@ModelAttribute Categoria categoria, RedirectAttributes redirectAttributes) {
        try {
            categoriaService.crearCategoria(categoria);
            redirectAttributes.addFlashAttribute("mensaje", "Categoría agregada exitosamente");
            redirectAttributes.addFlashAttribute("tipo", "success");
        } catch (Exception e) {
            logger.error("Error al guardar categoría", e);
            redirectAttributes.addFlashAttribute("mensaje", "Error al guardar la categoría: " + e.getMessage());
            redirectAttributes.addFlashAttribute("tipo", "error");
        }
        return "redirect:/admin/categorias";
    }

    // Esto es para editar una categoria por su ID
    @GetMapping("/editar/{id}")
    @PreAuthorize("hasAuthority('EDITAR_CATEGORIAS')")
    public String editarCategoriaForm(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        try {
            Categoria categoria = categoriaService.obtenerCategoriaPorId(id);
            if (categoria != null) {
                model.addAttribute("categoria", categoria);
                model.addAttribute("productos", productoService.obtenerTodosLosProductos()); // Vista para editar categoria
                return "admin/categoria/editar";
            } else {
                redirectAttributes.addFlashAttribute("mensaje", "Categoría no encontrada");
                redirectAttributes.addFlashAttribute("tipo", "error");
            }
        } catch (Exception e) {
            logger.error("Error al cargar categoría para editar", e);
            redirectAttributes.addFlashAttribute("mensaje", "Error al cargar la categoría: " + e.getMessage());
            redirectAttributes.addFlashAttribute("tipo", "error");
        }
        return "redirect:/admin/categorias";
    }

    // Esto es para guardar los cambios de la categoria editada
    @PostMapping("/editar/{id}")
    @PreAuthorize("hasAuthority('EDITAR_CATEGORIAS')")
    public String editarCategoria(@PathVariable Long id, @ModelAttribute Categoria categoria, RedirectAttributes redirectAttributes) {
        try {
            Categoria categoriaExistente = categoriaService.obtenerCategoriaPorId(id);
            if (categoriaExistente != null) {
                categoriaExistente.setNombreCategoria(categoria.getNombreCategoria());
                categoriaExistente.setDescripcionCategoria(categoria.getDescripcionCategoria());
                categoriaExistente.setIconoCategoria(categoria.getIconoCategoria());
                categoriaExistente.setEstadoActiva(categoria.getEstadoActiva());
                categoriaService.actualizarCategoria(categoriaExistente);
                redirectAttributes.addFlashAttribute("mensaje", "Categoría actualizada exitosamente");
                redirectAttributes.addFlashAttribute("tipo", "success");
            } else {
                redirectAttributes.addFlashAttribute("mensaje", "Categoría no encontrada");
                redirectAttributes.addFlashAttribute("tipo", "error");
            }
        } catch (Exception e) {
            logger.error("Error al actualizar categoría", e);
            redirectAttributes.addFlashAttribute("mensaje", "Error al actualizar la categoría: " + e.getMessage());
            redirectAttributes.addFlashAttribute("tipo", "error");
        }
        return "redirect:/admin/categorias";
    }

    // Esto es para eliminar una categoria por su ID
    @GetMapping("/eliminar/{id}")
    @PreAuthorize("hasAuthority('DESACTIVAR_CATEGORIAS')")
    public String eliminarCategoria(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            Categoria categoria = categoriaService.obtenerCategoriaPorId(id);
            if (categoria != null) {
                categoriaService.eliminarCategoriaPorId(id);
                redirectAttributes.addFlashAttribute("mensaje", "Categoría eliminada exitosamente");
                redirectAttributes.addFlashAttribute("tipo", "success");
            } else {
                redirectAttributes.addFlashAttribute("mensaje", "Categoría no encontrada");
                redirectAttributes.addFlashAttribute("tipo", "error");
            }
        } catch (Exception e) {
            logger.error("Error al eliminar categoría", e);
            redirectAttributes.addFlashAttribute("mensaje", "Error al eliminar la categoría: " + e.getMessage());
            redirectAttributes.addFlashAttribute("tipo", "error");
        }
        return "redirect:/admin/categorias";
    }
}

package com.diedari.jimdur.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.diedari.jimdur.dto.AgregarProveedorDTO;
import com.diedari.jimdur.mapper.ProveedorMapper;
import com.diedari.jimdur.model.Proveedor;
import com.diedari.jimdur.service.ProveedorService;

import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller
@RequestMapping("/admin/proveedor")
public class ProveedorController {

    private static final Logger logger = LoggerFactory.getLogger(ProveedorController.class);

    @Autowired
    private ProveedorService proveedorService;

    @GetMapping
    public String listarProveedores(Model model) {
        model.addAttribute("proveedores", proveedorService.obtenerTodosLosProveedores());
        return "admin/proveedor/listar";
    }

    @GetMapping("/agregar")
    public String mostrarFormularioNuevo(Model model) {
        model.addAttribute("proveedorDTO", new AgregarProveedorDTO());
        return "admin/proveedor/nuevo";
    }

    @PostMapping("/agregar")
    public String agregarProveedor(@Valid @ModelAttribute AgregarProveedorDTO proveedorDTO, 
                                 BindingResult result,
                                 Model model,
                                 RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            logger.warn("Errores de validación al agregar proveedor: {}", result.getAllErrors());
            model.addAttribute("proveedorDTO", proveedorDTO);
            return "admin/proveedor/nuevo";
        }

        try {
            Proveedor proveedor = ProveedorMapper.toEntity(proveedorDTO);
            proveedorService.guardarProveedor(proveedor);
            redirectAttributes.addFlashAttribute("mensaje", "Proveedor agregado exitosamente");
            redirectAttributes.addFlashAttribute("tipo", "success");
            return "redirect:/admin/proveedor";
        } catch (Exception e) {
            logger.error("Error al agregar proveedor", e);
            model.addAttribute("proveedorDTO", proveedorDTO);
            model.addAttribute("mensaje", "Error al agregar el proveedor: " + e.getMessage());
            model.addAttribute("tipo", "error");
            return "admin/proveedor/nuevo";
        }
    }

    @GetMapping("/{id}/editar")
    public String mostrarFormularioEditar(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        logger.debug("Mostrando formulario de edición para proveedor ID: {}", id);
        try {
            Proveedor proveedor = proveedorService.obtenerProveedorPorId(id);
            if (proveedor == null) {
                logger.warn("Proveedor no encontrado con ID: {}", id);
                redirectAttributes.addFlashAttribute("mensaje", "Proveedor no encontrado");
                redirectAttributes.addFlashAttribute("tipo", "error");
                return "redirect:/admin/proveedor";
            }

            AgregarProveedorDTO proveedorDTO = ProveedorMapper.toDTO(proveedor);
            logger.debug("ProveedorDTO creado: {}", proveedorDTO);
            model.addAttribute("proveedorDTO", proveedorDTO);
            return "admin/proveedor/editar";
        } catch (Exception e) {
            logger.error("Error al cargar proveedor para editar", e);
            redirectAttributes.addFlashAttribute("mensaje", "Error al cargar el proveedor: " + e.getMessage());
            redirectAttributes.addFlashAttribute("tipo", "error");
            return "redirect:/admin/proveedor";
        }
    }

    @PostMapping("/{id}/editar")
    public String editarProveedor(@PathVariable Long id, 
                                @Valid @ModelAttribute("proveedorDTO") AgregarProveedorDTO proveedorDTO,
                                BindingResult result,
                                Model model,
                                RedirectAttributes redirectAttributes) {
        logger.debug("Iniciando edición de proveedor ID: {}", id);
        logger.debug("DTO recibido: {}", proveedorDTO);
        
        if (result.hasErrors()) {
            logger.warn("Errores de validación al editar proveedor: {}", result.getAllErrors());
            model.addAttribute("proveedorDTO", proveedorDTO);
            model.addAttribute("mensaje", "Por favor corrija los errores en el formulario");
            model.addAttribute("tipo", "error");
            return "admin/proveedor/editar";
        }

        try {
            Proveedor proveedorExistente = proveedorService.obtenerProveedorPorId(id);
            if (proveedorExistente == null) {
                logger.warn("Proveedor no encontrado con ID: {}", id);
                redirectAttributes.addFlashAttribute("mensaje", "Proveedor no encontrado");
                redirectAttributes.addFlashAttribute("tipo", "error");
                return "redirect:/admin/proveedor";
            }

            logger.debug("Proveedor existente encontrado: {}", proveedorExistente);
            Proveedor proveedorActualizado = ProveedorMapper.toEntity(proveedorDTO);
            proveedorActualizado.setIdProveedor(id);
            
            // Aseguramos que las direcciones mantengan la referencia al proveedor
            if (proveedorActualizado.getDirecciones() != null) {
                proveedorActualizado.getDirecciones().forEach(d -> {
                    d.setProveedor(proveedorActualizado);
                    logger.debug("Dirección configurada: {}", d);
                });
            }
            
            proveedorService.guardarProveedor(proveedorActualizado);
            logger.info("Proveedor actualizado exitosamente: {}", proveedorActualizado);
            redirectAttributes.addFlashAttribute("mensaje", "Proveedor actualizado exitosamente");
            redirectAttributes.addFlashAttribute("tipo", "success");
            return "redirect:/admin/proveedor";
        } catch (Exception e) {
            logger.error("Error al actualizar proveedor", e);
            model.addAttribute("proveedorDTO", proveedorDTO);
            model.addAttribute("mensaje", "Error al actualizar el proveedor: " + e.getMessage());
            model.addAttribute("tipo", "error");
            return "admin/proveedor/editar";
        }
    }

    @PostMapping("/eliminar/{id}")
    public String eliminarProveedor(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            proveedorService.eliminarProveedor(id);
            redirectAttributes.addFlashAttribute("mensaje", "Proveedor eliminado exitosamente");
            redirectAttributes.addFlashAttribute("tipo", "success");
        } catch (Exception e) {
            logger.error("Error al eliminar proveedor", e);
            redirectAttributes.addFlashAttribute("mensaje", "Error al eliminar el proveedor: " + e.getMessage());
            redirectAttributes.addFlashAttribute("tipo", "error");
        }
        return "redirect:/admin/proveedor";
    }

    @PostMapping("/{id}/cambiar-estado")
    public String cambiarEstado(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            Proveedor proveedor = proveedorService.obtenerProveedorPorId(id);
            if (proveedor == null) {
                redirectAttributes.addFlashAttribute("mensaje", "Proveedor no encontrado");
                redirectAttributes.addFlashAttribute("tipo", "error");
                return "redirect:/admin/proveedor";
            }

            // Cambiar el estado
            String nuevoEstado = "Activo".equals(proveedor.getEstadoActivo()) ? "Inactivo" : "Activo";
            proveedor.setEstadoActivo(nuevoEstado);
            proveedorService.guardarProveedor(proveedor);

            redirectAttributes.addFlashAttribute("mensaje", "Estado del proveedor actualizado exitosamente");
            redirectAttributes.addFlashAttribute("tipo", "success");
        } catch (Exception e) {
            logger.error("Error al cambiar estado del proveedor", e);
            redirectAttributes.addFlashAttribute("mensaje", "Error al cambiar el estado del proveedor: " + e.getMessage());
            redirectAttributes.addFlashAttribute("tipo", "error");
        }
        return "redirect:/admin/proveedor";
    }
}

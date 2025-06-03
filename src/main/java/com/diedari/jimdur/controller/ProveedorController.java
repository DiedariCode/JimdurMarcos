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

import com.diedari.jimdur.dto.AgregarProveedorDTO;
import com.diedari.jimdur.mapper.ProveedorMapper;
import com.diedari.jimdur.model.Proveedor;
import com.diedari.jimdur.service.ProveedorService;

@Controller
@RequestMapping("/admin/proveedor")
public class ProveedorController {

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
    public String agregarProveedor(@ModelAttribute AgregarProveedorDTO proveedorDTO) {

        Proveedor proveedor = ProveedorMapper.toEntity(proveedorDTO);

        proveedorService.guardarProveedor(proveedor);

        return "redirect:/admin/proveedor";
    }

    @PostMapping("/eliminar/{id}")
    public String eliminarProveedor(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            proveedorService.eliminarProveedor(id);
            redirectAttributes.addAttribute("mensaje", "Proveedor eliminado exitosamente");
            redirectAttributes.addAttribute("tipo", "success");
        } catch (Exception e) {
            redirectAttributes.addAttribute("mensaje", "Error al eliminar el proveedor");
            redirectAttributes.addAttribute("tipo", "error");
        }
        return "redirect:/admin/proveedor/listar";
    }

    // @PostMappings("/cambiar-estado/{id}")
    // public String cambiarEstado(@PathVariable Long id, @RequestParam String nuevoEstado,
    //         RedirectAttributes redirectAttributes) {
    //     try {
    //         proveedorService.cambiarEstado(id, nuevoEstado);
    //         redirectAttributes.addAttribute("mensaje", "Estado actualizado exitosamente");
    //         redirectAttributes.addAttribute("tipo", "success");
    //     } catch (Exception e) {
    //         redirectAttributes.addAttribute("mensaje", "Error al cambiar el estado");
    //         redirectAttributes.addAttribute("tipo", "error");
    //     }
    //     return "redirect:/admin/proveedor/listar";
    // }

}

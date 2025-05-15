package com.diedari.jimdur.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.diedari.jimdur.model.Proveedor;
import com.diedari.jimdur.service.ProveedorService;

@Controller
@RequestMapping("/admin/proveedor")
public class ProveedorController {

    @Autowired
    private ProveedorService proveedorService;

    @GetMapping
    public String listarProveedorForm(Model model) {

        List<Proveedor> proveedor = proveedorService.listarProveedores();
        model.addAttribute("claseActiva", "proveedor");

        model.addAttribute("proveedores", proveedor);

        model.addAttribute("noProveedores", proveedor.isEmpty());
        return "admin/proveedor/listar";
    }

    @GetMapping("/agregar")
    public String nuevoProveedorForm(Model model) {
        model.addAttribute("proveedor", new Proveedor());
        model.addAttribute("claseActiva", "proveedor");
        return "admin/proveedor/nuevo";
    }

    @PostMapping("/agregar")
    public String guardarProveedor(@ModelAttribute Proveedor proveedor) {
        proveedorService.guardarProveedor(proveedor); // Llama al servicio para guardar el proveedor
        return "redirect:/admin/proveedor"; // Redirige a la lista de proveedores
    }


    @GetMapping("/editar/{idProveedor}")
    public String editarProveedorForm(@PathVariable Long idProveedor, Model model) {
        Proveedor proveedorExistente = proveedorService.obtenerProveedorPorId(idProveedor);
        model.addAttribute("proveedor", proveedorExistente);
        model.addAttribute("claseActiva", "proveedor");
        return "admin/proveedor/editar";
    }

    @PostMapping("/editar/{idProveedor}")
    public String editarProveedor(@ModelAttribute Proveedor proveedor, @PathVariable Long idProveedor) {
        Proveedor proveedorExistente = proveedorService.obtenerProveedorPorId(idProveedor);
        if (proveedorExistente == null) {
            return "redirect:/admin/proveedor";
        } else {
            proveedorExistente.setNombre(proveedor.getNombre());
            proveedorExistente.setTelefono(proveedor.getTelefono());
            proveedorExistente.setCorreo(proveedor.getCorreo());
            proveedorExistente.setDireccion(proveedor.getDireccion());
            proveedorExistente.setRuc(proveedor.getRuc());
            proveedorService.guardarProveedor(proveedorExistente); // Llama al servicio para guardar el proveedor
        
        }
        return "redirect:/admin/proveedor"; // Redirige a la lista de proveedores
    }

    @GetMapping("/eliminar/{idProveedor}")
    public String eliminarProveedor(@PathVariable Long idProveedor) {
        proveedorService.eliminarProveedor(idProveedor);
        return "redirect:/admin/proveedor"; // Redirige a la lista de proveedores
    }

}

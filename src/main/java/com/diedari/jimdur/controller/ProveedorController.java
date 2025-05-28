package com.diedari.jimdur.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.diedari.jimdur.dto.AgregarProveedorDTO;
import com.diedari.jimdur.mapper.ProveedorMapper;
import com.diedari.jimdur.model.Proveedor;
import com.diedari.jimdur.service.ProveedorService;


@Controller
@RequestMapping("/admin/proveedor")
public class ProveedorController {

    @Autowired
    private ProveedorService proveedorService;

    @GetMapping("/nuevo")
    public String mostrarFormularioNuevo(Model model) {
        model.addAttribute("proveedorDTO", new AgregarProveedorDTO());
        return "admin/proveedor/nuevo";
    }

    @PostMapping("/agregar")
    public String agregarProveedor(@ModelAttribute AgregarProveedorDTO proveedorDTO) {

        Proveedor proveedor = ProveedorMapper.toEntity(proveedorDTO);

        proveedorService.guardarProveedor(proveedor);

        return "redirect:/admin/proveedor/listar";
    }
    

}

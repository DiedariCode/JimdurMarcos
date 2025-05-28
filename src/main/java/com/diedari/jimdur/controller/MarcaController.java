package com.diedari.jimdur.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.diedari.jimdur.model.Marca;
import com.diedari.jimdur.service.MarcaService;

@Controller
@RequestMapping("/admin/marca")
public class MarcaController {
    
    @Autowired
    private MarcaService marcaService;

    @GetMapping
    public String listarTodasLasMarcas(Model model){
        model.addAttribute("marcas", marcaService.listarTodasLasMarcas());

        model.addAttribute("claseActiva", "marca");

        return "admin/marca/listar";
        
    }

    @GetMapping("/filtrar/estado")
    public String filtrarMarcasPorEstado (@RequestParam("estado") String activo, Model model) {
        List<Marca> marcas;
        if ("activa".equalsIgnoreCase(activo)) {
            marcas = marcaService.obtenerMarcasPorEstado(true); // Obtenemos las activas
        } else if ("inactiva".equalsIgnoreCase(activo)) {
            marcas = marcaService.obtenerMarcasPorEstado(false); // Obtenemos las inactivas
        } else {
            marcas = marcaService.listarTodasLasMarcas(); // Si no hay filtro, mostramos todas
        }
        model.addAttribute("marcas", marcas);
        return "admin/marca/listar"; 
    }

    @GetMapping("/filtrar/nombre")
    public String filtrarMarcasPorNombre (@RequestParam("nombre") String nombre , Model model) {
        List<Marca> marcas = marcaService.obtenerMarcaPorNombres(nombre);
        model.addAttribute("marcas", marcas);
        return "admin/marca/listar";
    }
    
    @GetMapping("/agregar")
    public String agregarMarca(Model model) {
        model.addAttribute("marca", new Marca());
        return "admin/marca/nueva";
    }

    @PostMapping("/agregar")
    public String guardarMarca (@ModelAttribute Marca marca) {
        marcaService.guardarMarcaNuevo(marca);
        return "redirect:/admin/marca"; // Redirige a la lista de marcas después de guardar
    } 

    @GetMapping("/editar/{id}")
    public String getMethodName(@PathVariable Long id, Model model) {
        Marca marca = marcaService.obtenerMarcaPorId(id);
        if (marca != null) {
            model.addAttribute("marca", marca);
            return "admin/marca/editar"; // Vista para editar la marca
        } else {
            return "redirect:/admin/marca"; // Redirige si no se encuentra la marca
        }
    }

    // @PostMapping("/editar/{id}")
    // public String editarMarca(@PathVariable Long id, @ModelAttribute Marca marca) {
    //     Marca actual = marcaService.obtenerMarcaPorId(id);
    //     if (actual != null) {
    //         actual.setNombre(marca.getNombre());
    //         actual.setDescripcion(marca.getDescripcion());
    //         actual.setLogoUrl(marca.getLogoUrl());
    //         actual.setPaisOrigen(marca.getPaisOrigen());
    //         actual.setSitioWeb(marca.getSitioWeb());
    //         actual.setActivo(marca.isActivo());
    //         marcaService.actualizarMarca(actual);
    //     }
    //     return "redirect:/admin/marca"; // Redirige a la lista de marcas después de editar
    // }
    
    @DeleteMapping("/eliminar/{id}")
    public String eliminarMarca(@PathVariable Long id) {
        marcaService.eliminarMarca(id);
        return "redirect:/admin/marca"; // Redirige a la lista de marcas después de eliminar
    }
}

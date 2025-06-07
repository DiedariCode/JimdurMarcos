package com.diedari.jimdur.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
import com.diedari.jimdur.repository.MarcaRepository;
import com.diedari.jimdur.service.MarcaService;

@Controller
@RequestMapping("/admin/marca")
public class MarcaController {

    private final MarcaRepository marcaRepository;

    @Autowired
    private MarcaService marcaService;

    MarcaController(MarcaRepository marcaRepository) {
        this.marcaRepository = marcaRepository;
    }

    // Listar marcas usando pageable
    @GetMapping
    public String listarMarcasPageable(Model model,
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "5") int size,
        @RequestParam(defaultValue = "id") String sortField,
        @RequestParam(defaultValue = "asc") String sortDirection,
        @RequestParam(required = false) String nombreMarca
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection.equals("asc") ? Sort.Direction.ASC : Sort.Direction.DESC, sortField));
        Page<Marca> marcas;

        if (nombreMarca != null && !nombreMarca.isEmpty()) {
            marcas = marcaRepository.findByNombreMarcaContainingIgnoreCase(nombreMarca, pageable);
        } else {
            marcas = marcaRepository.findAll(pageable);
        }

        model.addAttribute("marcas", marcas);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", marcas.getTotalPages());
        model.addAttribute("totalItems", marcas.getTotalElements());
        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDirection", sortDirection);
        model.addAttribute("reverseSortDirection", sortDirection.equals("asc") ? "desc" : "asc");
        model.addAttribute("nombreMarca", nombreMarca);

        return "admin/marca/listar"; 
    }

    @GetMapping("/filtrar/estado")
    public String filtrarMarcasPorEstado(@RequestParam("estado") String activo, Model model) {
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
    public String filtrarMarcasPorNombre(@RequestParam("nombre") String nombre, Model model) {
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
    public String guardarMarca(@ModelAttribute Marca marca) {
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

    @PostMapping("/editar/{id}")
    public String editarMarca(@PathVariable Long id, @ModelAttribute Marca marca) {
        Marca actual = marcaService.obtenerMarcaPorId(id);
        if (actual != null) {
            actual.setNombreMarca(marca.getNombreMarca());
            actual.setDescripcionMarca(marca.getDescripcionMarca());
            actual.setLogourlMarca(marca.getLogourlMarca());
            actual.setPaisOrigenMarca(marca.getPaisOrigenMarca());
            actual.setSitioWebMarca(marca.getSitioWebMarca());
            actual.setEstadoMarca(marca.getEstadoMarca());
            marcaService.actualizarMarca(actual);
        }
        return "redirect:/admin/marca"; // Redirige a la lista de marcas después de editar
    }
    
    @PostMapping("/eliminar/{id}")
    public String eliminarMarca(@PathVariable Long id) {
        marcaService.eliminarMarca(id);
        return "redirect:/admin/marca"; // Redirige a la lista de marcas después de eliminar
    }
}

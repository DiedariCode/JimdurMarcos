package com.diedari.jimdur.controller;

import java.util.List;

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

import com.diedari.jimdur.model.Categoria;
import com.diedari.jimdur.repository.CategoriaRepository;
import com.diedari.jimdur.repository.ProductoRepository;
import com.diedari.jimdur.service.CategoriaService;
import com.diedari.jimdur.service.ProductoService;

@Controller
@RequestMapping("/admin/categorias")
public class CategoriaController {

    @Autowired
    private ProductoService productoService;

    @Autowired
    private CategoriaService categoriaService;

    @Autowired
    private CategoriaRepository categoriaRepository;

    // // Aqui se lista todas las categorias
    // @GetMapping
    // public String listarTodasLasCategorias(Model model) {
    //     List<Categoria> categorias = categoriaService.obtenerTodasLasCategorias();
    //     model.addAttribute("categorias", categorias);
    //     model.addAttribute("categoria", new Categoria()); // Para el formulario de nueva categoria
    //     model.addAttribute("productos", productoService.obtenerTodosLosProductos()); // Vista para listar categorias


    //     model.addAttribute("claseActiva", "categoria");

    //     return "admin/categoria/listar";
    // }

    // Aqui es para listar categoria usando pageable
    @GetMapping
    public String listarCategoriasPageable(Model model,
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "5") int size,
        @RequestParam(defaultValue = "id") String sortField,
        @RequestParam(defaultValue = "asc") String sortDirection,
        @RequestParam(required = false) String nombreCategoria
    ) {
        
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection.equals("asc") ? Sort.Direction.ASC : Sort.Direction.DESC, sortField));
        Page<Categoria> categorias;
        if (nombreCategoria != null && !nombreCategoria.isEmpty()) {
            categorias = categoriaRepository.findByNombreCategoriaContainingIgnoreCase(nombreCategoria, pageable);
        } else {
            categorias = categoriaRepository.findAll(pageable);
        }

        model.addAttribute("categorias", categorias);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", categorias.getTotalPages());
        model.addAttribute("totalItems", categorias.getTotalElements());
        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDirection", sortDirection);
        model.addAttribute("reverseSortDirection", sortDirection.equals("asc") ? "desc" : "asc");
        model.addAttribute("nombreCategoria", nombreCategoria);
        model.addAttribute("claseActiva", "categoria");

        return "admin/categoria/listar";
    }

    // Aqui es para filtrar las categorias por su estado
    @GetMapping("/filtrar/estado")
    public String filtrarCategoriasPorEstado(@RequestParam("estado") String estado, Model model) {
        List<Categoria> categorias;

        // Verificar el estado y filtrar las categorías
        if ("activa".equalsIgnoreCase(estado)) {
            categorias = categoriaService.obtenerCategoriaPorEstado(true); // Obtenemos las activas
        } else if ("inactiva".equalsIgnoreCase(estado)) {
            categorias = categoriaService.obtenerCategoriaPorEstado(false); // Obtenemos las inactivas
        } else {
            categorias = categoriaService.obtenerTodasLasCategorias(); // Si no hay filtro, mostramos todas
        }

        model.addAttribute("categorias", categorias);
        model.addAttribute("categoria", new Categoria()); // Para el formulario de nueva categoría
        model.addAttribute("productos", productoService.obtenerTodosLosProductos()); // Vista para listar categorías

        return "admin/categoria/listar";
    }

    @GetMapping("/filtrar/nombre")
    public String filtrarCategoriasPorNombre(@RequestParam("nombre") String nombre, Model model) {
        List<Categoria> categorias = categoriaService.obtenerCategoriaPorNombre(nombre); 
        model.addAttribute("categorias", categorias);
        model.addAttribute("categoria", new Categoria()); // Para el formulario de nueva categoria
        model.addAttribute("productos", productoService.obtenerTodosLosProductos()); // Vista para listar categorias

        return "admin/categoria/listar";
    }

    // Esto es para agregar una nueva categoria
    @GetMapping("/agregar")
    public String nuevaCategoriaForm(Model model) {
        model.addAttribute("categoria", new Categoria());
        model.addAttribute("productos", productoService.obtenerTodosLosProductos()); // Vista para agregar categoria
        return "admin/categoria/nueva";
    }

    // Esto es para guardar una nueva categoria
    @PostMapping("/agregar")
    public String guardarCategoria(@ModelAttribute Categoria categoria) {
        categoriaService.crearCategoria(categoria);
        return "redirect:/admin/categorias";
    }

    // Esto es para editar una categoria por su ID
    @GetMapping("/editar/{id}")
    public String editarCategoriaForm(@PathVariable Long id, Model model) {
        Categoria categoria = categoriaService.obtenerCategoriaPorId(id);
        if (categoria != null) {
            model.addAttribute("categoria", categoria);
            model.addAttribute("productos", productoService.obtenerTodosLosProductos()); // Vista para editar categoria
            return "admin/categoria/editar";
        } else {
            return "redirect:/admin/categorias"; // Redirige a la lista de categorias si no se encuentra la categoria
        }
    }

    // Esto es para guardar los cambios de la categoria editada
    @PostMapping("/editar/{id}")
    public String editarCategoria(@PathVariable Long id, @ModelAttribute Categoria categoria) {
        Categoria categoriaExistente = categoriaService.obtenerCategoriaPorId(id);
        if (categoriaExistente != null) {
            categoriaExistente.setNombreCategoria(categoria.getNombreCategoria());
            categoriaExistente.setDescripcionCategoria(categoria.getDescripcionCategoria());
            categoriaExistente.setIconoCategoria(categoria.getIconoCategoria());
            categoriaExistente.setEstadoActiva(categoria.getEstadoActiva());
            categoriaService.actualizarCategoria(categoriaExistente);
        }
        return "redirect:/admin/categorias"; // Redirige a la lista de categorias
    }

    // Esto es para eliminar una categoria por su ID
    @GetMapping("/eliminar/{id}")
    public String eliminarCategoria(@PathVariable Long id) {
        Categoria categoria = categoriaService.obtenerCategoriaPorId(id);
        if (categoria != null) {
            categoriaService.eliminarCategoriaPorId(id);
            ;
        }
        return "redirect:/admin/categorias"; // Redirige a la lista de categorias
    }

}

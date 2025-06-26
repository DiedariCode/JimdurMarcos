package com.diedari.jimdur.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.diedari.jimdur.dto.CompatibilidadProductoDTO;
import com.diedari.jimdur.dto.EspecificacionProductoDTO;
import com.diedari.jimdur.dto.ProductoDTO;
import com.diedari.jimdur.dto.ProductoProveedorDTO;
import com.diedari.jimdur.mapper.ProductoMapper;
import com.diedari.jimdur.model.Categoria;
import com.diedari.jimdur.model.Marca;
import com.diedari.jimdur.model.Producto;
import com.diedari.jimdur.model.Proveedor;
import com.diedari.jimdur.repository.CategoriaRepository;
import com.diedari.jimdur.repository.MarcaRepository;
import com.diedari.jimdur.repository.ProductoRepository;
import com.diedari.jimdur.repository.ProveedorRepository;
import com.diedari.jimdur.service.ProductoService;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/admin/productos")
@RequiredArgsConstructor
@PreAuthorize("hasAuthority('LEER_PRODUCTOS')")
public class ProductoController {

    private final ProductoService productoService;
    private final CategoriaRepository categoriaRepository;
    private final MarcaRepository marcaRepository;
    private final ProveedorRepository proveedorRepository;
    private final ProductoRepository productoRepository;
    private final ProductoMapper productoMapper;

    @GetMapping
    public String listarProductos(
            Model model,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "idProducto") String sortField,
            @RequestParam(defaultValue = "asc") String sortDirection,
            @RequestParam(required = false) String nombreProducto) {

        Pageable pageable = PageRequest.of(page, size,
                Sort.by(sortDirection.equals("asc") ? Sort.Direction.ASC : Sort.Direction.DESC, sortField));
        
        Page<Producto> productosPage;
        if (nombreProducto != null && !nombreProducto.isEmpty()) {
            productosPage = productoRepository.findByNombreContainingIgnoreCase(nombreProducto, pageable);
        } else {
            productosPage = productoRepository.findAll(pageable);
        }
        
        // Convertir Page<Producto> a Page<ProductoDTO>
        Page<ProductoDTO> productos = productosPage.map(productoMapper::toDTO);

        List<Categoria> categorias = categoriaRepository.findByEstadoActiva(true);
        List<Marca> marcas = marcaRepository.findByEstadoMarca(true);

        model.addAttribute("productos", productos);
        model.addAttribute("categorias", categorias);
        model.addAttribute("marcas", marcas);
        model.addAttribute("pageTitle", "Gestión de Productos");
        
        // Atributos para la paginación
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", productos.getTotalPages());
        model.addAttribute("totalItems", productos.getTotalElements());
        model.addAttribute("pageSize", size);
        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDirection", sortDirection);
        model.addAttribute("reverseSortDirection", sortDirection.equals("asc") ? "desc" : "asc");
        model.addAttribute("nombreProducto", nombreProducto);

        return "admin/productos/listar";
    }

    @GetMapping("/nuevo")
    @PreAuthorize("hasAuthority('CREAR_PRODUCTOS')")
    public String mostrarFormularioNuevo(Model model) {
        ProductoDTO producto = new ProductoDTO();
        producto.setActivo(true); // Por defecto activo

        // Inicializar listas vacías
        producto.setProveedores(new ArrayList<>());
        producto.getProveedores().add(new ProductoProveedorDTO());

        producto.setEspecificaciones(new ArrayList<>());
        producto.getEspecificaciones().add(new EspecificacionProductoDTO());

        producto.setCompatibilidades(new ArrayList<>());
        producto.getCompatibilidades().add(new CompatibilidadProductoDTO());

        cargarDatosFormulario(model, producto);
        return "admin/productos/nuevo";
    }

    @GetMapping("/{id}/editar")
    @PreAuthorize("hasAuthority('EDITAR_PRODUCTOS')")
    public String mostrarFormularioEditar(@PathVariable Long id, Model model) {
        try {
            ProductoDTO producto = productoService.obtenerProductoPorId(id);
            if (producto == null) {
                return "redirect:/admin/productos?error=not_found";
            }

            // Cargar datos necesarios para el formulario
            List<Categoria> categorias = categoriaRepository.findByEstadoActiva(true);
            List<Marca> marcas = marcaRepository.findByEstadoMarca(true);
            List<Proveedor> proveedores = proveedorRepository.findByEstadoActivo("Activo");

            // Asegurarse de que las listas nunca sean null
            if (producto.getProveedores() == null) {
                producto.setProveedores(new ArrayList<>());
            }
            if (producto.getEspecificaciones() == null) {
                producto.setEspecificaciones(new ArrayList<>());
            }
            if (producto.getCompatibilidades() == null) {
                producto.setCompatibilidades(new ArrayList<>());
            }

            // Si no hay elementos, agregar uno vacío para el formulario
            if (producto.getProveedores().isEmpty()) {
                producto.getProveedores().add(new ProductoProveedorDTO());
            }
            if (producto.getEspecificaciones().isEmpty()) {
                producto.getEspecificaciones().add(new EspecificacionProductoDTO());
            }
            if (producto.getCompatibilidades().isEmpty()) {
                producto.getCompatibilidades().add(new CompatibilidadProductoDTO());
            }

            model.addAttribute("producto", producto);
            model.addAttribute("categorias", categorias);
            model.addAttribute("marcas", marcas);
            model.addAttribute("proveedores", proveedores);
            model.addAttribute("pageTitle", "Editar Producto");

            return "admin/productos/editar";
        } catch (Exception e) {
            return "redirect:/admin/productos?error=" + e.getMessage();
        }
    }

    @PostMapping("/{id}/eliminar")
    @PreAuthorize("hasAuthority('DESACTIVAR_PRODUCTOS')")
    public String eliminarProducto(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            productoService.eliminarProducto(id);
            redirectAttributes.addFlashAttribute("success", "Producto eliminado exitosamente");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al eliminar el producto: " + e.getMessage());
        }
        return "redirect:/admin/productos";
    }

    @GetMapping("/{id}/detalle")
    public String verDetalle(@PathVariable Long id, Model model) {
        try {
            ProductoDTO producto = productoService.obtenerProductoPorId(id);
            model.addAttribute("producto", producto);
            return "admin/productos/detalle";
        } catch (RuntimeException e) {
            return "redirect:/admin/productos?error=not_found";
        }
    }

    private void cargarDatosFormulario(Model model, ProductoDTO producto) {
        List<Categoria> categorias = categoriaRepository.findByEstadoActiva(true);
        List<Marca> marcas = marcaRepository.findByEstadoMarca(true);
        List<Proveedor> proveedores = proveedorRepository.findAll();

        model.addAttribute("producto", producto);
        model.addAttribute("categorias", categorias);
        model.addAttribute("marcas", marcas);
        model.addAttribute("proveedores", proveedores);

        String pageTitle = producto.getIdProducto() != null ? "Editar Producto" : "Nuevo Producto";
        model.addAttribute("pageTitle", pageTitle);
    }
}
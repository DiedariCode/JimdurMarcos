package com.diedari.jimdur.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.diedari.jimdur.dto.CompatibilidadProductoDTO;
import com.diedari.jimdur.dto.EspecificacionProductoDTO;
import com.diedari.jimdur.dto.ProductoDTO;
import com.diedari.jimdur.dto.ProductoProveedorDTO;
import com.diedari.jimdur.model.Categoria;
import com.diedari.jimdur.model.Marca;
import com.diedari.jimdur.model.Proveedor;
import com.diedari.jimdur.repository.CategoriaRepository;
import com.diedari.jimdur.repository.MarcaRepository;
import com.diedari.jimdur.repository.ProveedorRepository;
import com.diedari.jimdur.service.ProductoService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/admin/productos")
@RequiredArgsConstructor
public class ProductoController {

    private final ProductoService productoService;
    private final CategoriaRepository categoriaRepository;
    private final MarcaRepository marcaRepository;
    private final ProveedorRepository proveedorRepository;

    @GetMapping
    public String listarProductos(Model model) {
        List<ProductoDTO> productos = productoService.obtenerTodosLosProductos();
        List<Categoria> categorias = categoriaRepository.findByEstadoActiva(true);
        List<Marca> marcas = marcaRepository.findByEstadoMarca(true);

        model.addAttribute("productos", productos);
        model.addAttribute("categorias", categorias);
        model.addAttribute("marcas", marcas);
        model.addAttribute("pageTitle", "Gestión de Productos");
        return "admin/productos/listar";
    }

    @GetMapping("/nuevo")
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
    public String mostrarFormularioEditar(@PathVariable Long id, Model model) {
        try {
            ProductoDTO producto = productoService.obtenerProductoPorId(id);
            if (producto == null) {
                return "redirect:/admin/productos?error=not_found";
            }

            // Cargar datos necesarios para el formulario
            List<Categoria> categorias = categoriaRepository.findByEstadoActiva(true);
            List<Marca> marcas = marcaRepository.findByEstadoMarca(true);
            List<Proveedor> proveedores = proveedorRepository.findAll();

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
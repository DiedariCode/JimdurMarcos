package com.diedari.jimdur.controller;

import com.diedari.jimdur.model.Producto;
import com.diedari.jimdur.model.Categoria;
import com.diedari.jimdur.service.ProductoService;
import com.diedari.jimdur.service.CategoriaService;  // Añadir el servicio de Categorías
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/productos")
public class ProductoController {

    @Autowired
    private ProductoService productoService;

    @Autowired
    private CategoriaService categoriaService;  // Inyección de dependencias para el servicio de categorías

    // Listar productos (modo administrador)
    @GetMapping("/listar")
    public String listarProductosForm(Model model) {
        List<Producto> productos = productoService.listarTodosLosProductos();
        model.addAttribute("productos", productos);
        return "productos/listar"; // El nombre de la plantilla que se mostrará
    }

    // Mostrar el formulario para crear un producto
    @GetMapping("/nuevo")
    public String nuevoProductoForm(Model model) {
        model.addAttribute("producto", new Producto());
        List<Categoria> categorias = categoriaService.obtenerTodasLasCategorias();  // Obtener todas las categorías
        model.addAttribute("categorias", categorias);  // Pasar las categorías al modelo
        return "productos/nuevo"; // El nombre de la plantilla del formulario
    }

    // Guardar un nuevo producto (POST)
    @PostMapping("/guardar")
    public String guardarProducto(@ModelAttribute Producto producto) {
        productoService.guardarProductoNuevo(producto);
        return "redirect:/productos/listar"; // Redirige después de guardar
    }

    // Editar un producto existente
    @GetMapping("/editar/{id}")
    public String editarProducto(@PathVariable Long id, Model model) {
        Producto producto = productoService.obtenerProductoPorId(id);
        if (producto != null) {
            model.addAttribute("producto", producto);
            List<Categoria> categorias = categoriaService.obtenerTodasLasCategorias();  // Obtener todas las categorías
            model.addAttribute("categorias", categorias);  // Pasar las categorías al modelo
            return "productos/editar"; // Plantilla para editar
        } else {
            return "redirect:/productos/listar"; // Si no existe, redirige a la lista
        }
    }

    // Actualizar producto (POST)
    @PostMapping("/actualizar/{id}")
    public String actualizarProducto(@PathVariable Long id, @ModelAttribute Producto producto) {
        Producto actual = productoService.obtenerProductoPorId(id);
        if (actual != null) {
            actual.setNombre(producto.getNombre());
            actual.setDescripcion(producto.getDescripcion());
            actual.setCategoria(producto.getCategoria());
            actual.setPrecio(producto.getPrecio());
            actual.setStock(producto.getStock());
            actual.setProveedor(producto.getProveedor());
            actual.setImagenURL(producto.getImagenURL());
            actual.setActivo(producto.isActivo());
            productoService.actualizarProducto(actual);
        }
        return "redirect:/productos/listar"; // Redirige después de actualizar
    }

    // Eliminar un producto
    @GetMapping("/eliminar/{id}")
    public String eliminarProducto(@PathVariable Long id) {
        productoService.eliminarProducto(id);
        return "redirect:/productos/listar"; // Redirige después de eliminar
    }
}

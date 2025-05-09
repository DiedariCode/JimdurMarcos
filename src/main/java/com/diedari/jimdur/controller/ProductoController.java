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

import com.diedari.jimdur.model.Categoria;
import com.diedari.jimdur.model.Marca;
import com.diedari.jimdur.model.Producto;
import com.diedari.jimdur.service.CategoriaService;
import com.diedari.jimdur.service.MarcaService;
import com.diedari.jimdur.service.ProductoService;

@Controller
@RequestMapping("/admin/productos")
public class ProductoController {

    @Autowired
    private ProductoService productoService;

    @Autowired
    private CategoriaService categoriaService;

    @Autowired
    private MarcaService marcaService;

    // Listar productos
    @GetMapping("/")
    public String listarProductosForm(Model model) {
        List<Producto> productos = productoService.listarTodosLosProductos();
        model.addAttribute("productos", productos);
        model.addAttribute("producto", new Producto()); // Para el formulario de nuevo producto
        model.addAttribute("categorias", categoriaService.obtenerTodasLasCategorias()); // O el método que uses

        return "admin/productos/listar"; // Usamos el layout principal
    }

    // Formulario para nuevo producto
    @GetMapping("/agregar")
    public String nuevoProductoForm(Model model) {
        model.addAttribute("producto", new Producto());
        List<Categoria> categorias = categoriaService.obtenerCategoriaPorEstado(true);
        model.addAttribute("categorias", categorias);
        List<Marca> marcas = marcaService.obtenerMarcasPorEstado(true);
        model.addAttribute("marcas", marcas);
        return "admin/productos/nuevo"; // Usamos el layout principal
    }

    // Guardar nuevo producto
    @PostMapping("/agregar")
    public String guardarProducto(@ModelAttribute Producto producto) {
        productoService.guardarProductoNuevo(producto);
        return "redirect:/admin/productos/"; // Redirige a la lista de productos
    }

    // Editar producto
    @GetMapping("/editar/{id}")
    public String editarProducto(@PathVariable Long id, Model model) {
        Producto producto = productoService.obtenerProductoPorId(id);
        if (producto != null) {
            model.addAttribute("producto", producto);

            List<Categoria> categorias = categoriaService.obtenerCategoriaPorEstado(true);
            model.addAttribute("categorias", categorias);

            List<Marca> marcas = marcaService.obtenerMarcasPorEstado(true);
            model.addAttribute("marcas", marcas);

            return "admin/productos/editar";
        } else {
            return "redirect:/admin/productos/";
        }
    }

    // Actualizar producto
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
            actual.setCategoria(producto.getCategoria());
            actual.setMarca(producto.getMarca());
            actual.setDescuento(producto.getDescuento());
            actual.setTipoDescuento(producto.getTipoDescuento());
            actual.setActivo(producto.isActivo());
            actual.calcularPrecioOferta();
            productoService.actualizarProducto(actual);
        }
        return "redirect:/admin/productos/";
    }

    // Eliminar producto
    @GetMapping("/eliminar/{id}")
    public String eliminarProducto(@PathVariable Long id) {
        productoService.eliminarProducto(id);
        return "redirect:/admin/productos/";
    }
}

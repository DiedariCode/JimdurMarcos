package com.diedari.jimdur.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.diedari.jimdur.model.Producto;
import com.diedari.jimdur.service.ProductoService;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;



@RestController
@RequestMapping("/api/productos")
public class ProductoRestController {
    // Aquí irían los métodos para manejar las peticiones HTTP relacionadas con los productos
    // Por ejemplo, métodos para obtener todos los productos, obtener un producto por ID, crear un nuevo producto, etc.
    // Estos métodos interactuarían con el servicio de productos (ProductoService) para realizar las operaciones necesarias 
    // y devolverían las respuestas adecuadas al cliente (en formato JSON, por ejemplo).

    @Autowired
    private ProductoService productoService;

    @GetMapping()
    public List<Producto> listarProductos() {
        return productoService.listarTodosLosProductos();
    }

    @GetMapping("/{id}")
    public Producto obtenerProductoPorId(@PathVariable Long id) {
        return productoService.obtenerProductoPorId(id);
    }

    @PostMapping("/crear")
    public Producto guardarProducto(@RequestBody Producto producto) {
        return productoService.guardarProductoNuevo(producto);
    }

    @PutMapping("/{id}")
    public Producto actualizar(@PathVariable Long id, @RequestBody Producto producto) {
        Producto actual = productoService.obtenerProductoPorId(id);
        if(actual != null) {
            actual.setNombre(producto.getNombre());
            actual.setDescripcion(producto.getDescripcion());
            actual.setCategoria(producto.getCategoria());
            actual.setPrecio(producto.getPrecio());
            actual.setStock(producto.getStock());
            actual.setProveedor(producto.getProveedor());
            actual.setImagenURL(producto.getImagenURL());
            actual.setActivo(producto.isActivo());
            return productoService.actualizarProducto(actual);
        } else {
            return null;
        }
    }

    @DeleteMapping("/{id}")
    public void eliminarProducto(@PathVariable Long id) {
        productoService.eliminarProducto(id);
    }
}

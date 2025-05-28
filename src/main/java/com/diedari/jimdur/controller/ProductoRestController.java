package com.diedari.jimdur.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.diedari.jimdur.model.Producto;
import com.diedari.jimdur.repository.CategoriaRepository;
import com.diedari.jimdur.repository.MarcaRepository;
import com.diedari.jimdur.repository.ProveedorRepository;
import com.diedari.jimdur.repository.UbicacionRepository;
import com.diedari.jimdur.service.ProductoService;

@RestController
@RequestMapping("/api/productos")
public class ProductoRestController {
    // Aquí irían los métodos para manejar las peticiones HTTP relacionadas con los
    // productos
    // Por ejemplo, métodos para obtener todos los productos, obtener un producto
    // por ID, crear un nuevo producto, etc.
    // Estos métodos interactuarían con el servicio de productos (ProductoService)
    // para realizar las operaciones necesarias
    // y devolverían las respuestas adecuadas al cliente (en formato JSON, por
    // ejemplo).

    @Autowired
    private ProductoService productoService;

    @Autowired
    private MarcaRepository marcaRepository;

    @Autowired
    private CategoriaRepository categoriaRepository;

    @Autowired
    private ProveedorRepository proveedorRepository;

    @Autowired
    private UbicacionRepository ubicacionRepository;

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

    // @PutMapping("/{id}")
    // public Producto actualizar(@PathVariable Long id, @RequestBody Producto producto) {
    //     Producto actual = productoService.obtenerProductoPorId(id);
    //     if (actual != null) {
    //         actual.setNombre(producto.getNombre());
    //         actual.setImagenURL(producto.getImagenURL());
    //         actual.setDescripcion(producto.getDescripcion());
    //         actual.setPrecio(producto.getPrecio());
    //         actual.setStock(producto.getStock());
    //         actual.setActivo(producto.isActivo());
    //         actual.setTipoDescuento(producto.getTipoDescuento());
    //         actual.setDescuento(producto.getDescuento());
    //         actual.setPrecioOferta(producto.getPrecioOferta());
    //         actual.setSlug(producto.getSlug());

    //         // Asociar marca y categoría existentes por ID
    //         if (producto.getMarca() != null && producto.getMarca().getId() != null) {
    //             Marca marca = marcaRepository.findById(producto.getMarca().getId()).orElse(null);
    //             actual.setMarca(marca);
    //         }

    //         if (producto.getCategoria() != null && producto.getCategoria().getId() != null) {
    //             Categoria categoria = categoriaRepository.findById(producto.getCategoria().getId()).orElse(null);
    //             actual.setCategoria(categoria);
    //         }

    //         // Asociar proveedor
    //         if (producto.getProveedor() != null && producto.getProveedor().getIdProveedor() != null) {
    //             Proveedor proveedor = proveedorRepository.findById(producto.getProveedor().getIdProveedor()).orElse(null);
    //             actual.setProveedor(proveedor);
    //         }

    //         // Asociar ubicación
    //         if (producto.getUbicacion() != null && producto.getUbicacion().getIdUbicacion() != null) {
    //             Ubicacion ubicacion = ubicacionRepository.findById(producto.getUbicacion().getIdUbicacion()).orElse(null);
    //             actual.setUbicacion(ubicacion);
    //         }

    //         return productoService.actualizarProducto(actual);
    //     } else {
    //         return null;
    //     }
    // }

    @DeleteMapping("/{id}")
    public void eliminarProducto(@PathVariable Long id) {
        productoService.eliminarProducto(id);
    }
}

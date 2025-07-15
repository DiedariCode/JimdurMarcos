package com.diedari.jimdur.service;

import java.util.List;

import com.diedari.jimdur.dto.ProductoDTO;
import com.diedari.jimdur.dto.ProductoProveedorDTO;
import com.diedari.jimdur.model.Producto;

public interface ProductoService {

    ProductoDTO guardarProducto(ProductoDTO productoDTO);

    ProductoDTO actualizarProducto(ProductoDTO productoDTO);

    List<ProductoDTO> obtenerTodosLosProductos();

    ProductoDTO obtenerProductoPorId(Long id);

    void eliminarProducto(Long id);

    boolean existeSkuProducto(String sku, Long idProducto);

    List<Producto> obtenerProductoPorEstado(boolean activo);

    Producto obtenerProductoPorSlug(String slug);

    void guardarProveedoresProducto(Long idProducto, List<ProductoProveedorDTO> proveedores);

    void eliminarImagen(Long idImagen);

    void actualizarPortada(Long idImagen);

    // Métodos para productos destacados
    List<ProductoDTO> obtenerProductosDestacados(int limite);

    List<ProductoDTO> obtenerProductosConDescuentoMinimo(Double minDescuento, int limite);
}

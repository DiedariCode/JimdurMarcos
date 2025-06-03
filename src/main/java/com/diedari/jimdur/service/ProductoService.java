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

    List<Producto> obtenerProductoPorEstado(boolean estado);

    Producto obtenerProductoPorSlug(String slug);

    void guardarProveedoresProducto(Long idProducto, List<ProductoProveedorDTO> proveedores);
}

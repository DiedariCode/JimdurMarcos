package com.diedari.jimdur.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.diedari.jimdur.model.Categoria;
import com.diedari.jimdur.model.Marca;
import com.diedari.jimdur.model.Producto;
import com.diedari.jimdur.model.Proveedor;
import com.diedari.jimdur.model.Ubicacion;
import com.diedari.jimdur.repository.CategoriaRepository;
import com.diedari.jimdur.repository.MarcaRepository;
import com.diedari.jimdur.repository.ProductoRepository;
import com.diedari.jimdur.repository.ProveedorRepository;
import com.diedari.jimdur.repository.UbicacionRepository;

@Service
public class ProductoServiceImpl implements ProductoService {

    // Implementación de los métodos de ProductoService
    // Aquí iría la lógica para interactuar con el repositorio y manejar los
    // productos

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private CategoriaRepository categoriaRepository;

    @Autowired
    private MarcaRepository marcaRepository;

    @Autowired
    private ProveedorRepository proveedorRepository;

    @Autowired
    private UbicacionRepository ubicacionRepository;

    @Override
    public Producto actualizarProducto(Producto producto) {
        return productoRepository.save(producto);
    }

    @Override
    public Producto guardarProductoNuevo(Producto producto) {
        producto.calcularPrecioOferta();

        // Validar y asignar categoría
        Categoria categoria = categoriaRepository.findById(producto.getCategoria().getId())
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada"));
        producto.setCategoria(categoria);

        // Validar y asignar marca
        Marca marca = marcaRepository.findById(producto.getMarca().getId())
                .orElseThrow(() -> new RuntimeException("Marca no encontrada"));
        producto.setMarca(marca);

        // Validar y asignar proveedor
        Proveedor proveedor = proveedorRepository.findById(producto.getProveedor().getIdProveedor())
                .orElseThrow(() -> new RuntimeException("Proveedor no encontrado"));
        producto.setProveedor(proveedor);

        // Validar y asignar ubicación
        Ubicacion ubicacion = ubicacionRepository.findById(producto.getUbicacion().getIdUbicacion())
                .orElseThrow(() -> new RuntimeException("Ubicación no encontrada"));
        producto.setUbicacion(ubicacion);
        
        // Guardar producto (ya con precio de oferta calculado)
        return productoRepository.save(producto);
    }

    @Override
    public void eliminarProducto(Long id) {
        productoRepository.deleteById(id);
    }

    @Override
    public List<Producto> listarTodosLosProductos() {
        return productoRepository.findAll();
    }

    @Override
    public Producto obtenerProductoPorId(Long id) {
        return productoRepository.findById(id).get();
    }

    @Override
    public List<Producto> listarUltimosProductos(int cantidad) {
        Pageable limit = PageRequest.of(0, cantidad);
        return productoRepository.findTopNByOrderByIdDesc(limit);
    }

    @Override
    public Producto obtenerProductoPorSlug(String slug) {
        return productoRepository.findBySlug(slug);
    }

}

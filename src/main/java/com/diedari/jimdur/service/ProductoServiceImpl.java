package com.diedari.jimdur.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.diedari.jimdur.dto.ProductoDTO;
import com.diedari.jimdur.dto.ProductoProveedorDTO;
import com.diedari.jimdur.mapper.ProductoMapper;
import com.diedari.jimdur.model.Categoria;
import com.diedari.jimdur.model.ImagenProducto;
import com.diedari.jimdur.model.Inventario;
import com.diedari.jimdur.model.Marca;
import com.diedari.jimdur.model.Producto;
import com.diedari.jimdur.model.ProductoProveedor;
import com.diedari.jimdur.model.Proveedor;
import com.diedari.jimdur.model.Ubicaciones;
import com.diedari.jimdur.repository.CategoriaRepository;
import com.diedari.jimdur.repository.ImagenProductoRepository;
import com.diedari.jimdur.repository.InventarioRepository;
import com.diedari.jimdur.repository.MarcaRepository;
import com.diedari.jimdur.repository.ProductoProveedorRepository;
import com.diedari.jimdur.repository.ProductoRepository;
import com.diedari.jimdur.repository.ProveedorRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductoServiceImpl implements ProductoService {

    private final ProductoRepository productoRepository;
    private final CategoriaRepository categoriaRepository;
    private final MarcaRepository marcaRepository;
    private final ProveedorRepository proveedorRepository;
    private final ProductoProveedorRepository productoProveedorRepository;
    private final ImagenProductoRepository imagenProductoRepository;
    private final InventarioRepository inventarioRepository;
    private final UbicacionesRepository ubicacionesRepository;
    private final ProductoMapper productoMapper;

    private final String UPLOAD_DIR = "uploads/productos/";

    @Override
    @Transactional
    public ProductoDTO guardarProducto(ProductoDTO productoDTO) {
        productoMapper.calcularPrecioOferta(productoDTO);
        Producto producto = productoMapper.toEntity(productoDTO);

        Categoria categoria = categoriaRepository.findById(productoDTO.getIdCategoria())
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada"));
        Marca marca = marcaRepository.findById(productoDTO.getIdMarca())
                .orElseThrow(() -> new RuntimeException("Marca no encontrada"));

        producto.setCategoria(categoria);
        producto.setMarca(marca);

        producto = productoRepository.save(producto);

        if (productoDTO.getImagenes() != null && !productoDTO.getImagenes().isEmpty()) {
            guardarImagenes(producto, productoDTO.getImagenes());
        }

        if (productoDTO.getProveedores() != null && !productoDTO.getProveedores().isEmpty()) {
            guardarProveedores(producto, productoDTO.getProveedores());
        }

        return productoMapper.toDTO(producto);
    }

    @Override
    @Transactional
    public ProductoDTO actualizarProducto(ProductoDTO productoDTO) {
        Producto productoExistente = productoRepository.findById(productoDTO.getIdProducto())
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

        productoMapper.calcularPrecioOferta(productoDTO);

        productoExistente.setSku(productoDTO.getSku());
        productoExistente.setNombre(productoDTO.getNombre());
        productoExistente.setDescripcion(productoDTO.getDescripcion());
        productoExistente.setPrecio(productoDTO.getPrecio());
        productoExistente.setDescuento(productoDTO.getDescuento());
        productoExistente.setPrecioOferta(productoDTO.getPrecioOferta());
        productoExistente.setActivo(productoDTO.getActivo());

        Categoria categoria = categoriaRepository.findById(productoDTO.getIdCategoria())
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada"));
        Marca marca = marcaRepository.findById(productoDTO.getIdMarca())
                .orElseThrow(() -> new RuntimeException("Marca no encontrada"));

        productoExistente.setCategoria(categoria);
        productoExistente.setMarca(marca);

        if (!productoExistente.getNombre().equals(productoDTO.getNombre())) {
            productoExistente.setSlug(generateSlug(productoDTO.getNombre()));
        }

        productoExistente = productoRepository.save(productoExistente);

        if (productoDTO.getImagenes() != null && !productoDTO.getImagenes().isEmpty()) {
            guardarImagenes(productoExistente, productoDTO.getImagenes());
        }

        if (productoDTO.getProveedores() != null) {
            actualizarProveedores(productoExistente, productoDTO.getProveedores());
        }

        return productoMapper.toDTO(productoExistente);
    }

    @Override
    public List<ProductoDTO> obtenerTodosLosProductos() {
        return productoMapper.toDTOList(productoRepository.findAll());
    }

    @Override
    public ProductoDTO obtenerProductoPorId(Long id) {
        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));
        return productoMapper.toDTO(producto);
    }

    @Override
    @Transactional
    public void eliminarProducto(Long id) {
        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

        if (producto.getImagenes() != null) {
            producto.getImagenes().forEach(imagen -> {
                try {
                    Files.deleteIfExists(Paths.get(UPLOAD_DIR + imagen.getNombreArchivo()));
                } catch (IOException ignored) {
                }
            });
        }

        productoRepository.delete(producto);
    }

    private void guardarImagenes(Producto producto, List<MultipartFile> imagenes) {
        for (MultipartFile imagen : imagenes) {
            if (!imagen.isEmpty()) {
                try {
                    String nombreArchivo = UUID.randomUUID().toString() + "_" + imagen.getOriginalFilename();
                    Path path = Paths.get(UPLOAD_DIR + nombreArchivo);
                    Files.createDirectories(path.getParent());
                    Files.write(path, imagen.getBytes());

                    ImagenProducto imagenProducto = ImagenProducto.builder()
                            .producto(producto)
                            .nombreArchivo(nombreArchivo)
                            .esPortada(producto.getImagenes() == null || producto.getImagenes().isEmpty())
                            .build();

                    imagenProductoRepository.save(imagenProducto);
                } catch (IOException e) {
                    throw new RuntimeException("Error al guardar imagen: " + e.getMessage());
                }
            }
        }
    }

    private void guardarProveedores(Producto producto, List<ProductoProveedorDTO> proveedoresDTO) {
        for (ProductoProveedorDTO proveedorDTO : proveedoresDTO) {
            if (proveedorDTO.getIdProveedor() != null) {
                Proveedor proveedor = proveedorRepository.findById(proveedorDTO.getIdProveedor())
                        .orElseThrow(() -> new RuntimeException("Proveedor no encontrado"));

                ProductoProveedor productoProveedor = ProductoProveedor.builder()
                        .idProducto(producto.getIdProducto())
                        .idProveedor(proveedor.getIdProveedor())
                        .precioCompra(proveedorDTO.getPrecioCompra())
                        .producto(producto)
                        .proveedor(proveedor)
                        .build();

                productoProveedorRepository.save(productoProveedor);

                // if (proveedorDTO.getStockInicial() != null && proveedorDTO.getStockInicial() > 0) {
                //     crearInventarioInicial(producto, proveedorDTO.getStockInicial(), proveedorDTO.getPrecioCompra());
                // }
            }
        }
    }

    private void actualizarProveedores(Producto producto, List<ProductoProveedorDTO> proveedoresDTO) {
        productoProveedorRepository.deleteByIdProducto(producto.getIdProducto());
        guardarProveedores(producto, proveedoresDTO);
    }

    // private void crearInventarioInicial(Producto producto, Integer stockInicial, Double valorUnitario) {
    //     Ubicaciones ubicacionDefecto = ubicacionesRepository.findById(1L).orElse(null);

    //     if (ubicacionDefecto != null) {
    //         Inventario inventario = Inventario.builder()
    //                 .producto(producto)
    //                 .ubicacion(ubicacionDefecto)
    //                 .cantidad(stockInicial)
    //                 .valorUnitario(valorUnitario)
    //                 .build();
    //         inventarioRepository.save(inventario);
    //     }
    // }

    private String generateSlug(String nombre) {
        if (nombre == null)
            return "";
        return nombre.toLowerCase()
                .replaceAll("[^a-z0-9\\s-]", "")
                .replaceAll("\\s+", "-")
                .replaceAll("-+", "-")
                .replaceAll("^-|-$", "");
    }

    @Override
    public boolean existeSkuProducto(String sku, Long idProducto) {
        return idProducto != null ? productoRepository.existsBySkuAndIdProductoNot(sku, idProducto)
                : productoRepository.existsBySku(sku);
    }

    // * ANTES DE IMPLEMENTAR, VERIFICAR SI SE NECESITA
    @Override
    public List<Producto> obtenerProductoPorEstado(boolean estado) {
        return productoRepository.findByActivo(estado);
    }

    @Override
    public Producto obtenerProductoPorSlug(String slug) {
        return productoRepository.findBySlug(slug);
    }
}

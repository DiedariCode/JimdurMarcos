package com.diedari.jimdur.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.diedari.jimdur.dto.CompatibilidadProductoDTO;
import com.diedari.jimdur.dto.EspecificacionProductoDTO;
import com.diedari.jimdur.dto.ProductoDTO;
import com.diedari.jimdur.dto.ProductoProveedorDTO;
import com.diedari.jimdur.mapper.ProductoMapper;
import com.diedari.jimdur.model.Categoria;
import com.diedari.jimdur.model.CompatibilidadProducto;
import com.diedari.jimdur.model.EspecificacionProducto;
import com.diedari.jimdur.model.ImagenProducto;
import com.diedari.jimdur.model.Marca;
import com.diedari.jimdur.model.Producto;
import com.diedari.jimdur.model.ProductoProveedor;
import com.diedari.jimdur.model.Proveedor;
import com.diedari.jimdur.repository.CategoriaRepository;
import com.diedari.jimdur.repository.CompatibilidadProductoRepository;
import com.diedari.jimdur.repository.EspecificacionProductoRepository;
import com.diedari.jimdur.repository.ImagenProductoRepository;
import com.diedari.jimdur.repository.MarcaRepository;
import com.diedari.jimdur.repository.ProductoProveedorRepository;
import com.diedari.jimdur.repository.ProductoRepository;
import com.diedari.jimdur.repository.ProveedorRepository;

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
    private final EspecificacionProductoRepository especificacionProductoRepository;
    private final CompatibilidadProductoRepository compatibilidadProductoRepository;
    private final ProductoMapper productoMapper;
    private final FileStorageService fileStorageService;

    @Override
    @Transactional
    public ProductoDTO guardarProducto(ProductoDTO productoDTO) {
        productoMapper.calcularPrecioOferta(productoDTO);
        Producto producto = productoMapper.toEntity(productoDTO);

        // Generar slug si no existe
        if (producto.getSlug() == null || producto.getSlug().trim().isEmpty()) {
            producto.setSlug(generateSlug(producto.getNombre()));
        }

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

        if (productoDTO.getEspecificaciones() != null && !productoDTO.getEspecificaciones().isEmpty()) {
            guardarEspecificaciones(producto, productoDTO.getEspecificaciones());
        }

        if (productoDTO.getCompatibilidades() != null && !productoDTO.getCompatibilidades().isEmpty()) {
            guardarCompatibilidades(producto, productoDTO.getCompatibilidades());
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
        productoExistente.setTipoDescuento(productoDTO.getTipoDescuento());

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

        if (productoDTO.getEspecificaciones() != null) {
            actualizarEspecificaciones(productoExistente, productoDTO.getEspecificaciones());
        }

        if (productoDTO.getCompatibilidades() != null) {
            actualizarCompatibilidades(productoExistente, productoDTO.getCompatibilidades());
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
                fileStorageService.eliminarImagenProducto(imagen.getNombreArchivo());
            });
        }

        productoRepository.delete(producto);
    }

    private void guardarImagenes(Producto producto, List<MultipartFile> imagenes) {
        boolean primeraImagen = true;
        for (MultipartFile imagen : imagenes) {
            if (!imagen.isEmpty()) {
                String nombreArchivo = fileStorageService.guardarImagenProducto(imagen);
                ImagenProducto imagenProducto = ImagenProducto.builder()
                        .nombreArchivo(nombreArchivo)
                        .esPortada(primeraImagen)
                        .producto(producto)
                        .build();
                imagenProductoRepository.save(imagenProducto);
                primeraImagen = false;
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
            }
        }
    }

    private void guardarEspecificaciones(Producto producto, List<EspecificacionProductoDTO> especificacionesDTO) {
        for (EspecificacionProductoDTO especificacionDTO : especificacionesDTO) {
            EspecificacionProducto especificacion = EspecificacionProducto.builder()
                    .nombre(especificacionDTO.getNombre())
                    .valor(especificacionDTO.getValor())
                    .producto(producto)
                    .build();
            especificacionProductoRepository.save(especificacion);
        }
    }

    private void guardarCompatibilidades(Producto producto, List<CompatibilidadProductoDTO> compatibilidadesDTO) {
        for (CompatibilidadProductoDTO compatibilidadDTO : compatibilidadesDTO) {
            CompatibilidadProducto compatibilidad = CompatibilidadProducto.builder()
                    .modeloCompatible(compatibilidadDTO.getModeloCompatible())
                    .producto(producto)
                    .build();
            compatibilidadProductoRepository.save(compatibilidad);
        }
    }

    private void actualizarProveedores(Producto producto, List<ProductoProveedorDTO> proveedoresDTO) {
        productoProveedorRepository.deleteByIdProducto(producto.getIdProducto());
        guardarProveedores(producto, proveedoresDTO);
    }

    private void actualizarEspecificaciones(Producto producto, List<EspecificacionProductoDTO> especificacionesDTO) {
        especificacionProductoRepository.deleteByProducto(producto);
        guardarEspecificaciones(producto, especificacionesDTO);
    }

    private void actualizarCompatibilidades(Producto producto, List<CompatibilidadProductoDTO> compatibilidadesDTO) {
        compatibilidadProductoRepository.deleteByProducto(producto);
        guardarCompatibilidades(producto, compatibilidadesDTO);
    }

    private String generateSlug(String nombre) {
        if (nombre == null)
            return "";
        String slug = nombre.toLowerCase()
                .replaceAll("[^a-z0-9\\s-]", "") // Eliminar caracteres especiales
                .replaceAll("\\s+", "-") // Reemplazar espacios por guiones
                .replaceAll("-+", "-") // Eliminar guiones múltiples
                .replaceAll("^-|-$", ""); // Eliminar guiones al inicio y final

        // Asegurar que el slug sea único
        String slugBase = slug;
        int contador = 1;
        while (productoRepository.findBySlug(slug) != null) {
            slug = slugBase + "-" + contador++;
        }
        return slug;
    }

    @Override
    public boolean existeSkuProducto(String sku, Long idProducto) {
        return idProducto != null ? productoRepository.existsBySkuAndIdProductoNot(sku, idProducto)
                : productoRepository.existsBySku(sku);
    }

    @Override
    public List<Producto> obtenerProductoPorEstado(boolean estado) {
        return productoRepository.findByActivo(estado);
    }

    @Override
    public Producto obtenerProductoPorSlug(String slug) {
        return productoRepository.findBySlug(slug);
    }
}

package com.diedari.jimdur.service;

import java.util.List;
import java.util.stream.Collectors;

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
        // FASE 1: Guardar el producto principal
        Producto productoGuardado = guardarProductoPrincipal(productoDTO);

        // FASE 2: Guardar todas las relaciones
        guardarRelacionesProducto(productoGuardado, productoDTO);

        return productoMapper.toDTO(productoGuardado);
    }

    private Producto guardarProductoPrincipal(ProductoDTO productoDTO) {
        // Validar y obtener entidades relacionadas
        Categoria categoria = categoriaRepository.findById(productoDTO.getIdCategoria())
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada"));
        
        Marca marca = marcaRepository.findById(productoDTO.getIdMarca())
                .orElseThrow(() -> new RuntimeException("Marca no encontrada"));

        // Crear el producto
        Producto producto = productoMapper.toEntity(productoDTO);
        producto.setCategoria(categoria);
        producto.setMarca(marca);
        
        // Guardar y retornar el producto
        return productoRepository.save(producto);
    }

    private void guardarRelacionesProducto(Producto productoGuardado, ProductoDTO productoDTO) {
        // 1. Guardar imágenes
        if (productoDTO.getImagenes() != null && !productoDTO.getImagenes().isEmpty()) {
            guardarImagenes(productoGuardado, productoDTO.getImagenes());
        }
        
        // 2. Guardar relaciones con proveedores
        if (productoDTO.getProveedores() != null && !productoDTO.getProveedores().isEmpty()) {
            guardarProveedores(productoGuardado, productoDTO.getProveedores());
        }
        
        // 3. Guardar especificaciones
        if (productoDTO.getEspecificaciones() != null && !productoDTO.getEspecificaciones().isEmpty()) {
            guardarEspecificaciones(productoGuardado, productoDTO.getEspecificaciones());
        }
        
        // 4. Guardar compatibilidades
        if (productoDTO.getCompatibilidades() != null && !productoDTO.getCompatibilidades().isEmpty()) {
            guardarCompatibilidades(productoGuardado, productoDTO.getCompatibilidades());
        }
    }

    @Override
    @Transactional
    public ProductoDTO actualizarProducto(ProductoDTO productoDTO) {
        // Validar que el producto existe
        final Producto productoExistente = productoRepository.findById(productoDTO.getIdProducto())
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

        // Validar y obtener entidades relacionadas
        Categoria categoria = categoriaRepository.findById(productoDTO.getIdCategoria())
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada"));
        
        Marca marca = marcaRepository.findById(productoDTO.getIdMarca())
                .orElseThrow(() -> new RuntimeException("Marca no encontrada"));

        // Primero, eliminar todas las relaciones existentes
        // Es importante hacer esto antes de cualquier otra operación
        especificacionProductoRepository.deleteByProducto(productoExistente);
        compatibilidadProductoRepository.deleteByProducto(productoExistente);
        productoProveedorRepository.deleteByIdProducto(productoExistente.getIdProducto());

        // Forzar la sincronización con la base de datos
        productoRepository.flush();

        // Actualizar datos básicos del producto
        productoExistente.setSku(productoDTO.getSku());
        productoExistente.setNombre(productoDTO.getNombre());
        productoExistente.setDescripcion(productoDTO.getDescripcion());
        productoExistente.setPrecio(productoDTO.getPrecio());
        productoExistente.setDescuento(productoDTO.getDescuento());
        productoExistente.setPrecioOferta(productoDTO.getPrecioOferta());
        productoExistente.setTipoDescuento(productoDTO.getTipoDescuento());
        productoExistente.setActivo(productoDTO.getActivo());
        productoExistente.setCategoria(categoria);
        productoExistente.setMarca(marca);

        // Guardar los cambios básicos
        Producto productoActualizado = productoRepository.saveAndFlush(productoExistente);

        // Actualizar las relaciones solo si hay datos nuevos y válidos
        if (productoDTO.getProveedores() != null && !productoDTO.getProveedores().isEmpty()) {
            List<ProductoProveedorDTO> proveedoresValidos = productoDTO.getProveedores().stream()
                .filter(p -> p.getIdProveedor() != null && p.getPrecioCompra() != null)
                .collect(Collectors.toList());
            if (!proveedoresValidos.isEmpty()) {
                guardarProveedores(productoActualizado, proveedoresValidos);
            }
        }

        if (productoDTO.getEspecificaciones() != null && !productoDTO.getEspecificaciones().isEmpty()) {
            List<EspecificacionProductoDTO> especificacionesValidas = productoDTO.getEspecificaciones().stream()
                .filter(e -> e.getNombre() != null && !e.getNombre().trim().isEmpty() 
                    && e.getValor() != null && !e.getValor().trim().isEmpty())
                .collect(Collectors.toList());
            if (!especificacionesValidas.isEmpty()) {
                guardarEspecificaciones(productoActualizado, especificacionesValidas);
            }
        }

        if (productoDTO.getCompatibilidades() != null && !productoDTO.getCompatibilidades().isEmpty()) {
            List<CompatibilidadProductoDTO> compatibilidadesValidas = productoDTO.getCompatibilidades().stream()
                .filter(c -> c.getModeloCompatible() != null && !c.getModeloCompatible().trim().isEmpty())
                .collect(Collectors.toList());
            if (!compatibilidadesValidas.isEmpty()) {
                guardarCompatibilidades(productoActualizado, compatibilidadesValidas);
            }
        }

        // Actualizar imágenes si hay nuevas
        if (productoDTO.getImagenes() != null && !productoDTO.getImagenes().isEmpty()) {
            guardarImagenes(productoActualizado, productoDTO.getImagenes());
        }

        // Forzar la sincronización final
        productoRepository.flush();

        // Recargar el producto para obtener todas las relaciones actualizadas
        productoActualizado = productoRepository.findById(productoActualizado.getIdProducto())
                .orElseThrow(() -> new RuntimeException("Error al recargar el producto"));

        return productoMapper.toDTO(productoActualizado);
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
        if (proveedoresDTO == null || proveedoresDTO.isEmpty()) {
            return;
        }

        for (ProductoProveedorDTO proveedorDTO : proveedoresDTO) {
            // Solo procesar si hay un proveedor seleccionado y precio de compra
            if (proveedorDTO.getIdProveedor() != null && proveedorDTO.getPrecioCompra() != null) {
                try {
                    // Obtener el proveedor
                    Proveedor proveedor = proveedorRepository.findById(proveedorDTO.getIdProveedor())
                            .orElseThrow(() -> new RuntimeException("Proveedor no encontrado"));

                    // Crear la relación con los IDs correctos
                    ProductoProveedor productoProveedor = ProductoProveedor.builder()
                            .idProducto(producto.getIdProducto())
                            .idProveedor(proveedor.getIdProveedor())
                            .precioCompra(proveedorDTO.getPrecioCompra())
                            .build();

                    // Establecer las relaciones bidireccionales
                    productoProveedor.setProducto(producto);
                    productoProveedor.setProveedor(proveedor);

                    // Guardar la relación
                    productoProveedorRepository.save(productoProveedor);

                } catch (Exception e) {
                    throw new RuntimeException("Error al guardar la relación producto-proveedor: " + e.getMessage());
                }
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

    @Override
    @Transactional
    public void guardarProveedoresProducto(Long idProducto, List<ProductoProveedorDTO> proveedores) {
        try {
            // Obtener el producto y verificar que existe
            Producto producto = productoRepository.findById(idProducto)
                    .orElseThrow(() -> new RuntimeException("Producto no encontrado"));
            
            // Eliminar proveedores existentes y forzar flush para asegurar que la eliminación se complete
            productoProveedorRepository.deleteByIdProducto(idProducto);
            productoProveedorRepository.flush();

            // Validar la lista de proveedores
            if (proveedores == null || proveedores.isEmpty()) {
                return; // Si no hay proveedores, terminamos aquí
            }

            // Guardar los nuevos proveedores
            for (ProductoProveedorDTO proveedorDTO : proveedores) {
                if (proveedorDTO.getIdProveedor() != null && proveedorDTO.getPrecioCompra() != null) {
                    // Validar que el proveedor exista
                    Proveedor proveedor = proveedorRepository.findById(proveedorDTO.getIdProveedor())
                            .orElseThrow(() -> new RuntimeException("Proveedor no encontrado: " + proveedorDTO.getIdProveedor()));

                    // Construir la entidad con los IDs y el precio
                    ProductoProveedor productoProveedor = ProductoProveedor.builder()
                            .idProducto(idProducto)
                            .idProveedor(proveedorDTO.getIdProveedor())
                            .precioCompra(proveedorDTO.getPrecioCompra())
                            .build();

                    try {
                        // Guardar y flush para detectar errores temprano
                        productoProveedorRepository.saveAndFlush(productoProveedor);
                    } catch (Exception e) {
                        throw new RuntimeException("Error al guardar proveedor " + proveedorDTO.getIdProveedor() + 
                                                " para producto " + idProducto + ": " + e.getMessage());
                    }
                }
            }
        } catch (Exception e) {
            // Log del error para debugging
            e.printStackTrace();
            // Relanzar la excepción con un mensaje más descriptivo
            throw new RuntimeException("Error al guardar la relación producto-proveedor: " + e.getMessage(), e);
        }
    }

    @Override
    @Transactional
    public void eliminarImagen(Long idImagen) {
        ImagenProducto imagen = imagenProductoRepository.findById(idImagen)
                .orElseThrow(() -> new RuntimeException("Imagen no encontrada"));

        // Eliminar el archivo físico
        fileStorageService.eliminarImagenProducto(imagen.getNombreArchivo());

        // Eliminar el registro de la base de datos
        imagenProductoRepository.delete(imagen);
    }
}

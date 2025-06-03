package com.diedari.jimdur.mapper;

import com.diedari.jimdur.dto.CompatibilidadProductoDTO;
import com.diedari.jimdur.dto.EspecificacionProductoDTO;
import com.diedari.jimdur.dto.ProductoDTO;
import com.diedari.jimdur.dto.ProductoProveedorDTO;
import com.diedari.jimdur.model.*;
import com.diedari.jimdur.service.FileStorageService;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ProductoMapper {

    private final FileStorageService fileStorageService;

    public ProductoDTO toDTO(Producto producto) {
        if (producto == null) {
            return null;
        }

        ProductoDTO.ProductoDTOBuilder builder = ProductoDTO.builder()
                .idProducto(producto.getIdProducto())
                .sku(producto.getSku())
                .nombre(producto.getNombre())
                .descripcion(producto.getDescripcion())
                .precio(producto.getPrecio())
                .descuento(producto.getDescuento())
                .precioOferta(producto.getPrecioOferta())
                .activo(producto.getActivo())
                .slug(producto.getSlug())
                .tipoDescuento(producto.getTipoDescuento());

        // Mapear categoría
        if (producto.getCategoria() != null) {
            builder.idCategoria(producto.getCategoria().getId())
                   .nombreCategoria(producto.getCategoria().getNombreCategoria());
        }

        // Mapear marca
        if (producto.getMarca() != null) {
            builder.idMarca(producto.getMarca().getId())
                   .nombreMarca(producto.getMarca().getNombreMarca());
        }

        // Mapear proveedores
        if (producto.getProductoProveedores() != null) {
            List<ProductoProveedorDTO> proveedoresDTO = producto.getProductoProveedores().stream()
                    .map(this::toProveedorDTO)
                    .collect(Collectors.toList());
            builder.proveedores(proveedoresDTO);
        }

        // Mapear especificaciones
        if (producto.getEspecificaciones() != null) {
            List<EspecificacionProductoDTO> especificacionesDTO = producto.getEspecificaciones().stream()
                    .map(this::toEspecificacionDTO)
                    .collect(Collectors.toList());
            builder.especificaciones(especificacionesDTO);
        }

        // Mapear compatibilidades
        if (producto.getCompatibilidades() != null) {
            List<CompatibilidadProductoDTO> compatibilidadesDTO = producto.getCompatibilidades().stream()
                    .map(this::toCompatibilidadDTO)
                    .collect(Collectors.toList());
            builder.compatibilidades(compatibilidadesDTO);
        }

        return builder.build();
    }

    public Producto toEntity(ProductoDTO dto) {
        if (dto == null) {
            return null;
        }

        Producto producto = Producto.builder()
                .idProducto(dto.getIdProducto())
                .sku(dto.getSku())
                .nombre(dto.getNombre())
                .descripcion(dto.getDescripcion())
                .precio(dto.getPrecio())
                .descuento(dto.getDescuento())
                .precioOferta(dto.getPrecioOferta())
                .activo(dto.getActivo())
                .tipoDescuento(dto.getTipoDescuento())
                .build();

        // El slug se puede generar automáticamente
        if (dto.getSlug() != null) {
            producto.setSlug(dto.getSlug());
        } else if (dto.getNombre() != null) {
            producto.setSlug(generateSlug(dto.getNombre()));
        }

        // Mapear especificaciones
        if (dto.getEspecificaciones() != null) {
            List<EspecificacionProducto> especificaciones = dto.getEspecificaciones().stream()
                    .map(especificacionDTO -> {
                        EspecificacionProducto especificacion = toEspecificacionEntity(especificacionDTO);
                        if (especificacion != null) {
                            especificacion.setProducto(producto);
                        }
                        return especificacion;
                    })
                    .collect(Collectors.toList());
            producto.setEspecificaciones(especificaciones);
        }

        // Mapear compatibilidades
        if (dto.getCompatibilidades() != null) {
            List<CompatibilidadProducto> compatibilidades = dto.getCompatibilidades().stream()
                    .map(compatibilidadDTO -> {
                        CompatibilidadProducto compatibilidad = toCompatibilidadEntity(compatibilidadDTO);
                        if (compatibilidad != null) {
                            compatibilidad.setProducto(producto);
                        }
                        return compatibilidad;
                    })
                    .collect(Collectors.toList());
            producto.setCompatibilidades(compatibilidades);
        }

        return producto;
    }

    public ProductoProveedorDTO toProveedorDTO(ProductoProveedor productoProveedor) {
        if (productoProveedor == null) {
            return null;
        }

        ProductoProveedorDTO.ProductoProveedorDTOBuilder builder = ProductoProveedorDTO.builder()
                .idProducto(productoProveedor.getIdProducto())
                .idProveedor(productoProveedor.getIdProveedor())
                .precioCompra(productoProveedor.getPrecioCompra());

        // Obtener nombre del proveedor
        if (productoProveedor.getProveedor() != null) {
            builder.nombreProveedor(productoProveedor.getProveedor().getNombre());
        }

        return builder.build();
    }

    public ProductoProveedor toProveedorEntity(ProductoProveedorDTO dto) {
        if (dto == null) {
            return null;
        }

        return ProductoProveedor.builder()
                .idProducto(dto.getIdProducto())
                .idProveedor(dto.getIdProveedor())
                .precioCompra(dto.getPrecioCompra())
                .build();
    }

    public EspecificacionProductoDTO toEspecificacionDTO(EspecificacionProducto especificacion) {
        if (especificacion == null) {
            return null;
        }

        return EspecificacionProductoDTO.builder()
                .id(especificacion.getId())
                .nombre(especificacion.getNombre())
                .valor(especificacion.getValor())
                .idProducto(especificacion.getProducto() != null ? especificacion.getProducto().getIdProducto() : null)
                .build();
    }

    public EspecificacionProducto toEspecificacionEntity(EspecificacionProductoDTO dto) {
        if (dto == null) {
            return null;
        }

        return EspecificacionProducto.builder()
                .id(dto.getId())
                .nombre(dto.getNombre())
                .valor(dto.getValor())
                .build();
    }

    public CompatibilidadProductoDTO toCompatibilidadDTO(CompatibilidadProducto compatibilidad) {
        if (compatibilidad == null) {
            return null;
        }

        return CompatibilidadProductoDTO.builder()
                .id(compatibilidad.getId())
                .modeloCompatible(compatibilidad.getModeloCompatible())
                .idProducto(compatibilidad.getProducto() != null ? compatibilidad.getProducto().getIdProducto() : null)
                .build();
    }

    public CompatibilidadProducto toCompatibilidadEntity(CompatibilidadProductoDTO dto) {
        if (dto == null) {
            return null;
        }

        return CompatibilidadProducto.builder()
                .id(dto.getId())
                .modeloCompatible(dto.getModeloCompatible())
                .build();
    }

    public List<ProductoDTO> toDTOList(List<Producto> productos) {
        if (productos == null) {
            return new ArrayList<>();
        }
        return productos.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    private String generateSlug(String nombre) {
        if (nombre == null) {
            return "";
        }
        return nombre.toLowerCase()
                .replaceAll("[^a-z0-9\\s-]", "")
                .replaceAll("\\s+", "-")
                .replaceAll("-+", "-")
                .replaceAll("^-|-$", "");
    }

    // Método para calcular precio de oferta
    public void calcularPrecioOferta(ProductoDTO productoDTO) {
        if (productoDTO.getPrecio() != null && productoDTO.getDescuento() != null && productoDTO.getDescuento() > 0) {
            double precioOferta = productoDTO.getPrecio() - (productoDTO.getPrecio() * productoDTO.getDescuento() / 100);
            productoDTO.setPrecioOferta(Math.round(precioOferta * 100.0) / 100.0); // Redondear a 2 decimales
        } else {
            productoDTO.setPrecioOferta(null);
        }
    }
}

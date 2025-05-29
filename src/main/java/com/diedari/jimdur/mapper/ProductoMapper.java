package com.diedari.jimdur.mapper;

import com.diedari.jimdur.dto.ProductoDTO;
import com.diedari.jimdur.dto.ProductoProveedorDTO;
import com.diedari.jimdur.model.*;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class ProductoMapper {

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

        // Mapear imágenes existentes
        if (producto.getImagenes() != null) {
            List<String> imagenesExistentes = producto.getImagenes().stream()
                    .map(ImagenProducto::getNombreArchivo)
                    .collect(Collectors.toList());
            builder.imagenesExistentes(imagenesExistentes);
        }

        // Mapear proveedores
        if (producto.getProductoProveedores() != null) {
            List<ProductoProveedorDTO> proveedoresDTO = producto.getProductoProveedores().stream()
                    .map(this::toProveedorDTO)
                    .collect(Collectors.toList());
            builder.proveedores(proveedoresDTO);
        }

        return builder.build();
    }

    public Producto toEntity(ProductoDTO dto) {
        if (dto == null) {
            return null;
        }

        Producto.ProductoBuilder builder = Producto.builder()
                .idProducto(dto.getIdProducto())
                .sku(dto.getSku())
                .nombre(dto.getNombre())
                .descripcion(dto.getDescripcion())
                .precio(dto.getPrecio())
                .descuento(dto.getDescuento())
                .precioOferta(dto.getPrecioOferta())
                .activo(dto.getActivo())
                .tipoDescuento(dto.getTipoDescuento());

        // El slug se puede generar automáticamente
        if (dto.getSlug() != null) {
            builder.slug(dto.getSlug());
        } else if (dto.getNombre() != null) {
            builder.slug(generateSlug(dto.getNombre()));
        }

        return builder.build();
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
    public void calcularPrecioOferta(ProductoDTO dto) {
        if (dto.getPrecio() != null && dto.getDescuento() != null && dto.getDescuento() > 0) {
            double precioOferta = dto.getPrecio() - (dto.getPrecio() * dto.getDescuento() / 100);
            dto.setPrecioOferta(Math.round(precioOferta * 100.0) / 100.0);
        } else {
            dto.setPrecioOferta(null);
        }
    }
}

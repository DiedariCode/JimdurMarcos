package com.diedari.jimdur.dto;

import java.util.ArrayList;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductoDTO {

    private Long idProducto;

    @NotBlank(message = "El código SKU es obligatorio")
    @Size(max = 50, message = "El SKU no puede exceder 50 caracteres")
    @Pattern(regexp = "^[A-Z0-9]+(-[A-Z0-9]+)*$", message = "El SKU debe contener solo letras mayúsculas, números y guiones")
    private String sku;

    @NotBlank(message = "El nombre del producto es obligatorio")
    @Size(max = 100, message = "El nombre no puede exceder 100 caracteres")
    private String nombre;

    @Size(max = 1000, message = "La descripción no puede exceder 1000 caracteres")
    private String descripcion;

    @NotNull(message = "El precio es obligatorio")
    @DecimalMin(value = "0.01", message = "El precio debe ser mayor a 0")
    private Double precio;

    @DecimalMin(value = "0.0", message = "El descuento no puede ser negativo")
    private Double descuento;

    private Double precioOferta;

    @Size(max = 20, message = "El tipo de descuento no puede exceder 20 caracteres")
    private String tipoDescuento;

    @NotNull(message = "La categoría es obligatoria")
    private Long idCategoria;

    @NotNull(message = "La marca es obligatoria")
    private Long idMarca;

    @NotNull(message = "El estado es obligatorio")
    private Boolean activo;

    private String slug;

    // Para el formulario
    private String nombreCategoria;
    private String nombreMarca;

    // Para las imágenes
    private List<MultipartFile> imagenes;
    private List<ImagenProductoDTO> imagenesGuardadas;

    // Para los proveedores
    private List<ProductoProveedorDTO> proveedores = new ArrayList<>();

    // Para las especificaciones
    private List<EspecificacionProductoDTO> especificaciones = new ArrayList<>();

    // Para las compatibilidades
    private List<CompatibilidadProductoDTO> compatibilidades = new ArrayList<>();

    // Constructor con valores por defecto
    public ProductoDTO(String sku, String nombre) {
        this.sku = sku;
        this.nombre = nombre;
        this.activo = true;
        this.descuento = 0.0;
        this.proveedores = new ArrayList<>();
        this.especificaciones = new ArrayList<>();
        this.compatibilidades = new ArrayList<>();
    }
}
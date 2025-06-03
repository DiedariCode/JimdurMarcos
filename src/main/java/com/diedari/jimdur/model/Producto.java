package com.diedari.jimdur.model;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "Producto")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Producto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_producto")
    private Long idProducto;

    @Column(name = "sku", unique = true, length = 50, nullable = false)
    @NotBlank(message = "El código SKU del producto es obligatorio")
    @Size(max = 50)
    private String sku;

    @Column(name = "nombre", nullable = false, length = 100)
    @NotBlank(message = "El nombre del producto es obligatorio")
    @Size(max = 100)
    private String nombre;

    @Column(name = "descripcion", nullable = false, length = 500)
    @NotBlank(message = "La descripción del producto es obligatoria")
    @Size(max = 500)
    private String descripcion;

    @Column(name = "precio", nullable = false)
    @NotNull(message = "El precio es obligatorio")
    @DecimalMin(value = "0.0", inclusive = false)
    private Double precio;

    @Column(name = "slug", unique = true, length = 150, nullable = false)
    @NotBlank(message = "El slug es obligatorio")
    @Size(max = 150)
    private String slug;

    @Column(name = "activo", nullable = false)
    @NotNull(message = "El estado activo es obligatorio")
    private Boolean activo;

    @Column(name = "descuento")
    @DecimalMin(value = "0.0")
    @DecimalMax(value = "100.0")
    private Double descuento;

    @Column(name = "precio_oferta")
    @DecimalMin(value = "0.0")
    private Double precioOferta;

    @Column(name = "tipo_descuento", length = 20)
    @Size(max = 20)
    private String tipoDescuento;

    // Relaciones
    @ManyToOne(fetch = FetchType.LAZY) // carga perezosa para evitar problemas de rendimiento, no se carga la marca y categoría hasta que se necesite
    @JoinColumn(name = "id_marca", nullable = false)
    @NotNull(message = "La marca es obligatoria")
    private Marca marca;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_categoria", nullable = false)
    @NotNull(message = "La categoría es obligatoria")
    private Categoria categoria;

    @OneToMany(mappedBy = "producto", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ProductoProveedor> productoProveedores;

    // Relaciones OneToMany para imágenes, pedidos, etc.
    @OneToMany(mappedBy = "producto", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ImagenProducto> imagenes;

    @OneToMany(mappedBy = "producto", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ItemCarrito> itemsCarrito;

    @OneToMany(mappedBy = "producto", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<DetallePedido> detallesPedido;

    @OneToMany(mappedBy = "producto", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<DetalleBoleta> detallesBoleta;

    // Compatibilidad con otros productos
    @OneToMany(mappedBy = "producto", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<CompatibilidadProducto> compatibilidades;
    
    // Especificaciones del producto
    @OneToMany(mappedBy = "producto", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<EspecificacionProducto> especificaciones;

}

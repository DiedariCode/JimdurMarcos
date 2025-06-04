package com.diedari.jimdur.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "Producto_Proveedor")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@IdClass(ProductoProveedorId.class)
public class ProductoProveedor {
    
    @Id
    @Column(name = "id_producto")
    @NotNull(message = "El ID del producto es obligatorio")
    private Long idProducto;
    
    @Id
    @Column(name = "id_proveedor")
    @NotNull(message = "El ID del proveedor es obligatorio")
    private Long idProveedor;

    @Column(name = "precio_compra", nullable = false)
    @NotNull(message = "El precio de compra es obligatorio")
    @DecimalMin(value = "0.01", message = "El precio de compra debe ser mayor a 0")
    private Double precioCompra;
    

    // Relaciones
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_producto", insertable = false, updatable = false)
    @NotNull(message = "El producto es obligatorio")
    private Producto producto;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_proveedor", insertable = false, updatable = false)
    @NotNull(message = "El proveedor es obligatorio")
    private Proveedor proveedor;
}

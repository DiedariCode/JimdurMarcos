package com.diedari.jimdur.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "Detalle_Boleta",
       uniqueConstraints = {
           @UniqueConstraint(columnNames = {"id_boleta", "id_producto"},
                           name = "uk_boleta_producto")
       })
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DetalleBoleta {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_detalle_boleta")
    private Long idDetalleBoleta;
    
    @Column(name = "cantidad", nullable = false)
    @NotNull(message = "La cantidad es obligatoria")
    @Min(value = 1, message = "La cantidad debe ser mayor a 0")
    private Integer cantidad;
    
    @Column(name = "precio_unitario", nullable = false)
    @NotNull(message = "El precio unitario es obligatorio")
    @DecimalMin(value = "0.0", inclusive = false, message = "El precio unitario debe ser mayor a 0")
    private Double precioUnitario;
    
    @Column(name = "subtotal", nullable = false)
    @NotNull(message = "El subtotal es obligatorio")
    @DecimalMin(value = "0.0", message = "El subtotal no puede ser negativo")
    private Double subtotal;
    
    // Relaciones
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_boleta", nullable = false)
    @NotNull(message = "La boleta es obligatoria")
    private Boleta boleta;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_producto", nullable = false)
    @NotNull(message = "El producto es obligatorio")
    private Producto producto;
}

package com.diedari.jimdur.model;

import java.time.LocalDateTime;

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
@Table(name = "Inventario",
       uniqueConstraints = {
           @UniqueConstraint(columnNames = {"id_producto", "id_ubicacion"},
                           name = "uk_producto_ubicacion")
       })
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Inventario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_inventario")
    private Long idInventario;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_producto", nullable = false)
    @NotNull(message = "El producto es obligatorio")
    private Producto producto;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_ubicacion", nullable = false)
    @NotNull(message = "La ubicación es obligatoria")
    private Ubicaciones ubicacion;

    @Column(name = "cantidad", nullable = false)
    @NotNull(message = "La cantidad es obligatoria")
    @Min(value = 0, message = "La cantidad no puede ser negativa")
    private Integer cantidad;

    @Column(name = "valor_unitario", nullable = false)
    @NotNull(message = "El valor unitario es obligatorio")
    @DecimalMin(value = "0.0", inclusive = false, message = "El valor unitario debe ser mayor a 0")
    private Double valorUnitario;

    @Column(name = "fecha_actualizacion", nullable = false)
    @NotNull(message = "La fecha de actualización es obligatoria")
    private LocalDateTime fechaActualizacion;
}
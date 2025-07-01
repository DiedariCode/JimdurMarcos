package com.diedari.jimdur.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AsignarStockDTO {

    @NotNull(message = "El ID del producto es obligatorio")
    private Long idProducto;

    @NotNull(message = "El ID de la ubicación es obligatorio")
    private Long idUbicacion;

    @NotNull(message = "La cantidad es obligatoria")
    private Integer cantidad;

    @NotNull(message = "El valor unitario es obligatorio")
    @DecimalMin(value = "0.01", message = "El valor unitario debe ser mayor a 0")
    private Double valorUnitario;

    @Size(max = 500, message = "La descripción no puede exceder 500 caracteres")
    private String descripcion;

    // Tipo de operación para actualizaciones de stock (ENTRADA, SALIDA, AJUSTE)
    private String tipoOperacion;

    // Para mostrar información en el modal
    private String nombreProducto;
    private String skuProducto;
    private String nombreUbicacion;
    private String codigoUbicacion;
} 
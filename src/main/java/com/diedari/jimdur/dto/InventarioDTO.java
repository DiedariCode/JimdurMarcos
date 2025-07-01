package com.diedari.jimdur.dto;

import java.time.LocalDateTime;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InventarioDTO {

    private Long idInventario;

    @NotNull(message = "El ID del producto es obligatorio")
    private Long idProducto;

    @NotNull(message = "El ID de la ubicación es obligatorio")
    private Long idUbicacion;

    @NotNull(message = "La cantidad es obligatoria")
    @Min(value = 0, message = "La cantidad no puede ser negativa")
    private Integer cantidad;

    @NotNull(message = "El valor unitario es obligatorio")
    @DecimalMin(value = "0.0", inclusive = false, message = "El valor unitario debe ser mayor a 0")
    private Double valorUnitario;

    @NotNull(message = "La fecha de actualización es obligatoria")
    private LocalDateTime fechaActualizacion;

    // Para mostrar en formularios
    private String nombreProducto;
    private String skuProducto;
    private String nombreUbicacion;
    private String codigoUbicacion;
    private String tipoUbicacion;
    private Integer capacidadUbicacion;

    // Para cálculos
    private Double valorTotal;

    public Double getValorTotal() {
        if (cantidad != null && valorUnitario != null) {
            return cantidad * valorUnitario;
        }
        return 0.0;
    }
} 
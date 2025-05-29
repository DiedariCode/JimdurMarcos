package com.diedari.jimdur.dto;

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
public class ProductoProveedorDTO {

    @NotNull(message = "El proveedor es obligatorio")
    private Long idProveedor;

    @NotNull(message = "El precio de compra es obligatorio")
    @DecimalMin(value = "0.01", message = "El precio de compra debe ser mayor a 0")
    private Double precioCompra;

    // Para mostrar en vista
    private String nombreProveedor;
    private Long idProducto;
}
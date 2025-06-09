package com.diedari.jimdur.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CompatibilidadProductoDTO {
    
    private Long id;

    @NotBlank(message = "El modelo compatible es obligatorio")
    @Size(max = 100, message = "El modelo no puede exceder 100 caracteres")
    private String modeloCompatible;

    private Long idProducto;
} 
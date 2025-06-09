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
public class EspecificacionProductoDTO {
    
    private Long id;

    @NotBlank(message = "El nombre de la especificación es obligatorio")
    @Size(max = 100, message = "El nombre no puede exceder 100 caracteres")
    private String nombre;

    @NotBlank(message = "El valor de la especificación es obligatorio")
    @Size(max = 255, message = "El valor no puede exceder 255 caracteres")
    private String valor;

    private Long idProducto;
} 
package com.diedari.jimdur.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ImagenProductoDTO {
    private Long id;
    private String nombreArchivo;
    private Boolean esPortada;
} 
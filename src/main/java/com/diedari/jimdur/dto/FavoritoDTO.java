package com.diedari.jimdur.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FavoritoDTO {
    private Long id;
    private Long idUsuario;
    private Long idProducto;
    private LocalDateTime fechaAgregado;
    private String nombreProducto;
    private String skuProducto;
    private String imagenPortada;
} 
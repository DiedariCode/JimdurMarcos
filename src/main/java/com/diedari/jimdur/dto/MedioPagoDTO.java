package com.diedari.jimdur.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MedioPagoDTO {
    private Long id;
    private Long idUsuario;
    private String tipo;
    private String datos;
    private Boolean activo;
} 
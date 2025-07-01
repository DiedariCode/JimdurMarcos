package com.diedari.jimdur.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UbicacionDTO {
    private Long idUbicacion;
    private String codigo;
    private String nombre;
    private String descripcion;
    private Integer capacidad;
    private boolean activo;
    
    // Constructor simple para la API
    public UbicacionDTO(Long idUbicacion, String codigo, String nombre, Integer capacidad) {
        this.idUbicacion = idUbicacion;
        this.codigo = codigo;
        this.nombre = nombre;
        this.capacidad = capacidad;
        this.activo = true;
    }
}
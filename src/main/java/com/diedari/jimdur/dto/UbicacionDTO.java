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
    // Campos básicos
    private Long idUbicacion;
    private String codigo;
    private String nombre;
    private String descripcion;
    private Integer capacidad;
    private boolean activo;
    private String tipoUbicacion;
    
    // Campos para UX mejorada - ocupación y visualización
    private Integer ocupacionActual;
    private Integer espacioDisponible;
    private Double porcentajeOcupacion;
    private String estadoOcupacion; // VACIO, MEDIO, LLENO, SOBRECARGADO
    private String colorIndicador; // Color hex para visualización
    
    // Constructor simple para la API (mantener compatibilidad)
    public UbicacionDTO(Long idUbicacion, String codigo, String nombre, Integer capacidad) {
        this.idUbicacion = idUbicacion;
        this.codigo = codigo;
        this.nombre = nombre;
        this.capacidad = capacidad;
        this.activo = true;
    }
}
package com.diedari.jimdur.dto;

import com.diedari.jimdur.model.Ubicaciones.TipoUbicacion;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UbicacionDTO {
    private Long idUbicacion;
    private String nombre;
    private String descripcion;
    private String codigo;
    private Integer capacidad;
    private TipoUbicacion tipoUbicacion; // Tipo de ubicación
}
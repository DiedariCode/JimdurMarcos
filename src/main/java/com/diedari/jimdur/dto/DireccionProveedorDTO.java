package com.diedari.jimdur.dto;

import com.diedari.jimdur.model.DireccionProveedor.TipoDireccion;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DireccionProveedorDTO {

    private String etiqueta;

    private String calle;

    private String distrito;

    private String ciudad;

    private String departamentoEstado;


    private String codigoPostal;


    private String pais;


    private String referencia;


    private TipoDireccion tipoDireccion;
}

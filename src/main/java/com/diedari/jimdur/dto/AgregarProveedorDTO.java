package com.diedari.jimdur.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AgregarProveedorDTO {

    // Información general del proveedor
    private String nombreEmpresa;          // Equivalente a 'nombre'
    private String nombreComercial;
    private String rucEmpresa;
    private String tipoProveedor;
    private String estadoActivo;

    // Información de contacto del proveedor
    private String nombreContactoPrincipal;
    private String cargoContacto;
    private String telefonoContacto;
    private String emailContacto;
    private String sitioWebContacto;
    private String horarioAtencionContacto;

    // **Quitamos esta línea porque no está en la entidad**
    // private String categoriaProductosProveedor;

    // Lista de direcciones del proveedor
    private List<DireccionProveedorDTO> direcciones;
}

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
    private String nombreComercial;        // Si es diferente, puedes mantenerlo
    private String rucEmpresa;             // Equivalente a 'ruc'
    private String tipoProveedor;          // Puedes mapearlo a una categoría general
    private String estadoActivo;           // Para control de estado lógico (ej. "Activo", "Inactivo")

    // Información de contacto del proveedor
    private String nombreContactoPrincipal;
    private String cargoContacto;
    private String telefonoContacto;       // Equivalente a 'telefono'
    private String emailContacto;          // Equivalente a 'correo'
    private String sitioWebContacto;
    private String horarioAtencionContacto;

    // Categoría de productos que ofrece
    private String categoriaProductosProveedor;

    // Lista de direcciones del proveedor
    private List<DireccionProveedorDTO> direcciones;
}

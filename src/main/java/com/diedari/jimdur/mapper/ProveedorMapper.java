package com.diedari.jimdur.mapper;

import com.diedari.jimdur.dto.AgregarProveedorDTO;
import com.diedari.jimdur.dto.DireccionProveedorDTO;
import com.diedari.jimdur.model.DireccionProveedor;
import com.diedari.jimdur.model.Proveedor;

import java.util.List;
import java.util.stream.Collectors;

public class ProveedorMapper {

    public static Proveedor toEntity(AgregarProveedorDTO dto) {
        Proveedor proveedor = Proveedor.builder()
                .nombre(dto.getNombreEmpresa())
                .nombreComercial(dto.getNombreComercial())
                .ruc(dto.getRucEmpresa())
                .tipoProveedor(dto.getTipoProveedor())
                .estadoActivo(dto.getEstadoActivo())
                .nombreContactoPrincipal(dto.getNombreContactoPrincipal())
                .cargoContacto(dto.getCargoContacto())
                .telefono(dto.getTelefonoContacto())
                .correo(dto.getEmailContacto())
                .sitioWebContacto(dto.getSitioWebContacto())
                .horarioAtencionContacto(dto.getHorarioAtencionContacto())
                .categoriaProductosProveedor(dto.getCategoriaProductosProveedor())
                .build();

        if (dto.getDirecciones() != null) { // Verifica si hay direcciones en el DTO
            List<DireccionProveedor> direcciones = dto.getDirecciones().stream()
            .map(d ->
                    DireccionProveedor.builder()
                            .etiqueta(d.getEtiqueta())
                            .calle(d.getCalle())
                            .distrito(d.getDistrito())
                            .ciudad(d.getCiudad())
                            .departamentoEstado(d.getDepartamentoEstado())
                            .codigoPostal(d.getCodigoPostal())
                            .pais(d.getPais())
                            .referencia(d.getReferencia())
                            .tipoDireccion(d.getTipoDireccion()) 
                            .proveedor(proveedor)
                            .build()
            ).collect(Collectors.toList());

            proveedor.setDirecciones(direcciones);
        }

        return proveedor;
    }

    public static AgregarProveedorDTO toDTO(Proveedor proveedor) {
        AgregarProveedorDTO dto = new AgregarProveedorDTO();

        dto.setNombreEmpresa(proveedor.getNombre());
        dto.setNombreComercial(proveedor.getNombreComercial());
        dto.setRucEmpresa(proveedor.getRuc());
        dto.setTipoProveedor(proveedor.getTipoProveedor());
        dto.setEstadoActivo(proveedor.getEstadoActivo());
        dto.setNombreContactoPrincipal(proveedor.getNombreContactoPrincipal());
        dto.setCargoContacto(proveedor.getCargoContacto());
        dto.setTelefonoContacto(proveedor.getTelefono());
        dto.setEmailContacto(proveedor.getCorreo());
        dto.setSitioWebContacto(proveedor.getSitioWebContacto());
        dto.setHorarioAtencionContacto(proveedor.getHorarioAtencionContacto());
        dto.setCategoriaProductosProveedor(proveedor.getCategoriaProductosProveedor());

        if (proveedor.getDirecciones() != null) {
            List<DireccionProveedorDTO> direccionesDTO = proveedor.getDirecciones().stream().map(d ->
                    new DireccionProveedorDTO(
                            d.getEtiqueta(),
                            d.getCalle(),
                            d.getDistrito(),
                            d.getCiudad(),
                            d.getDepartamentoEstado(),
                            d.getCodigoPostal(),
                            d.getPais(),
                            d.getReferencia(),
                            d.getTipoDireccion()
                    )
            ).collect(Collectors.toList());

            dto.setDirecciones(direccionesDTO);
        }

        return dto;
    }
}

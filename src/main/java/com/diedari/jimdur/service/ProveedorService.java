package com.diedari.jimdur.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.diedari.jimdur.model.Proveedor;

public interface ProveedorService {
    
    public List<Proveedor> listarProveedores();

    public Proveedor obtenerProveedorPorId(Long idProveedor);

    public Proveedor guardarProveedor(Proveedor proveedor);

    public void eliminarProveedor(Long idProveedor);

    public Proveedor actualizarProveedor(Proveedor proveedor);

    public List<Proveedor> obtenerTodosLosProveedores();

    public List<Proveedor> obtenerProveedoresActivos(String estadoActivo);

    public Page<Proveedor> obtenerProveedoresFiltrados(
        String nombreProveedor,
        String tipoProveedor,
        String estadoActivo,
        Pageable pageable
    );

}

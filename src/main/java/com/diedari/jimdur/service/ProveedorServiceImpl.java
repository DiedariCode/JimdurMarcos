package com.diedari.jimdur.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.diedari.jimdur.model.Proveedor;
import com.diedari.jimdur.repository.ProveedorRepository;

@Service
public class ProveedorServiceImpl implements ProveedorService {

    @Autowired
    private ProveedorRepository proveedorRepository;

    @Override
    public List<Proveedor> listarProveedores() {
        return proveedorRepository.findAll();
    }

    @Override
    public Proveedor obtenerProveedorPorId(Long idProveedor) {
        return proveedorRepository.findById(idProveedor).orElse(null);
    }

    @Override
    public Proveedor guardarProveedor(Proveedor proveedor) {
        return proveedorRepository.save(proveedor);
    }

    @Override
    public void eliminarProveedor(Long idProveedor) {
        proveedorRepository.deleteById(idProveedor);
    }

    @Override
    public Proveedor actualizarProveedor(Proveedor proveedor) {
        return proveedorRepository.save(proveedor);
    }

    @Override
    public List<Proveedor> obtenerTodosLosProveedores() {
        return proveedorRepository.findAll();
    }

    @Override
    public List<Proveedor> obtenerProveedoresActivos(String estadoActivo) {
        return proveedorRepository.findByEstadoActivo(estadoActivo);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Proveedor> obtenerProveedoresFiltrados(
            String nombreProveedor,
            String tipoProveedor,
            String estadoActivo,
            Pageable pageable) {
        
        // Primero obtenemos la página con los datos básicos
        Page<Proveedor> proveedoresPage = proveedorRepository.findByFiltros(
            nombreProveedor, tipoProveedor, estadoActivo, pageable);

        // Si no hay resultados, retornamos la página vacía
        if (proveedoresPage.isEmpty()) {
            return proveedoresPage;
        }

        // Obtenemos la lista de proveedores con sus relaciones
        List<Proveedor> proveedoresConProductos = proveedorRepository.findProveedoresWithProductos(
            new ArrayList<>(proveedoresPage.getContent()));

        // Creamos una nueva página con los proveedores completos
        return new PageImpl<>(
            proveedoresConProductos, 
            pageable, 
            proveedoresPage.getTotalElements()
        );
    }
}

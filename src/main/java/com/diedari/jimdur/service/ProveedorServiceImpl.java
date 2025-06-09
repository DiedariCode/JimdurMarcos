package com.diedari.jimdur.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
}

package com.diedari.jimdur.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.diedari.jimdur.model.Producto;

@Service
public class ProductoServiceImpl implements ProductoService {

    @Override
    public Producto actualizarProducto(Producto producto) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void eliminarProducto(Long id) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public Producto guardarProductoNuevo(Producto producto) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<Producto> listarTodosLosProductos() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<Producto> listarUltimosProductos(int cantidad) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<Producto> obtenerProductoPorEstado(boolean activo) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Producto obtenerProductoPorId(Long id) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Producto obtenerProductoPorSlug(String slug) {
        // TODO Auto-generated method stub
        return null;
    }

}

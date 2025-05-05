package com.diedari.jimdur.service;

import java.util.List;
import com.diedari.jimdur.model.Producto;

public interface ProductoService {
    
    // Método para obtener todos los productos 
    public List<Producto> listarTodosLosProductos();

    // Método para obtener un producto por su ID
    public Producto obtenerProductoPorId(Long id);

    // Método para crear un nuevo producto y guardarlo en la base de datos
    public Producto guardarProductoNuevo(Producto producto);

    // Método para actualizar un producto existente
    public Producto actualizarProducto(Producto producto);

    // Método para eliminar un producto por su ID
    public void eliminarProducto(Long id);

    // Método para listar los últimos productos agregados ESTO PARA EL INDEX QUE TENGO ESO :V
    public List<Producto> listarUltimosProductos(int cantidad);
    
}

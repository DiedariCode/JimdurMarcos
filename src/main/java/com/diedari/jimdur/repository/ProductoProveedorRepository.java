package com.diedari.jimdur.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.diedari.jimdur.model.ProductoProveedor;
import com.diedari.jimdur.model.ProductoProveedorId;

@Repository
public interface ProductoProveedorRepository extends JpaRepository<ProductoProveedor, ProductoProveedorId> {
    
    // Custom query methods can be defined here if needed
    // For example:
    // List<ProductoProveedor> findByProductoId(Long productoId);
    
}

package com.diedari.jimdur.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.diedari.jimdur.model.ProductoProveedor;
import com.diedari.jimdur.model.ProductoProveedorId;

import jakarta.transaction.Transactional;

public interface ProductoProveedorRepository extends JpaRepository<ProductoProveedor, ProductoProveedorId> {

    @Transactional
    @Modifying
    @Query("DELETE FROM ProductoProveedor pp WHERE pp.idProducto = :idProducto")
    void deleteByIdProducto(Long idProducto);
}

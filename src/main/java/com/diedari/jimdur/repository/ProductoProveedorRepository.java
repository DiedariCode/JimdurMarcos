package com.diedari.jimdur.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.diedari.jimdur.model.ProductoProveedor;
import com.diedari.jimdur.model.ProductoProveedorId;

import jakarta.transaction.Transactional;
import java.util.List;

@Repository
public interface ProductoProveedorRepository extends JpaRepository<ProductoProveedor, ProductoProveedorId> {

    @Transactional
    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("DELETE FROM ProductoProveedor pp WHERE pp.idProducto = :idProducto")
    void deleteByIdProducto(Long idProducto);

    @Query("SELECT pp FROM ProductoProveedor pp WHERE pp.idProducto = :idProducto")
    List<ProductoProveedor> findByIdProducto(Long idProducto);

    @Query("SELECT CASE WHEN COUNT(pp) > 0 THEN true ELSE false END FROM ProductoProveedor pp WHERE pp.idProducto = :idProducto AND pp.idProveedor = :idProveedor")
    boolean existsByIdProductoAndIdProveedor(Long idProducto, Long idProveedor);
}

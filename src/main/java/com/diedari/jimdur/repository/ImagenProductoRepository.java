package com.diedari.jimdur.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.diedari.jimdur.model.ImagenProducto;
import com.diedari.jimdur.model.Producto;

import jakarta.transaction.Transactional;

@Repository
public interface ImagenProductoRepository extends JpaRepository<ImagenProducto, Long> {

    @Transactional
    @Modifying
    @Query("DELETE FROM ImagenProducto i WHERE i.producto = :producto")
    void deleteByProducto(Producto producto);
}

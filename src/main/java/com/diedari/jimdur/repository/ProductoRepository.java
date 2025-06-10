package com.diedari.jimdur.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.diedari.jimdur.model.Producto;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Long> {

    boolean existsBySku(String sku);

    boolean existsBySkuAndIdProductoNot(String sku, Long idProducto);

    List<Producto> findByActivo(boolean activo);

    Producto findBySlug(String slug);

    Page<Producto> findByNombreContainingIgnoreCase(String nombre, Pageable pageable);

    Page<Producto> findAll(Pageable pageable);
}

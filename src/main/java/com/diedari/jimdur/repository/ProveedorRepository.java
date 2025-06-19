package com.diedari.jimdur.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.diedari.jimdur.model.Proveedor;

@Repository
public interface ProveedorRepository extends JpaRepository<Proveedor, Long> {

    List<Proveedor> findByEstadoActivoOrderByNombreAsc(String estado);

    List<Proveedor> findByEstadoActivo(String estadoActivo);

    @Query(value = "SELECT DISTINCT p FROM Proveedor p " +
           "WHERE (:nombreProveedor IS NULL OR " +
           "      LOWER(p.nombre) LIKE LOWER(CONCAT('%', :nombreProveedor, '%')) OR " +
           "      LOWER(p.nombreComercial) LIKE LOWER(CONCAT('%', :nombreProveedor, '%'))) " +
           "AND (:tipoProveedor IS NULL OR p.tipoProveedor = :tipoProveedor) " +
           "AND (:estadoActivo IS NULL OR p.estadoActivo = :estadoActivo)")
    Page<Proveedor> findByFiltros(
        @Param("nombreProveedor") String nombreProveedor,
        @Param("tipoProveedor") String tipoProveedor,
        @Param("estadoActivo") String estadoActivo,
        Pageable pageable
    );

    @Query("SELECT p FROM Proveedor p " +
           "LEFT JOIN FETCH p.productoProveedores pp " +
           "LEFT JOIN FETCH pp.producto prod " +
           "LEFT JOIN FETCH prod.categoria " +
           "WHERE p IN :proveedores")
    List<Proveedor> findProveedoresWithProductos(@Param("proveedores") List<Proveedor> proveedores);
}

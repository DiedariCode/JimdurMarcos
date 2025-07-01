package com.diedari.jimdur.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.diedari.jimdur.model.Inventario;

public interface InventarioRepository extends JpaRepository<Inventario, Long> {

    /**
     * Buscar inventario por producto y ubicación
     */
    Optional<Inventario> findByProducto_IdProductoAndUbicacion_IdUbicacion(Long idProducto, Long idUbicacion);

    /**
     * Buscar inventarios por producto
     */
    List<Inventario> findByProducto_IdProducto(Long idProducto);

    /**
     * Buscar inventarios por ubicación
     */
    List<Inventario> findByUbicacion_IdUbicacion(Long idUbicacion);

    /**
     * Buscar inventarios con stock mayor a cero
     */
    List<Inventario> findByCantidadGreaterThan(Integer cantidad);

    /**
     * Buscar inventarios por producto con stock
     */
    List<Inventario> findByProducto_IdProductoAndCantidadGreaterThan(Long idProducto, Integer cantidad);

    /**
     * Obtener total de stock por producto
     */
    @Query("SELECT COALESCE(SUM(i.cantidad), 0) FROM Inventario i WHERE i.producto.idProducto = :idProducto")
    Integer getTotalStockByProducto(@Param("idProducto") Long idProducto);

    /**
     * Obtener total de stock por ubicación
     */
    @Query("SELECT COALESCE(SUM(i.cantidad), 0) FROM Inventario i WHERE i.ubicacion.idUbicacion = :idUbicacion")
    Integer getTotalStockByUbicacion(@Param("idUbicacion") Long idUbicacion);

    /**
     * Buscar inventarios por nombre de producto (para búsqueda)
     */
    @Query("SELECT i FROM Inventario i WHERE LOWER(i.producto.nombre) LIKE LOWER(CONCAT('%', :nombre, '%'))")
    Page<Inventario> findByProductoNombreContainingIgnoreCase(@Param("nombre") String nombre, Pageable pageable);

    /**
     * Buscar inventarios por SKU de producto
     */
    @Query("SELECT i FROM Inventario i WHERE LOWER(i.producto.sku) LIKE LOWER(CONCAT('%', :sku, '%'))")
    Page<Inventario> findByProductoSkuContainingIgnoreCase(@Param("sku") String sku, Pageable pageable);

    /**
     * Buscar inventarios por código de ubicación
     */
    @Query("SELECT i FROM Inventario i WHERE LOWER(i.ubicacion.codigo) LIKE LOWER(CONCAT('%', :codigo, '%'))")
    Page<Inventario> findByUbicacionCodigoContainingIgnoreCase(@Param("codigo") String codigo, Pageable pageable);

    /**
     * Verificar si existe stock para un producto en una ubicación específica
     */
    @Query("SELECT CASE WHEN COUNT(i) > 0 THEN true ELSE false END FROM Inventario i " +
           "WHERE i.producto.idProducto = :idProducto AND i.ubicacion.idUbicacion = :idUbicacion AND i.cantidad > 0")
    boolean existsStockByProductoAndUbicacion(@Param("idProducto") Long idProducto, @Param("idUbicacion") Long idUbicacion);
}

package com.diedari.jimdur.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.diedari.jimdur.model.MovimientoInventario;

public interface MovimientoInventarioRepository extends JpaRepository<MovimientoInventario, Long> {

    /**
     * Buscar movimientos por producto
     */
    List<MovimientoInventario> findByProducto_IdProductoOrderByFechaMovimientoDesc(Long idProducto);

    /**
     * Buscar movimientos por ubicación
     */
    List<MovimientoInventario> findByUbicacion_IdUbicacionOrderByFechaMovimientoDesc(Long idUbicacion);

    /**
     * Buscar movimientos por tipo
     */
    List<MovimientoInventario> findByTipoMovimientoOrderByFechaMovimientoDesc(MovimientoInventario.TipoMovimiento tipoMovimiento);

    /**
     * Buscar movimientos por usuario
     */
    List<MovimientoInventario> findByUsuario_IdOrderByFechaMovimientoDesc(Long idUsuario);

    /**
     * Buscar movimientos por producto y ubicación
     */
    List<MovimientoInventario> findByProducto_IdProductoAndUbicacion_IdUbicacionOrderByFechaMovimientoDesc(
            Long idProducto, Long idUbicacion);

    /**
     * Buscar movimientos en un rango de fechas
     */
    List<MovimientoInventario> findByFechaMovimientoBetweenOrderByFechaMovimientoDesc(
            LocalDateTime fechaInicio, LocalDateTime fechaFin);

    /**
     * Buscar movimientos por producto en un rango de fechas
     */
    List<MovimientoInventario> findByProducto_IdProductoAndFechaMovimientoBetweenOrderByFechaMovimientoDesc(
            Long idProducto, LocalDateTime fechaInicio, LocalDateTime fechaFin);

    /**
     * Buscar movimientos con paginación y ordenamiento
     */
    Page<MovimientoInventario> findAllByOrderByFechaMovimientoDesc(Pageable pageable);

    /**
     * Buscar movimientos por nombre de producto (para búsqueda)
     */
    @Query("SELECT m FROM MovimientoInventario m WHERE LOWER(m.producto.nombre) LIKE LOWER(CONCAT('%', :nombre, '%')) ORDER BY m.fechaMovimiento DESC")
    Page<MovimientoInventario> findByProductoNombreContainingIgnoreCase(@Param("nombre") String nombre, Pageable pageable);

    /**
     * Buscar movimientos por SKU de producto
     */
    @Query("SELECT m FROM MovimientoInventario m WHERE LOWER(m.producto.sku) LIKE LOWER(CONCAT('%', :sku, '%')) ORDER BY m.fechaMovimiento DESC")
    Page<MovimientoInventario> findByProductoSkuContainingIgnoreCase(@Param("sku") String sku, Pageable pageable);

    /**
     * Obtener últimos movimientos de un producto
     */
    List<MovimientoInventario> findTop10ByProducto_IdProductoOrderByFechaMovimientoDesc(Long idProducto);
} 
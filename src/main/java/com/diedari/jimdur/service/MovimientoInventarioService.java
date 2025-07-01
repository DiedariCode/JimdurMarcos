package com.diedari.jimdur.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.diedari.jimdur.dto.MovimientoInventarioDTO;

public interface MovimientoInventarioService {

    /**
     * Registrar un nuevo movimiento de inventario
     */
    MovimientoInventarioDTO registrarMovimiento(MovimientoInventarioDTO movimientoDTO);

    /**
     * Obtener todos los movimientos con paginación
     */
    Page<MovimientoInventarioDTO> obtenerTodosLosMovimientos(Pageable pageable);

    /**
     * Obtener movimiento por ID
     */
    MovimientoInventarioDTO obtenerMovimientoPorId(Long id);

    /**
     * Obtener movimientos por producto
     */
    List<MovimientoInventarioDTO> obtenerMovimientosPorProducto(Long idProducto);

    /**
     * Obtener movimientos por ubicación
     */
    List<MovimientoInventarioDTO> obtenerMovimientosPorUbicacion(Long idUbicacion);

    /**
     * Obtener movimientos por usuario
     */
    List<MovimientoInventarioDTO> obtenerMovimientosPorUsuario(Long idUsuario);

    /**
     * Obtener movimientos por tipo
     */
    List<MovimientoInventarioDTO> obtenerMovimientosPorTipo(String tipoMovimiento);

    /**
     * Obtener movimientos en un rango de fechas
     */
    List<MovimientoInventarioDTO> obtenerMovimientosPorFechas(LocalDateTime fechaInicio, LocalDateTime fechaFin);

    /**
     * Obtener movimientos por producto en un rango de fechas
     */
    List<MovimientoInventarioDTO> obtenerMovimientosPorProductoYFechas(Long idProducto, LocalDateTime fechaInicio, LocalDateTime fechaFin);

    /**
     * Buscar movimientos por nombre de producto
     */
    Page<MovimientoInventarioDTO> buscarMovimientosPorNombreProducto(String nombre, Pageable pageable);

    /**
     * Buscar movimientos por SKU de producto
     */
    Page<MovimientoInventarioDTO> buscarMovimientosPorSkuProducto(String sku, Pageable pageable);

    /**
     * Obtener últimos movimientos de un producto
     */
    List<MovimientoInventarioDTO> obtenerUltimosMovimientosDeProducto(Long idProducto);
} 
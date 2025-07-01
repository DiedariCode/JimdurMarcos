package com.diedari.jimdur.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.diedari.jimdur.dto.AsignarStockDTO;
import com.diedari.jimdur.dto.InventarioDTO;

public interface InventarioService {

    /**
     * Obtener todos los inventarios con paginación
     */
    Page<InventarioDTO> obtenerTodosLosInventarios(Pageable pageable);

    /**
     * Obtener inventario por ID
     */
    InventarioDTO obtenerInventarioPorId(Long id);

    /**
     * Obtener inventarios por producto
     */
    List<InventarioDTO> obtenerInventariosPorProducto(Long idProducto);

    /**
     * Obtener inventarios por ubicación
     */
    List<InventarioDTO> obtenerInventariosPorUbicacion(Long idUbicacion);

    /**
     * Obtener inventario específico por producto y ubicación
     */
    InventarioDTO obtenerInventarioPorProductoYUbicacion(Long idProducto, Long idUbicacion);

    /**
     * Obtener total de stock por producto
     */
    Integer obtenerTotalStockPorProducto(Long idProducto);

    /**
     * Asignar stock a un producto en una ubicación
     */
    InventarioDTO asignarStock(AsignarStockDTO asignarStockDTO, Long idUsuario);

    /**
     * Actualizar inventario
     */
    InventarioDTO actualizarInventario(InventarioDTO inventarioDTO, Long idUsuario);

    /**
     * Eliminar inventario
     */
    void eliminarInventario(Long id);

    /**
     * Buscar inventarios por nombre de producto
     */
    Page<InventarioDTO> buscarInventariosPorNombreProducto(String nombre, Pageable pageable);

    /**
     * Buscar inventarios por SKU de producto
     */
    Page<InventarioDTO> buscarInventariosPorSkuProducto(String sku, Pageable pageable);

    /**
     * Buscar inventarios por código de ubicación
     */
    Page<InventarioDTO> buscarInventariosPorCodigoUbicacion(String codigo, Pageable pageable);

    /**
     * Verificar si existe stock disponible para un producto en una ubicación
     */
    boolean existeStockDisponible(Long idProducto, Long idUbicacion);

    /**
     * Obtener inventarios con stock bajo (menos de cierta cantidad)
     */
    List<InventarioDTO> obtenerInventariosConStockBajo(Integer cantidadMinima);
} 
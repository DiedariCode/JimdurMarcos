package com.diedari.jimdur.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.diedari.jimdur.dto.MovimientoInventarioDTO;
import com.diedari.jimdur.model.MovimientoInventario;
import com.diedari.jimdur.model.Producto;
import com.diedari.jimdur.model.Ubicaciones;
import com.diedari.jimdur.model.Usuario;
import com.diedari.jimdur.repository.MovimientoInventarioRepository;
import com.diedari.jimdur.repository.ProductoRepository;
import com.diedari.jimdur.repository.UbicacionRepository;
import com.diedari.jimdur.repository.UsuarioRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class MovimientoInventarioServiceImpl implements MovimientoInventarioService {

    private final MovimientoInventarioRepository movimientoInventarioRepository;
    private final ProductoRepository productoRepository;
    private final UbicacionRepository ubicacionRepository;
    private final UsuarioRepository usuarioRepository;

    @Override
    public MovimientoInventarioDTO registrarMovimiento(MovimientoInventarioDTO movimientoDTO) {
        // Validar que el producto exista
        Producto producto = productoRepository.findById(movimientoDTO.getIdProducto())
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

        // Validar que la ubicación exista
        Ubicaciones ubicacion = ubicacionRepository.findById(movimientoDTO.getIdUbicacion())
                .orElseThrow(() -> new RuntimeException("Ubicación no encontrada"));

        // Validar que el usuario exista
        Usuario usuario = usuarioRepository.findById(movimientoDTO.getIdUsuario())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        // Crear el movimiento
        MovimientoInventario movimiento = MovimientoInventario.builder()
                .producto(producto)
                .ubicacion(ubicacion)
                .usuario(usuario)
                .tipoMovimiento(MovimientoInventario.TipoMovimiento.valueOf(movimientoDTO.getTipoMovimiento()))
                .cantidad(movimientoDTO.getCantidad())
                .descripcion(movimientoDTO.getDescripcion())
                .fechaMovimiento(movimientoDTO.getFechaMovimiento())
                .stockAnterior(movimientoDTO.getStockAnterior())
                .stockNuevo(movimientoDTO.getStockNuevo())
                .build();

        movimiento = movimientoInventarioRepository.save(movimiento);
        return convertirADTO(movimiento);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<MovimientoInventarioDTO> obtenerTodosLosMovimientos(Pageable pageable) {
        return movimientoInventarioRepository.findAllByOrderByFechaMovimientoDesc(pageable)
                .map(this::convertirADTO);
    }

    @Override
    @Transactional(readOnly = true)
    public MovimientoInventarioDTO obtenerMovimientoPorId(Long id) {
        MovimientoInventario movimiento = movimientoInventarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Movimiento no encontrado con ID: " + id));
        return convertirADTO(movimiento);
    }

    @Override
    @Transactional(readOnly = true)
    public List<MovimientoInventarioDTO> obtenerMovimientosPorProducto(Long idProducto) {
        return movimientoInventarioRepository.findByProducto_IdProductoOrderByFechaMovimientoDesc(idProducto)
                .stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<MovimientoInventarioDTO> obtenerMovimientosPorUbicacion(Long idUbicacion) {
        return movimientoInventarioRepository.findByUbicacion_IdUbicacionOrderByFechaMovimientoDesc(idUbicacion)
                .stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<MovimientoInventarioDTO> obtenerMovimientosPorUsuario(Long idUsuario) {
        return movimientoInventarioRepository.findByUsuario_IdOrderByFechaMovimientoDesc(idUsuario)
                .stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<MovimientoInventarioDTO> obtenerMovimientosPorTipo(String tipoMovimiento) {
        MovimientoInventario.TipoMovimiento tipo = MovimientoInventario.TipoMovimiento.valueOf(tipoMovimiento);
        return movimientoInventarioRepository.findByTipoMovimientoOrderByFechaMovimientoDesc(tipo)
                .stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<MovimientoInventarioDTO> obtenerMovimientosPorFechas(LocalDateTime fechaInicio, LocalDateTime fechaFin) {
        return movimientoInventarioRepository.findByFechaMovimientoBetweenOrderByFechaMovimientoDesc(fechaInicio, fechaFin)
                .stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<MovimientoInventarioDTO> obtenerMovimientosPorProductoYFechas(Long idProducto, LocalDateTime fechaInicio, LocalDateTime fechaFin) {
        return movimientoInventarioRepository.findByProducto_IdProductoAndFechaMovimientoBetweenOrderByFechaMovimientoDesc(
                idProducto, fechaInicio, fechaFin)
                .stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Page<MovimientoInventarioDTO> buscarMovimientosPorNombreProducto(String nombre, Pageable pageable) {
        return movimientoInventarioRepository.findByProductoNombreContainingIgnoreCase(nombre, pageable)
                .map(this::convertirADTO);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<MovimientoInventarioDTO> buscarMovimientosPorSkuProducto(String sku, Pageable pageable) {
        return movimientoInventarioRepository.findByProductoSkuContainingIgnoreCase(sku, pageable)
                .map(this::convertirADTO);
    }

    @Override
    @Transactional(readOnly = true)
    public List<MovimientoInventarioDTO> obtenerUltimosMovimientosDeProducto(Long idProducto) {
        return movimientoInventarioRepository.findTop10ByProducto_IdProductoOrderByFechaMovimientoDesc(idProducto)
                .stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    // Método auxiliar para convertir MovimientoInventario a MovimientoInventarioDTO
    private MovimientoInventarioDTO convertirADTO(MovimientoInventario movimiento) {
        return MovimientoInventarioDTO.builder()
                .idMovimiento(movimiento.getIdMovimiento())
                .idProducto(movimiento.getProducto().getIdProducto())
                .idUbicacion(movimiento.getUbicacion().getIdUbicacion())
                .tipoMovimiento(movimiento.getTipoMovimiento().name())
                .cantidad(movimiento.getCantidad())
                .descripcion(movimiento.getDescripcion())
                .fechaMovimiento(movimiento.getFechaMovimiento())
                .idUsuario(movimiento.getUsuario().getId())
                .nombreProducto(movimiento.getProducto().getNombre())
                .skuProducto(movimiento.getProducto().getSku())
                .nombreUbicacion(movimiento.getUbicacion().getNombre())
                .codigoUbicacion(movimiento.getUbicacion().getCodigo())
                .nombreUsuario(movimiento.getUsuario().getNombres() + " " + movimiento.getUsuario().getApellidos())
                .stockAnterior(movimiento.getStockAnterior())
                .stockNuevo(movimiento.getStockNuevo())
                .build();
    }
} 
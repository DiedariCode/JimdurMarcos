package com.diedari.jimdur.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.diedari.jimdur.dto.AsignarStockDTO;
import com.diedari.jimdur.dto.InventarioDTO;
import com.diedari.jimdur.dto.MovimientoInventarioDTO;
import com.diedari.jimdur.model.Inventario;
import com.diedari.jimdur.model.MovimientoInventario;
import com.diedari.jimdur.model.Producto;
import com.diedari.jimdur.model.Ubicaciones;
import com.diedari.jimdur.model.Usuario;
import com.diedari.jimdur.repository.InventarioRepository;
import com.diedari.jimdur.repository.ProductoRepository;
import com.diedari.jimdur.repository.UbicacionRepository;
import com.diedari.jimdur.repository.UsuarioRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class InventarioServiceImpl implements InventarioService {

    private final InventarioRepository inventarioRepository;
    private final ProductoRepository productoRepository;
    private final UbicacionRepository ubicacionRepository;
    private final UsuarioRepository usuarioRepository;
    private final MovimientoInventarioService movimientoInventarioService;

    @Override
    @Transactional(readOnly = true)
    public Page<InventarioDTO> obtenerTodosLosInventarios(Pageable pageable) {
        return inventarioRepository.findAll(pageable)
                .map(this::convertirADTO);
    }

    @Override
    @Transactional(readOnly = true)
    public InventarioDTO obtenerInventarioPorId(Long id) {
        Inventario inventario = inventarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Inventario no encontrado con ID: " + id));
        return convertirADTO(inventario);
    }

    @Override
    @Transactional(readOnly = true)
    public List<InventarioDTO> obtenerInventariosPorProducto(Long idProducto) {
        return inventarioRepository.findByProducto_IdProducto(idProducto)
                .stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<InventarioDTO> obtenerInventariosPorUbicacion(Long idUbicacion) {
        return inventarioRepository.findByUbicacion_IdUbicacion(idUbicacion)
                .stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public InventarioDTO obtenerInventarioPorProductoYUbicacion(Long idProducto, Long idUbicacion) {
        return inventarioRepository.findByProducto_IdProductoAndUbicacion_IdUbicacion(idProducto, idUbicacion)
                .map(this::convertirADTO)
                .orElse(null);
    }

    @Override
    @Transactional(readOnly = true)
    public Integer obtenerTotalStockPorProducto(Long idProducto) {
        return inventarioRepository.getTotalStockByProducto(idProducto);
    }

    @Override
    public InventarioDTO asignarStock(AsignarStockDTO asignarStockDTO, Long idUsuario) {
        // Validar que el producto exista
        Producto producto = productoRepository.findById(asignarStockDTO.getIdProducto())
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

        // Validar que la ubicación exista
        Ubicaciones ubicacion = ubicacionRepository.findById(asignarStockDTO.getIdUbicacion())
                .orElseThrow(() -> new RuntimeException("Ubicación no encontrada"));

        // Buscar si ya existe inventario para este producto en esta ubicación
        Inventario inventarioExistente = inventarioRepository
                .findByProducto_IdProductoAndUbicacion_IdUbicacion(
                        asignarStockDTO.getIdProducto(), 
                        asignarStockDTO.getIdUbicacion())
                .orElse(null);

        Inventario inventario;
        int stockAnterior = 0;
        int stockNuevo = 0;
        String tipoOperacion = asignarStockDTO.getTipoOperacion() != null ? 
                              asignarStockDTO.getTipoOperacion().toUpperCase() : "ENTRADA";

        if (inventarioExistente != null) {
            // Actualizar inventario existente
            stockAnterior = inventarioExistente.getCantidad();
            
            // Calcular nuevo stock según tipo de operación
            switch (tipoOperacion) {
                case "ENTRADA":
                    stockNuevo = stockAnterior + asignarStockDTO.getCantidad();
                    break;
                case "SALIDA":
                    stockNuevo = stockAnterior - asignarStockDTO.getCantidad();
                    if (stockNuevo < 0) {
                        throw new RuntimeException("No se puede reducir más stock del disponible");
                    }
                    break;
                case "AJUSTE":
                    stockNuevo = asignarStockDTO.getCantidad(); // Cantidad final deseada
                    break;
                default:
                    throw new RuntimeException("Tipo de operación no válido: " + tipoOperacion);
            }
            
            inventarioExistente.setCantidad(stockNuevo);
            inventarioExistente.setValorUnitario(asignarStockDTO.getValorUnitario());
            inventarioExistente.setFechaActualizacion(LocalDateTime.now());
            inventario = inventarioRepository.save(inventarioExistente);
        } else {
            // Crear nuevo inventario solo para ENTRADA o AJUSTE
            if (tipoOperacion.equals("SALIDA")) {
                throw new RuntimeException("No se puede hacer una salida de stock sin inventario existente");
            }
            
            stockNuevo = asignarStockDTO.getCantidad();
            inventario = Inventario.builder()
                    .producto(producto)
                    .ubicacion(ubicacion)
                    .cantidad(stockNuevo)
                    .valorUnitario(asignarStockDTO.getValorUnitario())
                    .fechaActualizacion(LocalDateTime.now())
                    .build();
            inventario = inventarioRepository.save(inventario);
        }

        // Registrar el movimiento con la cantidad real movida
        int cantidadMovimiento = Math.abs(stockNuevo - stockAnterior);
        
        MovimientoInventarioDTO movimiento = MovimientoInventarioDTO.builder()
                .idProducto(asignarStockDTO.getIdProducto())
                .idUbicacion(asignarStockDTO.getIdUbicacion())
                .tipoMovimiento(tipoOperacion)
                .cantidad(cantidadMovimiento) // Cantidad siempre positiva
                .descripcion(asignarStockDTO.getDescripcion() != null ? 
                           asignarStockDTO.getDescripcion() : "Operación de " + tipoOperacion.toLowerCase())
                .fechaMovimiento(LocalDateTime.now())
                .idUsuario(idUsuario)
                .stockAnterior(stockAnterior)
                .stockNuevo(stockNuevo)
                .build();

        movimientoInventarioService.registrarMovimiento(movimiento);

        return convertirADTO(inventario);
    }

    @Override
    public InventarioDTO actualizarInventario(InventarioDTO inventarioDTO, Long idUsuario) {
        Inventario inventario = inventarioRepository.findById(inventarioDTO.getIdInventario())
                .orElseThrow(() -> new RuntimeException("Inventario no encontrado"));

        int stockAnterior = inventario.getCantidad();
        int diferencia = inventarioDTO.getCantidad() - stockAnterior;

        inventario.setCantidad(inventarioDTO.getCantidad());
        inventario.setValorUnitario(inventarioDTO.getValorUnitario());
        inventario.setFechaActualizacion(LocalDateTime.now());

        inventario = inventarioRepository.save(inventario);

        // Registrar movimiento si hay cambio en la cantidad
        if (diferencia != 0) {
            String tipoMovimiento = diferencia > 0 ? "ENTRADA" : "SALIDA";
            MovimientoInventarioDTO movimiento = MovimientoInventarioDTO.builder()
                    .idProducto(inventario.getProducto().getIdProducto())
                    .idUbicacion(inventario.getUbicacion().getIdUbicacion())
                    .tipoMovimiento(tipoMovimiento)
                    .cantidad(Math.abs(diferencia))
                    .descripcion("Ajuste de inventario")
                    .fechaMovimiento(LocalDateTime.now())
                    .idUsuario(idUsuario)
                    .stockAnterior(stockAnterior)
                    .stockNuevo(inventario.getCantidad())
                    .build();

            movimientoInventarioService.registrarMovimiento(movimiento);
        }

        return convertirADTO(inventario);
    }

    @Override
    public void eliminarInventario(Long id) {
        if (!inventarioRepository.existsById(id)) {
            throw new RuntimeException("Inventario no encontrado");
        }
        inventarioRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<InventarioDTO> buscarInventariosPorNombreProducto(String nombre, Pageable pageable) {
        return inventarioRepository.findByProductoNombreContainingIgnoreCase(nombre, pageable)
                .map(this::convertirADTO);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<InventarioDTO> buscarInventariosPorSkuProducto(String sku, Pageable pageable) {
        return inventarioRepository.findByProductoSkuContainingIgnoreCase(sku, pageable)
                .map(this::convertirADTO);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<InventarioDTO> buscarInventariosPorCodigoUbicacion(String codigo, Pageable pageable) {
        return inventarioRepository.findByUbicacionCodigoContainingIgnoreCase(codigo, pageable)
                .map(this::convertirADTO);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existeStockDisponible(Long idProducto, Long idUbicacion) {
        return inventarioRepository.existsStockByProductoAndUbicacion(idProducto, idUbicacion);
    }

    @Override
    @Transactional(readOnly = true)
    public List<InventarioDTO> obtenerInventariosConStockBajo(Integer cantidadMinima) {
        return inventarioRepository.findByCantidadGreaterThan(0)
                .stream()
                .filter(inventario -> inventario.getCantidad() <= cantidadMinima)
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    // Método auxiliar para convertir Inventario a InventarioDTO
    private InventarioDTO convertirADTO(Inventario inventario) {
        return InventarioDTO.builder()
                .idInventario(inventario.getIdInventario())
                .idProducto(inventario.getProducto().getIdProducto())
                .idUbicacion(inventario.getUbicacion().getIdUbicacion())
                .cantidad(inventario.getCantidad())
                .valorUnitario(inventario.getValorUnitario())
                .fechaActualizacion(inventario.getFechaActualizacion())
                .nombreProducto(inventario.getProducto().getNombre())
                .skuProducto(inventario.getProducto().getSku())
                .nombreUbicacion(inventario.getUbicacion().getNombre())
                .codigoUbicacion(inventario.getUbicacion().getCodigo())
                .tipoUbicacion(inventario.getUbicacion().getTipoUbicacion().name())
                .capacidadUbicacion(inventario.getUbicacion().getCapacidad())
                .build();
    }
} 
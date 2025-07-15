package com.diedari.jimdur.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import com.diedari.jimdur.dto.AsignarStockDTO;
import com.diedari.jimdur.dto.InventarioDTO;
import com.diedari.jimdur.dto.MovimientoInventarioDTO;
import com.diedari.jimdur.model.Inventario;
import com.diedari.jimdur.model.Producto;
import com.diedari.jimdur.model.Ubicaciones;
import com.diedari.jimdur.repository.InventarioRepository;
import com.diedari.jimdur.repository.ProductoRepository;
import com.diedari.jimdur.repository.UbicacionRepository;
import com.diedari.jimdur.repository.UsuarioRepository;

class InventarioServiceTest {

        @InjectMocks
        private InventarioServiceImpl inventarioService;

        @Mock
        private InventarioRepository inventarioRepository;

        @Mock
        private ProductoRepository productoRepository;

        @Mock
        private UbicacionRepository ubicacionRepository;

        @Mock
        private UsuarioRepository usuarioRepository;

        @Mock
        private MovimientoInventarioService movimientoInventarioService;

        @BeforeEach
        void setUp() {
                MockitoAnnotations.openMocks(this);
        }

        @Test
        void testAsignarStock_CrearNuevoInventario() {
                Long idProducto = 1L;
                Long idUbicacion = 2L;
                Long idUsuario = 3L;

                Producto producto = Producto.builder().idProducto(idProducto).nombre("Producto Test").sku("SKU123")
                                .build();
                Ubicaciones ubicacion = Ubicaciones.builder().idUbicacion(idUbicacion).nombre("Almacén A")
                                .codigo("UBI01")
                                .tipoUbicacion(Ubicaciones.TipoUbicacion.CONTENEDOR).capacidad(100).build();

                when(productoRepository.findById(idProducto)).thenReturn(Optional.of(producto));
                when(ubicacionRepository.findById(idUbicacion)).thenReturn(Optional.of(ubicacion));
                when(inventarioRepository.findByProducto_IdProductoAndUbicacion_IdUbicacion(idProducto, idUbicacion))
                                .thenReturn(Optional.empty());

                Inventario nuevoInventario = Inventario.builder()
                                .idInventario(10L)
                                .producto(producto)
                                .ubicacion(ubicacion)
                                .cantidad(50)
                                .valorUnitario(15.0)
                                .fechaActualizacion(LocalDateTime.now())
                                .build();

                when(inventarioRepository.save(any(Inventario.class))).thenReturn(nuevoInventario);

                AsignarStockDTO dto = AsignarStockDTO.builder()
                                .idProducto(idProducto)
                                .idUbicacion(idUbicacion)
                                .cantidad(50)
                                .valorUnitario(15.0)
                                .tipoOperacion("ENTRADA")
                                .descripcion("Test")
                                .build();

                InventarioDTO result = inventarioService.asignarStock(dto, idUsuario);

                assertNotNull(result);
                assertEquals(50, result.getCantidad());
                assertEquals(15.0, result.getValorUnitario());
                verify(movimientoInventarioService).registrarMovimiento(any(MovimientoInventarioDTO.class));
        }

        @Test
        void testObtenerInventarioPorId_Existe() {
                Long idInventario = 1L;

                Producto producto = Producto.builder().idProducto(1L).nombre("Prod").sku("SKU").build();
                Ubicaciones ubicacion = Ubicaciones.builder().idUbicacion(2L).nombre("Ub").codigo("C1")
                                .tipoUbicacion(Ubicaciones.TipoUbicacion.CONTENEDOR).capacidad(100).build();

                Inventario inventario = Inventario.builder()
                                .idInventario(idInventario)
                                .producto(producto)
                                .ubicacion(ubicacion)
                                .cantidad(10)
                                .valorUnitario(5.0)
                                .fechaActualizacion(LocalDateTime.now())
                                .build();

                when(inventarioRepository.findById(idInventario)).thenReturn(Optional.of(inventario));

                InventarioDTO dto = inventarioService.obtenerInventarioPorId(idInventario);

                assertNotNull(dto);
                assertEquals(10, dto.getCantidad());
                assertEquals(5.0, dto.getValorUnitario());
        }

        @Test
        void testActualizarInventario_CambioCantidad() {
                Long idInventario = 1L;
                Long idUsuario = 100L;

                Producto producto = Producto.builder().idProducto(1L).nombre("Prod").sku("S").build();
                Ubicaciones ubicacion = Ubicaciones.builder().idUbicacion(1L).nombre("Ubi").codigo("Code")
                                .tipoUbicacion(Ubicaciones.TipoUbicacion.CONTENEDOR).capacidad(100).build();

                Inventario inventario = Inventario.builder()
                                .idInventario(idInventario)
                                .producto(producto)
                                .ubicacion(ubicacion)
                                .cantidad(10)
                                .valorUnitario(20.0)
                                .fechaActualizacion(LocalDateTime.now())
                                .build();

                when(inventarioRepository.findById(idInventario)).thenReturn(Optional.of(inventario));
                when(inventarioRepository.save(any(Inventario.class))).thenReturn(inventario);

                InventarioDTO dto = InventarioDTO.builder()
                                .idInventario(idInventario)
                                .idProducto(producto.getIdProducto())
                                .idUbicacion(ubicacion.getIdUbicacion())
                                .cantidad(15)
                                .valorUnitario(22.0)
                                .build();

                InventarioDTO result = inventarioService.actualizarInventario(dto, idUsuario);

                assertEquals(15, result.getCantidad());
                assertEquals(22.0, result.getValorUnitario());
                verify(movimientoInventarioService).registrarMovimiento(any(MovimientoInventarioDTO.class));
        }

        @Test
        void testObtenerTodosLosInventarios() {
                Producto mockProducto = mock(Producto.class);
                when(mockProducto.getIdProducto()).thenReturn(1L);
                when(mockProducto.getNombre()).thenReturn("Producto Mock");
                when(mockProducto.getSku()).thenReturn("SKU_MOCK");

                Ubicaciones mockUbicacion = mock(Ubicaciones.class);
                when(mockUbicacion.getIdUbicacion()).thenReturn(10L);
                when(mockUbicacion.getNombre()).thenReturn("Ubicacion Mock");
                when(mockUbicacion.getCodigo()).thenReturn("COD_MOCK");
                when(mockUbicacion.getTipoUbicacion()).thenReturn(Ubicaciones.TipoUbicacion.ESTANTE);
                when(mockUbicacion.getCapacidad()).thenReturn(200);

                Inventario inv = mock(Inventario.class);
                when(inv.getIdInventario()).thenReturn(1L);
                when(inv.getProducto()).thenReturn(mockProducto);
                when(inv.getUbicacion()).thenReturn(mockUbicacion);
                when(inv.getCantidad()).thenReturn(50);
                when(inv.getValorUnitario()).thenReturn(10.0);
                when(inv.getFechaActualizacion()).thenReturn(LocalDateTime.now());

                Page<Inventario> page = new PageImpl<>(List.of(inv));

                when(inventarioRepository.findAll(any(PageRequest.class))).thenReturn(page);

                Page<InventarioDTO> result = inventarioService.obtenerTodosLosInventarios(PageRequest.of(0, 10));

                assertNotNull(result);
                assertEquals(1, result.getContent().size());
                InventarioDTO dtoResult = result.getContent().get(0);
                assertEquals(1L, dtoResult.getIdInventario());
                assertEquals(50, dtoResult.getCantidad());
                assertEquals(1L, dtoResult.getIdProducto());
                assertEquals(10L, dtoResult.getIdUbicacion());
                assertEquals("Producto Mock", dtoResult.getNombreProducto());
                assertEquals("SKU_MOCK", dtoResult.getSkuProducto());
                assertEquals("Ubicacion Mock", dtoResult.getNombreUbicacion());
                assertEquals("COD_MOCK", dtoResult.getCodigoUbicacion());
                assertEquals("ESTANTE", dtoResult.getTipoUbicacion());
                assertEquals(200, dtoResult.getCapacidadUbicacion());
            }
}
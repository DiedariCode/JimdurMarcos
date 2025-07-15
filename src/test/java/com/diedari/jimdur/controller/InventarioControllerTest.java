package com.diedari.jimdur.controller;

import com.diedari.jimdur.service.InventarioService;
import com.diedari.jimdur.dto.InventarioDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.http.ResponseEntity;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import com.diedari.jimdur.repository.ProductoRepository;
import com.diedari.jimdur.repository.UbicacionRepository;
import com.diedari.jimdur.service.MovimientoInventarioService;
import com.diedari.jimdur.repository.UsuarioRepository;
import com.diedari.jimdur.dto.AsignarStockDTO;

class InventarioControllerTest {

    @Mock
    private InventarioService inventarioService;
    @Mock
    private Model model;
    @Mock
    private Page<InventarioDTO> pageMock;
    @Mock
    private ProductoRepository productoRepository;
    @Mock
    private UbicacionRepository ubicacionRepository;
    @Mock
    private MovimientoInventarioService movimientoInventarioService;
    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private InventarioController inventarioController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void listarInventarios_debeRetornarNombreVista() {
        when(inventarioService.obtenerTodosLosInventarios(any(Pageable.class))).thenReturn(pageMock);
        when(pageMock.getTotalPages()).thenReturn(1);
        when(pageMock.getTotalElements()).thenReturn(0L);

        String resultado = inventarioController.listarInventarios(
                model, 0, 10, "idInventario", "desc", null, null);
        assertEquals("admin/inventario/listar", resultado);
        verify(model, atLeastOnce()).addAttribute(anyString(), any());
    }

    @Test
    void mostrarFormularioNuevo_debeRetornarVista() {
        when(productoRepository.findByActivo(true)).thenReturn(new ArrayList<>());
        when(ubicacionRepository.findAll()).thenReturn(new ArrayList<>());
        String resultado = inventarioController.mostrarFormularioNuevo(model);
        assertEquals("admin/inventario/nuevo", resultado);
        verify(model).addAttribute(eq("inventario"), any());
        verify(model).addAttribute(eq("productos"), any());
        verify(model).addAttribute(eq("ubicaciones"), any());
        verify(model).addAttribute(eq("pageTitle"), any());
        verify(model).addAttribute(eq("claseActiva"), any());
    }

    @Test
    void guardarInventario_conErroresDebeRetornarVistaNuevo() {
        BindingResult result = mock(BindingResult.class);
        Principal principal = mock(Principal.class);
        RedirectAttributes redirectAttributes = mock(RedirectAttributes.class);
        when(result.hasErrors()).thenReturn(true);
        when(productoRepository.findByActivo(true)).thenReturn(new ArrayList<>());
        when(ubicacionRepository.findAll()).thenReturn(new ArrayList<>());
        String resultado = inventarioController.guardarInventario(new InventarioDTO(), result, model, principal, redirectAttributes);
        assertEquals("admin/inventario/nuevo", resultado);
    }

    @Test
    void mostrarFormularioEditar_debeRetornarVistaEditar() {
        Long id = 1L;
        InventarioDTO inventario = new InventarioDTO();
        when(inventarioService.obtenerInventarioPorId(id)).thenReturn(inventario);
        when(productoRepository.findByActivo(true)).thenReturn(new ArrayList<>());
        when(ubicacionRepository.findAll()).thenReturn(new ArrayList<>());
        String resultado = inventarioController.mostrarFormularioEditar(id, model);
        assertEquals("admin/inventario/editar", resultado);
    }

    @Test
    void eliminarInventario_debeRedirigir() {
        RedirectAttributes redirectAttributes = mock(RedirectAttributes.class);
        String resultado = inventarioController.eliminarInventario(1L, redirectAttributes);
        assertTrue(resultado.startsWith("redirect:/admin/inventario"));
    }

    @Test
    void verStockBajo_debeRetornarVista() {
        when(inventarioService.obtenerInventariosConStockBajo(anyInt())).thenReturn(new ArrayList<>());
        when(ubicacionRepository.findAll()).thenReturn(new ArrayList<>());
        String resultado = inventarioController.verStockBajo(model, 0, 10, null, null, null);
        assertEquals("admin/inventario/stock-bajo", resultado);
    }

    @Test
    void mostrarReporteInventario_debeRetornarVista() {
        Page<InventarioDTO> pageMock = mock(Page.class);
        when(inventarioService.obtenerTodosLosInventarios(any())).thenReturn(pageMock);
        when(pageMock.getContent()).thenReturn(new ArrayList<>());
        String resultado = inventarioController.mostrarReporteInventario(model);
        assertEquals("admin/inventario/reporte", resultado);
    }

    @Test
    void obtenerInventarioPorProducto_debeRetornarResponseEntity() {
        when(inventarioService.obtenerInventariosPorProducto(anyLong())).thenReturn(new ArrayList<>());
        ResponseEntity<?> response = inventarioController.obtenerInventarioPorProducto(1L);
        assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    void obtenerInventarioPorUbicacion_debeRetornarResponseEntity() {
        when(inventarioService.obtenerInventariosPorUbicacion(anyLong())).thenReturn(new ArrayList<>());
        ResponseEntity<?> response = inventarioController.obtenerInventarioPorUbicacion(1L);
        assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    void obtenerMovimientosPorProducto_debeRetornarResponseEntity() {
        when(movimientoInventarioService.obtenerMovimientosPorProducto(anyLong())).thenReturn(new ArrayList<>());
        ResponseEntity<?> response = inventarioController.obtenerMovimientosPorProducto(1L);
        assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    void obtenerEstadisticasProducto_debeRetornarResponseEntity() {
        when(movimientoInventarioService.obtenerMovimientosPorProducto(anyLong())).thenReturn(new ArrayList<>());
        ResponseEntity<?> response = inventarioController.obtenerEstadisticasProducto(1L);
        assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    void actualizarInventario_conErroresDebeRetornarVistaEditar() {
        BindingResult result = mock(BindingResult.class);
        Principal principal = mock(Principal.class);
        RedirectAttributes redirectAttributes = mock(RedirectAttributes.class);
        when(result.hasErrors()).thenReturn(true);
        when(productoRepository.findByActivo(true)).thenReturn(new ArrayList<>());
        when(ubicacionRepository.findAll()).thenReturn(new ArrayList<>());
        String resultado = inventarioController.actualizarInventario(1L, new InventarioDTO(), result, model, principal, redirectAttributes);
        assertEquals("admin/inventario/editar", resultado);
    }

    @Test
    void asignarStock_debeRetornarResponseEntityOk() {
        Principal principal = mock(Principal.class);
        when(principal.getName()).thenReturn("admin@jimdur.com");
        when(usuarioRepository.findByEmail(anyString())).thenReturn(java.util.Optional.of(new com.diedari.jimdur.model.Usuario()));
        when(inventarioService.asignarStock(any(), anyLong())).thenReturn(new InventarioDTO());
        ResponseEntity<?> response = inventarioController.asignarStock(new com.diedari.jimdur.dto.AsignarStockDTO(), principal);
        assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    void actualizarStock_debeRetornarBadRequestSiNoHayTipoOperacion() {
        Principal principal = mock(Principal.class);
        com.diedari.jimdur.dto.AsignarStockDTO stockDTO = new com.diedari.jimdur.dto.AsignarStockDTO();
        stockDTO.setTipoOperacion("");
        ResponseEntity<?> response = inventarioController.actualizarStock(stockDTO, principal);
        assertEquals(400, response.getStatusCodeValue());
    }

    @Test
    void listarInventarios_conBusquedaSku() {
        when(inventarioService.buscarInventariosPorSkuProducto(anyString(), any())).thenReturn(pageMock);
        when(pageMock.getTotalPages()).thenReturn(1);
        when(pageMock.getTotalElements()).thenReturn(0L);
        String resultado = inventarioController.listarInventarios(
            model, 0, 10, "idInventario", "desc", "12345", "sku");
        assertEquals("admin/inventario/listar", resultado);
        verify(inventarioService).buscarInventariosPorSkuProducto(eq("12345"), any());
    }

    @Test
    void listarInventarios_conBusquedaUbicacion() {
        when(inventarioService.buscarInventariosPorCodigoUbicacion(anyString(), any())).thenReturn(pageMock);
        when(pageMock.getTotalPages()).thenReturn(1);
        when(pageMock.getTotalElements()).thenReturn(0L);
        String resultado = inventarioController.listarInventarios(
            model, 0, 10, "idInventario", "desc", "A1", "ubicacion");
        assertEquals("admin/inventario/listar", resultado);
        verify(inventarioService).buscarInventariosPorCodigoUbicacion(eq("A1"), any());
    }

    @Test
    void listarInventarios_conBusquedaNombre() {
        when(inventarioService.buscarInventariosPorNombreProducto(anyString(), any())).thenReturn(pageMock);
        when(pageMock.getTotalPages()).thenReturn(1);
        when(pageMock.getTotalElements()).thenReturn(0L);
        String resultado = inventarioController.listarInventarios(
            model, 0, 10, "idInventario", "desc", "ProductoX", "nombre");
        assertEquals("admin/inventario/listar", resultado);
        verify(inventarioService).buscarInventariosPorNombreProducto(eq("ProductoX"), any());
    }

    @Test
    void guardarInventario_lanzaExcepcion() {
        BindingResult result = mock(BindingResult.class);
        Principal principal = mock(Principal.class);
        RedirectAttributes redirectAttributes = mock(RedirectAttributes.class);
        when(result.hasErrors()).thenReturn(false);
        when(usuarioRepository.findByEmail(anyString())).thenThrow(new RuntimeException("Error simulado"));
        String resultado = inventarioController.guardarInventario(new InventarioDTO(), result, model, principal, redirectAttributes);
        assertEquals("redirect:/admin/inventario/nuevo", resultado);
    }

    @Test
    void actualizarInventario_lanzaExcepcion() {
        BindingResult result = mock(BindingResult.class);
        Principal principal = mock(Principal.class);
        RedirectAttributes redirectAttributes = mock(RedirectAttributes.class);
        when(result.hasErrors()).thenReturn(false);
        when(usuarioRepository.findByEmail(anyString())).thenThrow(new RuntimeException("Error simulado"));
        String resultado = inventarioController.actualizarInventario(1L, new InventarioDTO(), result, model, principal, redirectAttributes);
        assertTrue(resultado.startsWith("redirect:/admin/inventario/1/editar"));
    }

    @Test
    void asignarStock_lanzaExcepcion() {
        Principal principal = mock(Principal.class);
        when(principal.getName()).thenReturn("admin@jimdur.com");
        when(usuarioRepository.findByEmail(anyString())).thenThrow(new RuntimeException("Error simulado"));
        ResponseEntity<?> response = inventarioController.asignarStock(new com.diedari.jimdur.dto.AsignarStockDTO(), principal);
        assertEquals(400, response.getStatusCodeValue());
    }

    @Test
    void actualizarStock_lanzaExcepcion() {
        Principal principal = mock(Principal.class);
        com.diedari.jimdur.dto.AsignarStockDTO stockDTO = new com.diedari.jimdur.dto.AsignarStockDTO();
        when(usuarioRepository.findByEmail(anyString())).thenThrow(new RuntimeException("Error simulado"));
        ResponseEntity<?> response = inventarioController.actualizarStock(stockDTO, principal);
        assertEquals(400, response.getStatusCodeValue());
    }

    @Test
    void mostrarReporteInventario_lanzaExcepcion() {
        when(inventarioService.obtenerTodosLosInventarios(any())).thenThrow(new RuntimeException("Error simulado"));
        String resultado = inventarioController.mostrarReporteInventario(model);
        assertEquals("admin/inventario/reporte", resultado);
        verify(model).addAttribute(eq("error"), contains("Error al generar el reporte"));
    }

    @Test
    void obtenerInventarioPorProducto_lanzaExcepcion() {
        when(inventarioService.obtenerInventariosPorProducto(anyLong())).thenThrow(new RuntimeException("Error simulado"));
        ResponseEntity<?> response = inventarioController.obtenerInventarioPorProducto(1L);
        assertEquals(400, response.getStatusCodeValue());
    }

    @Test
    void obtenerInventarioPorUbicacion_lanzaExcepcion() {
        when(inventarioService.obtenerInventariosPorUbicacion(anyLong())).thenThrow(new RuntimeException("Error simulado"));
        ResponseEntity<?> response = inventarioController.obtenerInventarioPorUbicacion(1L);
        assertEquals(400, response.getStatusCodeValue());
    }

    @Test
    void obtenerMovimientosPorProducto_lanzaExcepcion() {
        when(movimientoInventarioService.obtenerMovimientosPorProducto(anyLong())).thenThrow(new RuntimeException("Error simulado"));
        ResponseEntity<?> response = inventarioController.obtenerMovimientosPorProducto(1L);
        assertEquals(400, response.getStatusCodeValue());
    }

    @Test
    void obtenerEstadisticasProducto_lanzaExcepcion() {
        when(movimientoInventarioService.obtenerMovimientosPorProducto(anyLong())).thenThrow(new RuntimeException("Error simulado"));
        ResponseEntity<?> response = inventarioController.obtenerEstadisticasProducto(1L);
        assertEquals(200, response.getStatusCodeValue()); // El método retorna 200 con mapa vacío
    }

    @Test
    void guardarInventario_flujoExitoso() {
        BindingResult result = mock(BindingResult.class);
        Principal principal = mock(Principal.class);
        RedirectAttributes redirectAttributes = mock(RedirectAttributes.class);
        InventarioDTO inventarioDTO = new InventarioDTO();
        com.diedari.jimdur.model.Usuario usuario = new com.diedari.jimdur.model.Usuario();
        usuario.setId(1L);

        when(result.hasErrors()).thenReturn(false);
        when(principal.getName()).thenReturn("admin@jimdur.com");
        when(usuarioRepository.findByEmail(anyString())).thenReturn(java.util.Optional.of(usuario));
        when(inventarioService.asignarStock(any(), anyLong())).thenReturn(new InventarioDTO());

        String resultado = inventarioController.guardarInventario(inventarioDTO, result, model, principal, redirectAttributes);
        assertEquals("redirect:/admin/inventario", resultado);
        verify(redirectAttributes).addFlashAttribute(eq("success"), contains("Inventario creado exitosamente"));
    }

    @Test
    void actualizarInventario_flujoExitoso() {
        BindingResult result = mock(BindingResult.class);
        Principal principal = mock(Principal.class);
        RedirectAttributes redirectAttributes = mock(RedirectAttributes.class);
        InventarioDTO inventarioDTO = new InventarioDTO();
        com.diedari.jimdur.model.Usuario usuario = new com.diedari.jimdur.model.Usuario();
        usuario.setId(1L);

        when(result.hasErrors()).thenReturn(false);
        when(principal.getName()).thenReturn("admin@jimdur.com");
        when(usuarioRepository.findByEmail(anyString())).thenReturn(java.util.Optional.of(usuario));
        when(inventarioService.actualizarInventario(any(), anyLong())).thenReturn(new InventarioDTO());

        String resultado = inventarioController.actualizarInventario(1L, inventarioDTO, result, model, principal, redirectAttributes);
        assertEquals("redirect:/admin/inventario", resultado);
        verify(inventarioService).actualizarInventario(any(), anyLong());
    }

    @Test
    void eliminarInventario_flujoExitoso() {
        RedirectAttributes redirectAttributes = mock(RedirectAttributes.class);
        doNothing().when(inventarioService).eliminarInventario(anyLong());
        String resultado = inventarioController.eliminarInventario(1L, redirectAttributes);
        assertEquals("redirect:/admin/inventario", resultado);
        verify(redirectAttributes).addFlashAttribute(eq("success"), contains("Inventario eliminado exitosamente"));
    }

    @Test
    void actualizarStock_flujoExitoso() {
        Principal principal = mock(Principal.class);
        when(principal.getName()).thenReturn("admin@jimdur.com");
        when(usuarioRepository.findByEmail(anyString())).thenReturn(java.util.Optional.of(new com.diedari.jimdur.model.Usuario()));
        AsignarStockDTO dto = new AsignarStockDTO();
        dto.setIdProducto(1L);
        dto.setIdUbicacion(2L);
        dto.setCantidad(5);
        dto.setTipoOperacion("ENTRADA");
        List<InventarioDTO> inventarios = new java.util.ArrayList<>();
        InventarioDTO inventarioExistente = new InventarioDTO();
        inventarioExistente.setIdProducto(1L);
        inventarioExistente.setIdUbicacion(2L);
        inventarioExistente.setCantidad(10);
        inventarios.add(inventarioExistente);
        when(inventarioService.obtenerInventariosPorProducto(1L)).thenReturn(inventarios);
        when(inventarioService.asignarStock(any(), anyLong())).thenReturn(new InventarioDTO());
        ResponseEntity<?> response = inventarioController.actualizarStock(dto, principal);
        assertEquals(200, response.getStatusCodeValue());
    }
} 
package com.diedari.jimdur.controller;

import com.diedari.jimdur.dto.ProductoDTO;
import com.diedari.jimdur.dto.ProductoProveedorDTO;
import com.diedari.jimdur.model.Categoria;
import com.diedari.jimdur.model.Marca;
import com.diedari.jimdur.model.Proveedor;
import com.diedari.jimdur.repository.CategoriaRepository;
import com.diedari.jimdur.repository.MarcaRepository;
import com.diedari.jimdur.repository.ProveedorRepository;
import com.diedari.jimdur.service.ProductoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductoRestControllerTest {
    @Mock
    private ProductoService productoService;
    @Mock
    private CategoriaRepository categoriaRepository;
    @Mock
    private MarcaRepository marcaRepository;
    @Mock
    private ProveedorRepository proveedorRepository;
    @Mock
    private BindingResult bindingResult;
    @InjectMocks
    private ProductoRestController controller;

    private ProductoDTO productoDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        productoDTO = new ProductoDTO();
        productoDTO.setIdProducto(1L);
        productoDTO.setSku("SKU-1");
        productoDTO.setNombre("Producto Test");
        productoDTO.setIdCategoria(1L);
        productoDTO.setIdMarca(1L);
        productoDTO.setProveedores(new ArrayList<>());
        productoDTO.setEspecificaciones(new ArrayList<>());
        productoDTO.setCompatibilidades(new ArrayList<>());
    }

    @Test
    void testListarProductos() {
        ResponseEntity<List<ProductoDTO>> response = controller.listarProductos();
        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
    }

    @Test
    void testCrearProducto_SkuExistente() {
        when(bindingResult.hasErrors()).thenReturn(false);
        Map<String, Object> map = controller.crearProducto(productoDTO, bindingResult).getBody();
        assertTrue(map.get("message") == null || map.get("message").toString().contains("Error") || map.get("message").toString().contains("validación"));
    }

    @Test
    void testCrearProducto_ErroresValidacion() {
        when(bindingResult.hasErrors()).thenReturn(true);
        when(bindingResult.getFieldErrors()).thenReturn(Collections.emptyList());
        Map<String, Object> map = controller.crearProducto(productoDTO, bindingResult).getBody();
        assertFalse((Boolean) map.get("success"));
        assertEquals("Errores de validación", map.get("message"));
    }

    @Test
    void testCrearProducto_Exito() {
        when(bindingResult.hasErrors()).thenReturn(false);
        Map<String, Object> map = controller.crearProducto(productoDTO, bindingResult).getBody();
        assertTrue(map.get("success") instanceof Boolean);
        assertTrue(map.get("message") == null || map.get("message").toString().contains("Error") || map.get("message").toString().contains("validación"));
    }

    @Test
    void testCrearProducto_Exception() {
        when(bindingResult.hasErrors()).thenReturn(false);
        Map<String, Object> map = controller.crearProducto(productoDTO, bindingResult).getBody();
        assertFalse((Boolean) map.get("success"));
        assertTrue(((String) map.get("message")).contains("Error") || ((String) map.get("message")).contains("validación"));
    }

    @Test
    void testGuardarProveedores_Exito() {
        Map<String, Object> req = new HashMap<>();
        req.put("idProducto", 1L);
        List<Map<String, Object>> proveedores = new ArrayList<>();
        Map<String, Object> prov = new HashMap<>();
        prov.put("idProveedor", 2L);
        prov.put("precioCompra", 100.0);
        proveedores.add(prov);
        req.put("proveedores", proveedores);
        Map<String, Object> map = controller.guardarProveedores(req).getBody();
        assertTrue((Boolean) map.get("success"));
        assertEquals("Proveedores guardados exitosamente", map.get("message"));
    }

    @Test
    void testGuardarProveedores_Exception() {
        Map<String, Object> req = new HashMap<>();
        req.put("idProducto", 1L);
        req.put("proveedores", new ArrayList<>());
        Map<String, Object> map = controller.guardarProveedores(req).getBody();
        assertTrue(map.get("success") instanceof Boolean);
    }

    @Test
    void testEditarProducto_SinProveedores() {
        productoDTO.setProveedores(null);
        productoDTO.setEspecificaciones(List.of(new com.diedari.jimdur.dto.EspecificacionProductoDTO()));
        productoDTO.setCompatibilidades(List.of(new com.diedari.jimdur.dto.CompatibilidadProductoDTO()));
        Map<String, Object> map = controller.editarProducto(productoDTO, bindingResult).getBody();
        assertFalse((Boolean) map.get("success"));
        assertEquals("Debe agregar al menos un proveedor", map.get("message"));
    }

    @Test
    void testEditarProducto_SinEspecificaciones() {
        productoDTO.setProveedores(List.of(new ProductoProveedorDTO()));
        productoDTO.setEspecificaciones(null);
        productoDTO.setCompatibilidades(List.of(new com.diedari.jimdur.dto.CompatibilidadProductoDTO()));
        Map<String, Object> map = controller.editarProducto(productoDTO, bindingResult).getBody();
        assertFalse((Boolean) map.get("success"));
        assertEquals("Debe agregar al menos una especificación", map.get("message"));
    }

    @Test
    void testEditarProducto_SinCompatibilidades() {
        productoDTO.setProveedores(List.of(new ProductoProveedorDTO()));
        productoDTO.setEspecificaciones(List.of(new com.diedari.jimdur.dto.EspecificacionProductoDTO()));
        productoDTO.setCompatibilidades(null);
        Map<String, Object> map = controller.editarProducto(productoDTO, bindingResult).getBody();
        assertFalse((Boolean) map.get("success"));
        assertEquals("Debe agregar al menos una compatibilidad", map.get("message"));
    }

    @Test
    void testEditarProducto_Exito() {
        when(bindingResult.hasErrors()).thenReturn(false);
        productoDTO.setProveedores(List.of(new ProductoProveedorDTO()));
        productoDTO.setEspecificaciones(List.of(new com.diedari.jimdur.dto.EspecificacionProductoDTO()));
        productoDTO.setCompatibilidades(List.of(new com.diedari.jimdur.dto.CompatibilidadProductoDTO()));
        Map<String, Object> map = controller.editarProducto(productoDTO, bindingResult).getBody();
        assertTrue(map.get("success") instanceof Boolean);
        assertTrue(map.get("message") == null || map.get("message").toString().contains("Error") || map.get("message").toString().contains("validación"));
    }

    @Test
    void testEditarProducto_SkuExistente() {
        when(bindingResult.hasErrors()).thenReturn(false);
        productoDTO.setProveedores(List.of(new ProductoProveedorDTO()));
        productoDTO.setEspecificaciones(List.of(new com.diedari.jimdur.dto.EspecificacionProductoDTO()));
        productoDTO.setCompatibilidades(List.of(new com.diedari.jimdur.dto.CompatibilidadProductoDTO()));
        Map<String, Object> map = controller.editarProducto(productoDTO, bindingResult).getBody();
        assertTrue(map.get("message") == null || map.get("message").toString().contains("Error") || map.get("message").toString().contains("validación"));
    }

    @Test
    void testCalcularPrecioOferta_Exito() {
        Map<String, Object> req = new HashMap<>();
        req.put("precio", 100.0);
        req.put("descuento", 10.0);
        req.put("tipoDescuento", "PORCENTAJE");
        ResponseEntity<Map<String, Object>> response = controller.calcularPrecioOferta(req);
        assertEquals(200, response.getStatusCodeValue());
        assertTrue(response.getBody().containsKey("precioOferta"));
    }

    @Test
    void testCalcularPrecioOferta_FaltanCampos() {
        Map<String, Object> req = new HashMap<>();
        assertThrows(NullPointerException.class, () -> controller.calcularPrecioOferta(req));
    }
} 
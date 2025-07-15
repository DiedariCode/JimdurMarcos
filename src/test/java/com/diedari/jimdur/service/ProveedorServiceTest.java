package com.diedari.jimdur.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.data.domain.*;

import com.diedari.jimdur.model.Proveedor;
import com.diedari.jimdur.repository.ProveedorRepository;

class ProveedorServiceTest {

    @Mock
    private ProveedorRepository proveedorRepository;

    @InjectMocks
    private ProveedorServiceImpl proveedorService;

    private Proveedor proveedorEjemplo;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        proveedorEjemplo = Proveedor.builder()
                .idProveedor(1L)
                .nombre("Proveedor S.A.")
                .nombreComercial("ProveedorCom")
                .ruc("12345678901")
                .tipoProveedor("Local")
                .estadoActivo("Activo")
                .correo("proveedor@correo.com")
                .telefono("999999999")
                .build();
    }

    @Test
    void testListarProveedores() {
        when(proveedorRepository.findAll()).thenReturn(List.of(proveedorEjemplo));

        List<Proveedor> resultado = proveedorService.listarProveedores();

        assertEquals(1, resultado.size());
        assertEquals("Proveedor S.A.", resultado.get(0).getNombre());
    }

    @Test
    void testObtenerProveedorPorId_CuandoExiste() {
        when(proveedorRepository.findById(1L)).thenReturn(Optional.of(proveedorEjemplo));

        Proveedor resultado = proveedorService.obtenerProveedorPorId(1L);

        assertNotNull(resultado);
        assertEquals("Proveedor S.A.", resultado.getNombre());
    }

    @Test
    void testObtenerProveedorPorId_CuandoNoExiste() {
        when(proveedorRepository.findById(99L)).thenReturn(Optional.empty());

        Proveedor resultado = proveedorService.obtenerProveedorPorId(99L);

        assertNull(resultado);
    }

    @Test
    void testGuardarProveedor() {
        when(proveedorRepository.save(proveedorEjemplo)).thenReturn(proveedorEjemplo);

        Proveedor resultado = proveedorService.guardarProveedor(proveedorEjemplo);

        assertEquals("12345678901", resultado.getRuc());
        assertEquals("Activo", resultado.getEstadoActivo());
    }

    @Test
    void testActualizarProveedor() {
        proveedorEjemplo.setNombre("Proveedor Actualizado");

        when(proveedorRepository.save(proveedorEjemplo)).thenReturn(proveedorEjemplo);

        Proveedor resultado = proveedorService.actualizarProveedor(proveedorEjemplo);

        assertEquals("Proveedor Actualizado", resultado.getNombre());
    }

    @Test
    void testEliminarProveedor() {
        doNothing().when(proveedorRepository).deleteById(1L);

        proveedorService.eliminarProveedor(1L);

        verify(proveedorRepository, times(1)).deleteById(1L);
    }

    @Test
    void testObtenerTodosLosProveedores() {
        when(proveedorRepository.findAll()).thenReturn(List.of(proveedorEjemplo));

        List<Proveedor> resultado = proveedorService.obtenerTodosLosProveedores();

        assertEquals(1, resultado.size());
    }

    @Test
    void testObtenerProveedoresActivos() {
        when(proveedorRepository.findByEstadoActivo("Activo")).thenReturn(List.of(proveedorEjemplo));

        List<Proveedor> resultado = proveedorService.obtenerProveedoresActivos("Activo");

        assertEquals(1, resultado.size());
        assertEquals("Activo", resultado.get(0).getEstadoActivo());
    }

    @Test
    void testObtenerProveedoresFiltrados() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Proveedor> page = new PageImpl<>(List.of(proveedorEjemplo));

        when(proveedorRepository.findByFiltros("Proveedor", "Local", "Activo", pageable)).thenReturn(page);
        when(proveedorRepository.findProveedoresWithProductos(anyList())).thenReturn(List.of(proveedorEjemplo));

        Page<Proveedor> resultado = proveedorService.obtenerProveedoresFiltrados("Proveedor", "Local", "Activo",
                pageable);

        assertEquals(1, resultado.getContent().size());
        assertEquals("Proveedor S.A.", resultado.getContent().get(0).getNombre());
    }
}

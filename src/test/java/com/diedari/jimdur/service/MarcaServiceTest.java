package com.diedari.jimdur.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.data.domain.*;

import com.diedari.jimdur.model.Marca;
import com.diedari.jimdur.repository.MarcaRepository;

class MarcaServiceTest {

    @Mock
    private MarcaRepository marcaRepository;

    @InjectMocks
    private MarcaServiceImpl marcaService;

    private Marca marcaEjemplo;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        marcaEjemplo = Marca.builder()
                .id(1L)
                .nombreMarca("Nike")
                .descripcionMarca("Marca deportiva")
                .paisOrigenMarca("EE.UU.")
                .sitioWebMarca("https://www.nike.com")
                .logourlMarca("https://nike.com/logo.png")
                .slugMarca("nike")
                .estadoMarca(true)
                .build();
    }

    @Test
    void testListarTodasLasMarcas() {
        when(marcaRepository.findAll()).thenReturn(List.of(marcaEjemplo));

        List<Marca> resultado = marcaService.listarTodasLasMarcas();

        assertEquals(1, resultado.size());
        assertEquals("Nike", resultado.get(0).getNombreMarca());
    }

    @Test
    void testObtenerMarcaPorId() {
        when(marcaRepository.findById(1L)).thenReturn(Optional.of(marcaEjemplo));

        Marca resultado = marcaService.obtenerMarcaPorId(1L);

        assertNotNull(resultado);
        assertEquals("Nike", resultado.getNombreMarca());
    }

    @Test
    void testGuardarMarcaNuevo() {
        when(marcaRepository.save(marcaEjemplo)).thenReturn(marcaEjemplo);

        Marca resultado = marcaService.guardarMarcaNuevo(marcaEjemplo);

        assertEquals("nike", resultado.getSlugMarca());
        assertTrue(resultado.getEstadoMarca());
    }

    @Test
    void testActualizarMarca() {
        marcaEjemplo.setDescripcionMarca("Marca global actualizada");

        when(marcaRepository.save(marcaEjemplo)).thenReturn(marcaEjemplo);

        Marca resultado = marcaService.actualizarMarca(marcaEjemplo);

        assertEquals("Marca global actualizada", resultado.getDescripcionMarca());
    }

    @Test
    void testEliminarMarca() {
        doNothing().when(marcaRepository).deleteById(1L);

        marcaService.eliminarMarca(1L);

        verify(marcaRepository, times(1)).deleteById(1L);
    }

    @Test
    void testObtenerMarcaPorNombres_CuandoNombreNoEsNulo() {
        when(marcaRepository.findByNombreMarca("Nike")).thenReturn(List.of(marcaEjemplo));

        List<Marca> resultado = marcaService.obtenerMarcaPorNombres("Nike");

        assertEquals(1, resultado.size());
    }

    @Test
    void testObtenerMarcaPorNombres_CuandoNombreEsNulo() {
        when(marcaRepository.findAll()).thenReturn(List.of(marcaEjemplo));

        List<Marca> resultado = marcaService.obtenerMarcaPorNombres(null);

        assertEquals(1, resultado.size());
    }

    @Test
    void testObtenerMarcasPorEstado() {
        when(marcaRepository.findByEstadoMarca(true)).thenReturn(List.of(marcaEjemplo));

        List<Marca> resultado = marcaService.obtenerMarcasPorEstado(true);

        assertEquals(1, resultado.size());
        assertTrue(resultado.get(0).getEstadoMarca());
    }

    @Test
    void testObtenerMarcasFiltradas_SinFiltros() {
        Pageable pageable = PageRequest.of(0, 5);
        Page<Marca> page = new PageImpl<>(List.of(marcaEjemplo));

        when(marcaRepository.findAll(pageable)).thenReturn(page);

        Page<Marca> resultado = marcaService.obtenerMarcasFiltradas(null, null, pageable);

        assertEquals(1, resultado.getTotalElements());
    }

    @Test
    void testObtenerMarcasFiltradas_PorNombre() {
        Pageable pageable = PageRequest.of(0, 5);
        Page<Marca> page = new PageImpl<>(List.of(marcaEjemplo));

        when(marcaRepository.findByNombreMarcaContainingIgnoreCase("nike", pageable)).thenReturn(page);

        Page<Marca> resultado = marcaService.obtenerMarcasFiltradas("nike", "", pageable);

        assertEquals(1, resultado.getContent().size());
    }

    @Test
    void testObtenerMarcasFiltradas_PorEstado() {
        Pageable pageable = PageRequest.of(0, 5);
        Page<Marca> page = new PageImpl<>(List.of(marcaEjemplo));

        when(marcaRepository.findByEstadoMarca(true, pageable)).thenReturn(page);

        Page<Marca> resultado = marcaService.obtenerMarcasFiltradas("", "activa", pageable);

        assertEquals(1, resultado.getTotalElements());
    }

    @Test
    void testObtenerMarcasFiltradas_PorNombreYEstado() {
        Pageable pageable = PageRequest.of(0, 5);
        Page<Marca> page = new PageImpl<>(List.of(marcaEjemplo));

        when(marcaRepository.findByNombreMarcaContainingIgnoreCaseAndEstadoMarca("nike", true, pageable))
                .thenReturn(page);

        Page<Marca> resultado = marcaService.obtenerMarcasFiltradas("nike", "activa", pageable);

        assertEquals(1, resultado.getContent().size());
    }
}
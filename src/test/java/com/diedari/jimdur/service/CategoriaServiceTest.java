package com.diedari.jimdur.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.data.domain.*;

import com.diedari.jimdur.model.Categoria;
import com.diedari.jimdur.repository.CategoriaRepository;
import com.diedari.jimdur.service.CategoriaServiceImpl;

class CategoriaServiceTest {

    @Mock
    private CategoriaRepository categoriaRepository;

    @InjectMocks
    private CategoriaServiceImpl categoriaService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    private Categoria crearMockCategoria() {
        return Categoria.builder()
                .id(1L)
                .nombreCategoria("Ropa")
                .descripcionCategoria("Ropa de todo tipo")
                .slugCategoria("ropa")
                .iconoCategoria("icono.png")
                .estadoActiva(true)
                .build();
    }

    @Test
    void testObtenerTodasLasCategorias() {
        when(categoriaRepository.findAll()).thenReturn(List.of(crearMockCategoria()));

        List<Categoria> resultado = categoriaService.obtenerTodasLasCategorias();

        assertEquals(1, resultado.size());
        assertEquals("Ropa", resultado.get(0).getNombreCategoria());
    }

    @Test
    void testObtenerCategoriaPorId_CuandoExiste() {
        when(categoriaRepository.findById(1L)).thenReturn(Optional.of(crearMockCategoria()));

        Categoria resultado = categoriaService.obtenerCategoriaPorId(1L);

        assertNotNull(resultado);
        assertEquals("Ropa", resultado.getNombreCategoria());
    }

    @Test
    void testObtenerCategoriaPorId_CuandoNoExiste() {
        when(categoriaRepository.findById(2L)).thenReturn(Optional.empty());

        Categoria resultado = categoriaService.obtenerCategoriaPorId(2L);

        assertNull(resultado);
    }

    @Test
    void testCrearCategoria() {
        Categoria mock = crearMockCategoria();
        when(categoriaRepository.save(mock)).thenReturn(mock);

        Categoria resultado = categoriaService.crearCategoria(mock);

        assertEquals("Ropa", resultado.getNombreCategoria());
    }

    @Test
    void testActualizarCategoria() {
        Categoria actualizada = crearMockCategoria();
        actualizada.setDescripcionCategoria("Nueva descripción");

        when(categoriaRepository.save(actualizada)).thenReturn(actualizada);

        Categoria resultado = categoriaService.actualizarCategoria(actualizada);

        assertEquals("Nueva descripción", resultado.getDescripcionCategoria());
    }

    @Test
    void testEliminarCategoriaPorId() {
        doNothing().when(categoriaRepository).deleteById(1L);

        categoriaService.eliminarCategoriaPorId(1L);

        verify(categoriaRepository, times(1)).deleteById(1L);
    }

    @Test
    void testObtenerCategoriaPorEstado() {
        when(categoriaRepository.findByEstadoActiva(true)).thenReturn(List.of(crearMockCategoria()));

        List<Categoria> resultado = categoriaService.obtenerCategoriaPorEstado(true);

        assertEquals(1, resultado.size());
    }

    @Test
    void testObtenerCategoriaPorNombre_ConNombre() {
        when(categoriaRepository.findByNombreCategoria("Ropa")).thenReturn(List.of(crearMockCategoria()));

        List<Categoria> resultado = categoriaService.obtenerCategoriaPorNombre("Ropa");

        assertEquals(1, resultado.size());
    }

    @Test
    void testObtenerCategoriaPorNombre_SinNombre() {
        when(categoriaRepository.findAll()).thenReturn(List.of(crearMockCategoria()));

        List<Categoria> resultado = categoriaService.obtenerCategoriaPorNombre("");

        assertEquals(1, resultado.size());
    }

    @Test
    void testObtenerCategoriasFiltradas_SinFiltros() {
        Pageable pageable = PageRequest.of(0, 5);
        Page<Categoria> page = new PageImpl<>(List.of(crearMockCategoria()));

        when(categoriaRepository.findAll(pageable)).thenReturn(page);

        Page<Categoria> resultado = categoriaService.obtenerCategoriasFiltradas("", "", pageable);

        assertEquals(1, resultado.getContent().size());
    }

    @Test
    void testObtenerCategoriasFiltradas_ConNombreYEstado() {
        Pageable pageable = PageRequest.of(0, 5);
        Page<Categoria> page = new PageImpl<>(List.of(crearMockCategoria()));

        when(categoriaRepository.findByNombreCategoriaContainingIgnoreCaseAndEstadoActiva("Ropa", true, pageable))
                .thenReturn(page);

        Page<Categoria> resultado = categoriaService.obtenerCategoriasFiltradas("Ropa", "activa", pageable);

        assertEquals(1, resultado.getContent().size());
    }
}
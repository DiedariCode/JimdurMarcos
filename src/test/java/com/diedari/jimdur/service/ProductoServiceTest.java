package com.diedari.jimdur.service;

import com.diedari.jimdur.model.Producto;
import com.diedari.jimdur.model.Categoria;
import com.diedari.jimdur.model.Marca;
import com.diedari.jimdur.repository.ProductoRepository;
import com.diedari.jimdur.repository.CategoriaRepository;
import com.diedari.jimdur.repository.MarcaRepository;
import com.diedari.jimdur.repository.EspecificacionProductoRepository;
import com.diedari.jimdur.repository.CompatibilidadProductoRepository;
import com.diedari.jimdur.repository.ProductoProveedorRepository;
import com.diedari.jimdur.dto.ProductoDTO;
import com.diedari.jimdur.mapper.ProductoMapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductoServiceTest {

    @Mock
    private ProductoRepository productoRepository;

    @Mock
    private CategoriaRepository categoriaRepository;

    @Mock
    private MarcaRepository marcaRepository;

    @Mock
    private EspecificacionProductoRepository especificacionProductoRepository;

    @Mock
    private CompatibilidadProductoRepository compatibilidadProductoRepository;

    @Mock
    private ProductoProveedorRepository productoProveedorRepository;

    @Mock
    private ProductoMapper productoMapper;

    @InjectMocks
    private ProductoServiceImpl productoService;

    private Producto producto;
    private ProductoDTO productoDTO;
    private Categoria categoria;
    private Marca marca;

    @BeforeEach
    void setUp() {
        categoria = new Categoria();
        categoria.setId(1L);
        categoria.setNombreCategoria("Motocicletas");

        marca = new Marca();
        marca.setId(1L);
        marca.setNombreMarca("Honda");

        producto = new Producto();
        producto.setIdProducto(1L);
        producto.setNombre("Moto Honda CBR 600");
        producto.setDescripcion("Moto deportiva de alta performance");
        producto.setPrecio(15000.00);
        producto.setCategoria(categoria);
        producto.setMarca(marca);

        productoDTO = new ProductoDTO();
        productoDTO.setIdProducto(1L);
        productoDTO.setNombre("Moto Honda CBR 600");
        productoDTO.setDescripcion("Moto deportiva de alta performance");
        productoDTO.setPrecio(15000.00);
        productoDTO.setIdCategoria(1L);
        productoDTO.setIdMarca(1L);
    }

    @Test
    void testObtenerTodosLosProductos() {
        List<Producto> productos = Arrays.asList(producto);
        when(productoRepository.findAll()).thenReturn(productos);
        when(productoMapper.toDTOList(productos)).thenReturn(Arrays.asList(productoDTO));

        List<ProductoDTO> result = productoService.obtenerTodosLosProductos();

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(productoRepository).findAll();
        verify(productoMapper).toDTOList(productos);
    }

    @Test
    void testObtenerProductoPorId_WhenExists() {
        when(productoRepository.findById(1L)).thenReturn(Optional.of(producto));
        when(productoMapper.toDTO(producto)).thenReturn(productoDTO);

        ProductoDTO result = productoService.obtenerProductoPorId(1L);

        assertNotNull(result);
        assertEquals(productoDTO, result);
        verify(productoRepository).findById(1L);
    }

    @Test
    void testObtenerProductoPorId_WhenNotExists() {
        when(productoRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> {
            productoService.obtenerProductoPorId(999L);
        });
        verify(productoRepository).findById(999L);
    }

    @Test
    void testGuardarProducto() {
        when(categoriaRepository.findById(1L)).thenReturn(Optional.of(categoria));
        when(marcaRepository.findById(1L)).thenReturn(Optional.of(marca));
        when(productoMapper.toEntity(productoDTO)).thenReturn(producto);
        when(productoRepository.save(any(Producto.class))).thenReturn(producto);
        when(productoMapper.toDTO(producto)).thenReturn(productoDTO);

        ProductoDTO result = productoService.guardarProducto(productoDTO);

        assertNotNull(result);
        assertEquals(productoDTO, result);
        verify(categoriaRepository).findById(1L);
        verify(marcaRepository).findById(1L);
        verify(productoRepository).save(any(Producto.class));
    }

    @Test
    void testGuardarProducto_WhenCategoriaNotFound() {
        productoDTO.setIdCategoria(999L);
        when(categoriaRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> {
            productoService.guardarProducto(productoDTO);
        });
        verify(categoriaRepository).findById(999L);
        verify(marcaRepository, never()).findById(anyLong());
        verify(productoRepository, never()).save(any(Producto.class));
    }

    @Test
    void testActualizarProducto() {
        when(productoRepository.findById(1L)).thenReturn(Optional.of(producto));
        when(categoriaRepository.findById(1L)).thenReturn(Optional.of(categoria));
        when(marcaRepository.findById(1L)).thenReturn(Optional.of(marca));
        when(productoRepository.saveAndFlush(any(Producto.class))).thenReturn(producto);
        when(productoMapper.toDTO(producto)).thenReturn(productoDTO);

        ProductoDTO result = productoService.actualizarProducto(productoDTO);

        assertNotNull(result);
        assertEquals(productoDTO, result);
        verify(productoRepository, times(2)).findById(1L);
        verify(productoRepository).saveAndFlush(any(Producto.class));
    }

    @Test
    void testEliminarProducto() {
        when(productoRepository.findById(1L)).thenReturn(Optional.of(producto));

        productoService.eliminarProducto(1L);

        verify(productoRepository).findById(1L);
        verify(productoRepository).delete(producto);
    }

    @Test
    void testEliminarProducto_WhenNotExists() {
        when(productoRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> {
            productoService.eliminarProducto(999L);
        });
        verify(productoRepository).findById(999L);
        verify(productoRepository, never()).deleteById(anyLong());
    }

    @Test
    void testExisteSkuProducto() {
        when(productoRepository.existsBySkuAndIdProductoNot("SKU123", 1L)).thenReturn(true);

        boolean result = productoService.existeSkuProducto("SKU123", 1L);

        assertTrue(result);
        verify(productoRepository).existsBySkuAndIdProductoNot("SKU123", 1L);
    }

    @Test
    void testObtenerProductoPorEstado() {
        List<Producto> productos = Arrays.asList(producto);
        when(productoRepository.findByActivo(true)).thenReturn(productos);

        List<Producto> result = productoService.obtenerProductoPorEstado(true);

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(productoRepository).findByActivo(true);
    }

    @Test
    void testObtenerProductoPorSlug() {
        when(productoRepository.findBySlug("moto-honda-cbr-600")).thenReturn(producto);

        Producto result = productoService.obtenerProductoPorSlug("moto-honda-cbr-600");

        assertNotNull(result);
        assertEquals(producto, result);
        verify(productoRepository).findBySlug("moto-honda-cbr-600");
    }
}
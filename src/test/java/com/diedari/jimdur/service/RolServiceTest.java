package com.diedari.jimdur.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.diedari.jimdur.model.Rol;
import com.diedari.jimdur.model.Permiso;
import com.diedari.jimdur.repository.RolRepository;
import com.diedari.jimdur.repository.PermisoRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import java.util.*;

class RolServiceTest {

    @Mock
    private RolRepository rolRepository;

    @Mock
    private PermisoRepository permisoRepository;

    @InjectMocks
    private RolServiceImpl rolService;

    private Rol rolEjemplo;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        rolEjemplo = Rol.builder()
                .id(1L)
                .nombre("ROLE_ADMIN")
                .descripcion("Administrador del sistema")
                .activo(true)
                .build();
    }

    @Test
    void testFindAll() {
        when(rolRepository.findAll()).thenReturn(List.of(rolEjemplo));

        List<Rol> resultado = rolService.findAll();

        assertEquals(1, resultado.size());
        assertEquals("ROLE_ADMIN", resultado.get(0).getNombre());
    }

    @Test
    void testFindAllActivos() {
        when(rolRepository.findByActivoTrue()).thenReturn(List.of(rolEjemplo));

        List<Rol> resultado = rolService.findAllActivos();

        assertEquals(1, resultado.size());
        assertTrue(resultado.get(0).getActivo());
    }

    @Test
    void testFindById() {
        when(rolRepository.findById(1L)).thenReturn(Optional.of(rolEjemplo));

        Optional<Rol> resultado = rolService.findById(1L);

        assertTrue(resultado.isPresent());
        assertEquals("ROLE_ADMIN", resultado.get().getNombre());
    }

    @Test
    void testFindByNombre() {
        when(rolRepository.findByNombre("ROLE_ADMIN")).thenReturn(Optional.of(rolEjemplo));

        Optional<Rol> resultado = rolService.findByNombre("ROLE_ADMIN");

        assertTrue(resultado.isPresent());
        assertEquals("ROLE_ADMIN", resultado.get().getNombre());
    }

    @Test
    void testSave() {
        when(rolRepository.save(rolEjemplo)).thenReturn(rolEjemplo);

        Rol resultado = rolService.save(rolEjemplo);

        assertEquals("ROLE_ADMIN", resultado.getNombre());
        assertTrue(resultado.getActivo());
    }

    @Test
    void testDeleteById() {
        doNothing().when(rolRepository).deleteById(1L);

        rolService.deleteById(1L);

        verify(rolRepository, times(1)).deleteById(1L);
    }

    @Test
    void testActivar() {
        rolEjemplo.setActivo(false);

        when(rolRepository.findById(1L)).thenReturn(Optional.of(rolEjemplo));
        when(rolRepository.save(any(Rol.class))).thenReturn(rolEjemplo);

        rolService.activar(1L);

        verify(rolRepository).save(rolEjemplo);
        assertTrue(rolEjemplo.getActivo());
    }

    @Test
    void testDesactivar() {
        rolEjemplo.setActivo(true);

        when(rolRepository.findById(1L)).thenReturn(Optional.of(rolEjemplo));
        when(rolRepository.save(any(Rol.class))).thenReturn(rolEjemplo);

        rolService.desactivar(1L);

        verify(rolRepository).save(rolEjemplo);
        assertFalse(rolEjemplo.getActivo());
    }

    @Test
    void testExistsByNombre() {
        when(rolRepository.existsByNombre("ROLE_ADMIN")).thenReturn(true);

        boolean existe = rolService.existsByNombre("ROLE_ADMIN");

        assertTrue(existe);
    }

    @Test
    void testExistsByNombreAndIdNot() {
        when(rolRepository.existsByNombreAndIdNot("ROLE_ADMIN", 2L)).thenReturn(false);

        boolean resultado = rolService.existsByNombreAndIdNot("ROLE_ADMIN", 2L);

        assertFalse(resultado);
    }

    @Test
    void testAsignarPermisos() {
        Permiso permiso1 = Permiso.builder()
                .id(1L)
                .nombre("PERMISO_1")
                .descripcion("desc")
                .build();



        Permiso permiso2 = Permiso.builder()
                .id(2L)
                .nombre("PERMISO_2")
                .descripcion("desc")
                .build();

        Set<Long> ids = Set.of(1L, 2L);
        List<Permiso> permisos = List.of(permiso1, permiso2);

        when(rolRepository.findById(1L)).thenReturn(Optional.of(rolEjemplo));
        when(permisoRepository.findAllById(ids)).thenReturn(permisos);
        when(rolRepository.save(any(Rol.class))).thenReturn(rolEjemplo);

        Rol resultado = rolService.asignarPermisos(1L, ids);

        assertEquals(2, resultado.getPermisos().size());
    }

    @Test
    void testGetPermisosDisponibles() {
        Permiso permiso = Permiso.builder()
                .id(1L)
                .nombre("PERMISO_VIEW")
                .descripcion("ver")
                .build();

        when(permisoRepository.findAll()).thenReturn(List.of(permiso));

        List<Permiso> resultado = rolService.getPermisosDisponibles();

        assertEquals(1, resultado.size());
        assertEquals("PERMISO_VIEW", resultado.get(0).getNombre());
    }
}
package com.diedari.jimdur.service;

import com.diedari.jimdur.model.Usuario;
import com.diedari.jimdur.model.Rol;
import com.diedari.jimdur.repository.UsuarioRepository;
import com.diedari.jimdur.repository.RolRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UsuarioServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private RolRepository rolRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UsuarioServiceImpl usuarioService;

    private Usuario usuario;
    private Rol rol;

    @BeforeEach
    void setUp() {
        rol = new Rol();
        rol.setId(1L);
        rol.setNombre("ADMIN");

        usuario = new Usuario();
        usuario.setId(1L);
        usuario.setNombres("Admin");
        usuario.setApellidos("User");
        usuario.setEmail("admin@jimdur.com");
        usuario.setContrasenaHash("encodedPassword");
        usuario.setEstadoCuenta("ACTIVO");
        usuario.setRoles(java.util.Set.of(rol));
    }

    @Test
    void testFindAll() {
        // Arrange
        List<Usuario> usuarios = Arrays.asList(usuario);
        when(usuarioRepository.findAll()).thenReturn(usuarios);

        // Act
        List<Usuario> result = usuarioService.findAll();

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(usuario, result.get(0));
        verify(usuarioRepository).findAll();
    }

    @Test
    void testFindById_WhenExists() {
        // Arrange
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));

        // Act
        Optional<Usuario> result = usuarioService.findById(1L);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(usuario, result.get());
        verify(usuarioRepository).findById(1L);
    }

    @Test
    void testFindById_WhenNotExists() {
        // Arrange
        when(usuarioRepository.findById(999L)).thenReturn(Optional.empty());

        // Act
        Optional<Usuario> result = usuarioService.findById(999L);

        // Assert
        assertFalse(result.isPresent());
        verify(usuarioRepository).findById(999L);
    }

    @Test
    void testFindByEmail_WhenExists() {
        // Arrange
        when(usuarioRepository.findByEmail("admin@jimdur.com")).thenReturn(Optional.of(usuario));

        // Act
        Optional<Usuario> result = usuarioService.findByEmail("admin@jimdur.com");

        // Assert
        assertTrue(result.isPresent());
        assertEquals(usuario, result.get());
        verify(usuarioRepository).findByEmail("admin@jimdur.com");
    }

    @Test
    void testSave() {
        // Arrange
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuario);

        // Act
        Usuario result = usuarioService.save(usuario);

        // Assert
        assertNotNull(result);
        assertEquals(usuario, result);
        verify(usuarioRepository).save(any(Usuario.class));
    }

    @Test
    void testExistsByEmail() {
        // Arrange
        when(usuarioRepository.existsByEmail("admin@jimdur.com")).thenReturn(true);

        // Act
        boolean result = usuarioService.existsByEmail("admin@jimdur.com");

        // Assert
        assertTrue(result);
        verify(usuarioRepository).existsByEmail("admin@jimdur.com");
    }

    @Test
    void testExistsByEmailAndIdNot() {
        // Arrange
        when(usuarioRepository.existsByEmailAndIdNot("admin@jimdur.com", 1L)).thenReturn(false);

        // Act
        boolean result = usuarioService.existsByEmailAndIdNot("admin@jimdur.com", 1L);

        // Assert
        assertFalse(result);
        verify(usuarioRepository).existsByEmailAndIdNot("admin@jimdur.com", 1L);
    }

    @Test
    void testActivar() {
        // Arrange
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuario);

        // Act
        usuarioService.activar(1L);

        // Assert
        verify(usuarioRepository).findById(1L);
        verify(usuarioRepository).save(any(Usuario.class));
        assertEquals("ACTIVO", usuario.getEstadoCuenta());
    }

    @Test
    void testDesactivar() {
        // Arrange
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuario);

        // Act
        usuarioService.desactivar(1L);

        // Assert
        verify(usuarioRepository).findById(1L);
        verify(usuarioRepository).save(any(Usuario.class));
        assertEquals("INACTIVO", usuario.getEstadoCuenta());
    }

    @Test
    void testGetRolesDisponibles() {
        List<Rol> rolesActivosEsperados = Arrays.asList(rol);
        when(rolRepository.findByActivoTrue()).thenReturn(rolesActivosEsperados);

        List<Rol> result = usuarioService.getRolesDisponibles();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(rol, result.get(0));
        verify(rolRepository).findByActivoTrue();
    }

    @Test
    void testAsignarRoles() {
        // Arrange
        Set<Long> rolesIds = Set.of(1L);
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
        when(rolRepository.findAllById(anySet())).thenReturn(Arrays.asList(rol));
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuario);

        // Act
        Usuario result = usuarioService.asignarRoles(1L, rolesIds);

        // Assert
        assertNotNull(result);
        assertEquals(usuario, result);
        verify(usuarioRepository).findById(1L);
        verify(rolRepository).findAllById(rolesIds);
        verify(usuarioRepository).save(any(Usuario.class));
    }
}
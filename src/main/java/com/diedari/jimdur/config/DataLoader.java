package com.diedari.jimdur.config;

import com.diedari.jimdur.model.Rol;
import com.diedari.jimdur.model.Usuario;
import com.diedari.jimdur.model.Vista;
import com.diedari.jimdur.repository.RolRepository;
import com.diedari.jimdur.repository.UsuarioRepository;
import com.diedari.jimdur.repository.VistaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.Set;

@Configuration
@RequiredArgsConstructor
public class DataLoader implements CommandLineRunner {
    private final RolRepository rolRepository;
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final VistaRepository vistaRepository;

    @Override
    public void run(String... args) throws Exception {
        // --- Crear roles si no existen ---
        Rol adminRol = rolRepository.findByNombre("ADMIN")
                .orElseGet(() -> rolRepository.save(Rol.builder().nombre("ADMIN").build()));
        Rol clienteRol = rolRepository.findByNombre("CLIENTE")
                .orElseGet(() -> rolRepository.save(Rol.builder().nombre("CLIENTE").build()));
        Rol gestorProductosRol = rolRepository.findByNombre("GESTOR_PRODUCTOS")
                .orElseGet(() -> rolRepository.save(Rol.builder().nombre("GESTOR_PRODUCTOS").build()));

        // --- Crear usuarios de prueba si no existen ---
        if (!usuarioRepository.existsByEmail("admin@demo.com")) {
            Usuario admin = Usuario.builder()
                    .nombres("Admin")
                    .apellidos("Demo")
                    .email("admin@demo.com")
                    .contrasenaHash(passwordEncoder.encode("admin123"))
                    .telefono("123456789")
                    .roles(Set.of(adminRol))
                    .estadoCuenta("ACTIVO")
                    .fechaRegistro(LocalDateTime.now())
                    .build();
            usuarioRepository.save(admin);
        }
        if (!usuarioRepository.existsByEmail("cliente@demo.com")) {
            Usuario cliente = Usuario.builder()
                    .nombres("Cliente")
                    .apellidos("Demo")
                    .email("cliente@demo.com")
                    .contrasenaHash(passwordEncoder.encode("cliente123"))
                    .telefono("987654321")
                    .roles(Set.of(clienteRol))
                    .estadoCuenta("ACTIVO")
                    .fechaRegistro(LocalDateTime.now())
                    .build();
            usuarioRepository.save(cliente);
        }
        if (!usuarioRepository.existsByEmail("gestor@demo.com")) {
            Usuario gestor = Usuario.builder()
                    .nombres("Gestor")
                    .apellidos("Productos")
                    .email("gestor@demo.com")
                    .contrasenaHash(passwordEncoder.encode("gestor123"))
                    .telefono("111222333")
                    .roles(Set.of(gestorProductosRol))
                    .estadoCuenta("ACTIVO")
                    .fechaRegistro(LocalDateTime.now())
                    .build();
            usuarioRepository.save(gestor);
        }

        // --- Creación/Actualización de Vistas para el control de acceso ---
        // Vistas de Super Admin
        createOrUpdateVista("Gestión de Permisos", "/admin/vistas/**", Set.of(adminRol));
        
        // Vistas de Admin
        createOrUpdateVista("Dashboard", "/admin/dashboard", Set.of(adminRol));
        createOrUpdateVista("Gestión de Usuarios", "/admin/usuarios/**", Set.of(adminRol));
        createOrUpdateVista("Perfil de Admin", "/admin/perfil-admin/**", Set.of(adminRol));
        createOrUpdateVista("Gestión de Ubicaciones", "/admin/ubicaciones/**", Set.of(adminRol));
        
        // Vistas de Gestión de Productos (Admin y Gestor)
        createOrUpdateVista("Gestión de Productos", "/admin/productos/**", Set.of(adminRol, gestorProductosRol));
        createOrUpdateVista("Gestión de Categorías", "/admin/categorias/**", Set.of(adminRol, gestorProductosRol));
        createOrUpdateVista("Gestión de Marcas", "/admin/marcas/**", Set.of(adminRol, gestorProductosRol));
        createOrUpdateVista("Gestión de Proveedores", "/admin/proveedores/**", Set.of(adminRol, gestorProductosRol));
        
        // Vistas de Usuario
        createOrUpdateVista("Mis Favoritos", "/user/favoritos", Set.of(clienteRol, adminRol, gestorProductosRol));
        createOrUpdateVista("Mi Perfil", "/user/perfil", Set.of(clienteRol, adminRol, gestorProductosRol));
    }

    private void createOrUpdateVista(String nombre, String path, Set<Rol> roles) {
        Vista vista = vistaRepository.findByPath(path)
                .orElse(Vista.builder().nombre(nombre).path(path).build());
        vista.setRoles(roles);
        vistaRepository.save(vista);
    }
} 
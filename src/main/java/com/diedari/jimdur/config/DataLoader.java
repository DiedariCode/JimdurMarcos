package com.diedari.jimdur.config;

import com.diedari.jimdur.model.Rol;
import com.diedari.jimdur.model.Usuario;
import com.diedari.jimdur.repository.RolRepository;
import com.diedari.jimdur.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;

@Configuration
@RequiredArgsConstructor
public class DataLoader implements CommandLineRunner {
    private final RolRepository rolRepository;
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        // Crear roles si no existen
        Rol adminRol = rolRepository.findByNombre("ADMIN")
                .orElseGet(() -> rolRepository.save(Rol.builder().nombre("ADMIN").build()));
        Rol clienteRol = rolRepository.findByNombre("CLIENTE")
                .orElseGet(() -> rolRepository.save(Rol.builder().nombre("CLIENTE").build()));


        // Crear usuario admin si no existe
        if (!usuarioRepository.existsByEmail("admin@demo.com")) {
            Usuario admin = Usuario.builder()
                    .nombres("Admin")   
                    .apellidos("Demo")
                    .email("admin@demo.com")
                    .contrasenaHash(passwordEncoder.encode("admin123"))
                    .telefono("123456789")
                    .rol(adminRol)
                    .estadoCuenta("ACTIVO")
                    .fechaRegistro(LocalDateTime.now())
                    .build();
            usuarioRepository.save(admin);
        }
        // Crear usuario cliente si no existe
        if (!usuarioRepository.existsByEmail("cliente@demo.com")) {
            Usuario cliente = Usuario.builder()
                    .nombres("Cliente")
                    .apellidos("Demo")
                    .email("cliente@demo.com")
                    .contrasenaHash(passwordEncoder.encode("cliente123"))
                    .telefono("987654321")
                    .rol(clienteRol)
                    .estadoCuenta("ACTIVO")
                    .fechaRegistro(LocalDateTime.now())
                    .build();
            usuarioRepository.save(cliente);
        }
    }
} 
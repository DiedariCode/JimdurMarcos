package com.diedari.jimdur.service;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.diedari.jimdur.dto.RegistroUsuarioDTO;
import com.diedari.jimdur.dto.PerfilUsuarioDTO;
import com.diedari.jimdur.model.Usuario;
import com.diedari.jimdur.model.Rol;
import com.diedari.jimdur.repository.UsuarioRepository;
import com.diedari.jimdur.repository.RolRepository;


@Service
public class UsuarioServiceImpl implements UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private RolRepository rolRepository;

    @Override
    public void crearUsuario(RegistroUsuarioDTO dto) {
        Usuario usuario = new Usuario();
        usuario.setNombres(dto.getNombres());
        usuario.setApellidos(dto.getApellidos());
        usuario.setEmail(dto.getEmail());
        usuario.setTelefono(dto.getTelefono());
        usuario.setContrasenaHash(passwordEncoder.encode(dto.getContrasena()));
        usuario.setFechaRegistro(LocalDateTime.now());
        Rol rolCliente = rolRepository.findByNombre("CLIENTE").orElseThrow(() -> new RuntimeException("Rol CLIENTE no encontrado"));
        usuario.setRol(rolCliente);
        usuario.setEstadoCuenta("ACTIVA");
        usuarioRepository.save(usuario);
    }

    @Override
    public void actualizarPerfil(Long id, PerfilUsuarioDTO dto) {
        Usuario usuario = usuarioRepository.findById(id).orElseThrow();
        usuario.setNombres(dto.getNombres());
        usuario.setApellidos(dto.getApellidos());
        usuario.setEmail(dto.getEmail());
        usuario.setTelefono(dto.getTelefono());
        usuarioRepository.save(usuario);
    }

    @Override
    public void actualizarFotoPerfil(Long id, String nombreImagen) {
        Usuario usuario = usuarioRepository.findById(id).orElseThrow();
        usuario.setImagen(nombreImagen);
        usuarioRepository.save(usuario);
    }

    @Override
    public PerfilUsuarioDTO obtenerPerfil(Long id) {
        Usuario usuario = usuarioRepository.findById(id).orElseThrow();
        return new PerfilUsuarioDTO(
            usuario.getId(),
            usuario.getNombres(),
            usuario.getApellidos(),
            usuario.getEmail(),
            usuario.getTelefono(),
            usuario.getImagen(),
            usuario.getRol() != null ? usuario.getRol().getNombre() : null
        );
    }

    @Override
    public PerfilUsuarioDTO obtenerPerfilPorEmail(String email) {
        Usuario usuario = usuarioRepository.findByEmail(email).orElseThrow();
        return new PerfilUsuarioDTO(
            usuario.getId(),
            usuario.getNombres(),
            usuario.getApellidos(),
            usuario.getEmail(),
            usuario.getTelefono(),
            usuario.getImagen(),
            usuario.getRol() != null ? usuario.getRol().getNombre() : null
        );
    }

    @Override
    public Usuario obtenerUsuarioPorId(Long id) {
        return usuarioRepository.findById(id).orElseThrow();
    }

    @Override
    public Usuario buscarPorEmail(String email) {
        return usuarioRepository.findByEmail(email).orElseThrow();
    }

}

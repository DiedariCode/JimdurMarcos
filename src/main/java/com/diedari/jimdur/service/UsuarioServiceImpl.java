package com.diedari.jimdur.service;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.diedari.jimdur.dto.RegistroUsuarioDTO;
import com.diedari.jimdur.model.Usuario;
import com.diedari.jimdur.repository.UsuarioRepository;


@Service
public class UsuarioServiceImpl implements UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void crearUsuario(RegistroUsuarioDTO dto) {
        Usuario usuario = new Usuario();
        usuario.setNombres(dto.getNombres());
        usuario.setApellidos(dto.getApellidos());
        usuario.setEmail(dto.getEmail());
        usuario.setTelefono(dto.getTelefono());
        usuario.setContrasenaHash(passwordEncoder.encode(dto.getContrasena()));
        usuario.setFechaRegistro(LocalDateTime.now());
        usuario.setRol("ROLE_USER");
        usuario.setEstadoCuenta("ACTIVA");
        usuarioRepository.save(usuario);
    }

}

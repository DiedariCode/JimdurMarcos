package com.diedari.jimdur.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.HashSet;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.diedari.jimdur.dto.RegistroUsuarioDTO;
import com.diedari.jimdur.model.Usuario;
import com.diedari.jimdur.model.Rol;
import com.diedari.jimdur.repository.UsuarioRepository;
import com.diedari.jimdur.repository.RolRepository;

@Service
@Transactional
public class UsuarioServiceImpl implements UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private RolRepository rolRepository;

    @Override
    @Transactional
    public void crearUsuario(RegistroUsuarioDTO dto) {
        Usuario usuario = new Usuario();
        usuario.setNombres(dto.getNombres());
        usuario.setApellidos(dto.getApellidos());
        usuario.setEmail(dto.getEmail());
        usuario.setTelefono(dto.getTelefono());
        usuario.setContrasenaHash(passwordEncoder.encode(dto.getContrasena()));
        usuario.setFechaRegistro(LocalDateTime.now());
        
        // Asignar rol por defecto (USER)
        Rol rolUser = rolRepository.findByNombre("ROLE_USER")
                .orElseThrow(() -> new RuntimeException("Rol ROLE_USER no encontrado"));
        usuario.setRoles(Set.of(rolUser));
        usuario.setEstadoCuenta("ACTIVO");
        
        usuarioRepository.save(usuario);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Usuario> findAll() {
        return usuarioRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Usuario> findById(Long id) {
        return usuarioRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Usuario> findByEmail(String email) {
        return usuarioRepository.findByEmail(email);
    }

    @Override
    @Transactional
    public Usuario save(Usuario usuario) {
        return usuarioRepository.save(usuario);
    }

    @Override
    @Transactional
    public Usuario asignarRoles(Long usuarioId, Set<Long> rolesIds) {
        Optional<Usuario> usuarioOpt = usuarioRepository.findById(usuarioId);
        if (usuarioOpt.isPresent()) {
            Usuario usuario = usuarioOpt.get();
            
            // Obtener los roles por IDs
            Set<Rol> roles = new HashSet<>();
            if (rolesIds != null && !rolesIds.isEmpty()) {
                roles = rolRepository.findAllById(rolesIds)
                    .stream()
                    .collect(Collectors.toSet());
            }
            
            // Si no se proporcionan roles, mantener los roles actuales
            if (roles.isEmpty()) {
                if (usuario.getRoles() == null || usuario.getRoles().isEmpty()) {
                    // Solo asignar USER si no hay roles actuales
                    Rol rolUser = rolRepository.findByNombre("ROLE_USER")
                        .orElseThrow(() -> new RuntimeException("Rol ROLE_USER no encontrado"));
                    roles.add(rolUser);
                } else {
                    // Mantener los roles actuales
                    roles = usuario.getRoles();
                }
            }
            
            usuario.setRoles(roles);
            return usuarioRepository.save(usuario);
        }
        throw new RuntimeException("Usuario no encontrado con ID: " + usuarioId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Rol> getRolesDisponibles() {
        return rolRepository.findAll().stream()
            .filter(rol -> rol.getActivo() != null && rol.getActivo())
            .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void activar(Long id) {
        Optional<Usuario> usuarioOpt = usuarioRepository.findById(id);
        if (usuarioOpt.isPresent()) {
            Usuario usuario = usuarioOpt.get();
            usuario.setEstadoCuenta("ACTIVO");
            usuarioRepository.save(usuario);
        } else {
            throw new RuntimeException("Usuario no encontrado con ID: " + id);
        }
    }

    @Override
    @Transactional
    public void desactivar(Long id) {
        Optional<Usuario> usuarioOpt = usuarioRepository.findById(id);
        if (usuarioOpt.isPresent()) {
            Usuario usuario = usuarioOpt.get();
            usuario.setEstadoCuenta("INACTIVO");
            usuarioRepository.save(usuario);
        } else {
            throw new RuntimeException("Usuario no encontrado con ID: " + id);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByEmail(String email) {
        return usuarioRepository.existsByEmail(email);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByEmailAndIdNot(String email, Long id) {
        return usuarioRepository.existsByEmailAndIdNot(email, id);
    }
}

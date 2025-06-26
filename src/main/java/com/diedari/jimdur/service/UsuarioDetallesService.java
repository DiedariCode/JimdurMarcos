package com.diedari.jimdur.service;

import com.diedari.jimdur.model.Permiso;
import com.diedari.jimdur.model.Rol;
import com.diedari.jimdur.model.Usuario;
import com.diedari.jimdur.repository.UsuarioRepository;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class UsuarioDetallesService implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;

    public UsuarioDetallesService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() ->
                        new UsernameNotFoundException("Usuario no encontrado con el email: " + email));

        // Verifica que el usuario esté activo (según tu lógica)
        if (!"ACTIVO".equalsIgnoreCase(usuario.getEstadoCuenta())) {
            throw new UsernameNotFoundException("Usuario inactivo: " + email);
        }

        return new User(
                usuario.getEmail(),
                usuario.getContrasenaHash(),
                usuario.isHabilitado(),  // enabled
                true,                    // accountNonExpired
                true,                    // credentialsNonExpired
                true,                    // accountNonLocked
                mapRolesAndPermissionsToAuthorities(usuario.getRoles())
        );
    }

    /**
     * Convierte roles y permisos en authorities.
     */
    private Collection<? extends GrantedAuthority> mapRolesAndPermissionsToAuthorities(Set<Rol> roles) {
        Stream<GrantedAuthority> rolesStream = roles.stream()
                .map(rol -> new SimpleGrantedAuthority(rol.getNombre()));

        Stream<GrantedAuthority> permisosStream = roles.stream()
                .flatMap(rol -> rol.getPermisos().stream())
                .map(permiso -> new SimpleGrantedAuthority(permiso.getNombre()));

        return Stream.concat(rolesStream, permisosStream)
                .collect(Collectors.toSet()); // Evita duplicados
    }
}

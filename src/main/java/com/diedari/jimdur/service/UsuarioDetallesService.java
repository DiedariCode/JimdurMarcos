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

    /**
     * Servicio personalizado que implementa UserDetailsService,
     * requerido por Spring Security para cargar usuarios en el proceso de
     * autenticación.
     *
     * Anotado con @Transactional(readOnly = true) porque solo se realizan
     * operaciones de lectura.
     *
     * Al autenticar, Spring llama automáticamente a loadUserByUsername(), pasando
     * el email del usuario.
     * Si el usuario existe y está activo, se construye un UserDetails con sus
     * credenciales y permisos.
     */
    @Override
    @Transactional(readOnly = true) // SOLO LECTURA
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado con el email: " + email));

        // Verifica que el usuario esté activo (según tu lógica)
        if (!"ACTIVO".equalsIgnoreCase(usuario.getEstadoCuenta())) {
            throw new UsernameNotFoundException("Usuario inactivo: " + email);
        }

        return new User(
                usuario.getEmail(),
                usuario.getContrasenaHash(),
                usuario.isHabilitado(), // enabled
                true, // accountNonExpired: la cuenta NO ha expirado
                true, // credentialsNonExpired: la contraseña NO ha expirado
                true, // accountNonLocked: la cuenta NO está bloqueada
                mapRolesAndPermissionsToAuthorities(usuario.getRoles()));
    }

    /**
     * Convierte roles y permisos en authorities.
     */

    // Esto transforma los roles y permisos en objetos GrantedAuthority, que Spring
    // Security entiende para validar reglas como
    // hasAuthority o PreAuthority
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

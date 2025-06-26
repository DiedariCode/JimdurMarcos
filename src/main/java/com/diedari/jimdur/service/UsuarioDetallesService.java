package com.diedari.jimdur.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.diedari.jimdur.model.Usuario;
import com.diedari.jimdur.model.Rol;
import com.diedari.jimdur.model.Permiso;
import com.diedari.jimdur.repository.UsuarioRepository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;

@Service
public class UsuarioDetallesService implements UserDetailsService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado: " + email));

        // Verificar que el usuario esté activo
        if (!"ACTIVO".equals(usuario.getEstadoCuenta())) {
            throw new UsernameNotFoundException("Usuario inactivo: " + email);
        }

        return new User(
                usuario.getEmail(),
                usuario.getContrasenaHash(),
                getAuthorities(usuario.getRoles())
        );
    }

    /**
     * Convierte los roles y permisos del usuario en authorities de Spring Security
     */
    private Collection<SimpleGrantedAuthority> getAuthorities(Set<Rol> roles) {
        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        
        for (Rol rol : roles) {
            // Agregar el rol como authority
            authorities.add(new SimpleGrantedAuthority(rol.getNombre()));
            
            // Agregar todos los permisos del rol como authorities
            for (Permiso permiso : rol.getPermisos()) {
                authorities.add(new SimpleGrantedAuthority(permiso.getNombre()));
            }
        }
        
        return authorities;
    }
}


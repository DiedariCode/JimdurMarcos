package com.diedari.jimdur.service;

import com.diedari.jimdur.dto.RegistroUsuarioDTO;
import com.diedari.jimdur.model.Usuario;
import com.diedari.jimdur.model.Rol;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface UsuarioService {

    /**
     * Crea un nuevo usuario en la base de datos.
     *
     * @param dto el objeto DTO que contiene la información del usuario a crear
     */
    void crearUsuario(RegistroUsuarioDTO dto);
    
    /**
     * Encuentra todos los usuarios
     */
    List<Usuario> findAll();
    
    /**
     * Encuentra usuario por ID
     */
    Optional<Usuario> findById(Long id);
    
    /**
     * Encuentra usuario por email
     */
    Optional<Usuario> findByEmail(String email);
    
    /**
     * Guarda un usuario
     */
    Usuario save(Usuario usuario);
    
    /**
     * Asigna roles a un usuario
     */
    Usuario asignarRoles(Long usuarioId, Set<Long> rolesIds);
    
    /**
     * Obtiene todos los roles disponibles
     */
    List<Rol> getRolesDisponibles();
    
    /**
     * Activa un usuario
     */
    void activar(Long id);
    
    /**
     * Desactiva un usuario
     */
    void desactivar(Long id);
    
    /**
     * Verifica si existe un usuario con el email dado
     */
    boolean existsByEmail(String email);
    
    /**
     * Verifica si existe un usuario con el email dado excluyendo un ID específico
     */
    boolean existsByEmailAndIdNot(String email, Long id);
}


package com.diedari.jimdur.service;

import com.diedari.jimdur.dto.RegistroUsuarioDTO;
import com.diedari.jimdur.dto.PerfilUsuarioDTO;
import com.diedari.jimdur.model.Rol;
import com.diedari.jimdur.model.Usuario;

import java.util.List;

public interface UsuarioService {

    /**
     * Crea un nuevo usuario en la base de datos.
     *
     * @param dto el objeto DTO que contiene la información del usuario a crear
     */
    void crearUsuario(RegistroUsuarioDTO dto);

    void actualizarPerfil(Long id, PerfilUsuarioDTO dto);
    void actualizarFotoPerfil(Long id, String nombreImagen);
    PerfilUsuarioDTO obtenerPerfil(Long id);
    PerfilUsuarioDTO obtenerPerfilPorEmail(String email);
    Usuario buscarPorEmail(String email);
    Usuario obtenerUsuarioPorId(Long id);

    List<Usuario> findAllTrabajadores();
    List<Usuario> findAllClientes();
    List<Rol> findAllTrabajadorRoles();
    void crearTrabajador(RegistroUsuarioDTO dto);
    void actualizarTrabajador(Long id, RegistroUsuarioDTO dto);
    RegistroUsuarioDTO obtenerUsuarioParaEditar(Long id);
}


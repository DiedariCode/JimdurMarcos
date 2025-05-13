package com.diedari.jimdur.service;

import java.util.List;

import com.diedari.jimdur.model.Usuario;

public interface UsuarioService {

    public List<Usuario> listarTodosLosUsuarios();

    public Usuario guardarUsuario(Usuario usuario);

    public Usuario obtenerUsuarioPorId(Long id);

    public Usuario actualizarUsuario(Usuario usuario);

    public void eliminarUsuario(Long id);

}

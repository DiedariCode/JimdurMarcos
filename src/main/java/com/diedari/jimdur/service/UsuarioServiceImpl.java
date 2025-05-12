package com.diedari.jimdur.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.diedari.jimdur.model.Usuario;
import com.diedari.jimdur.repository.UsuarioRepository;

@Service
public class UsuarioServiceImpl implements UsuarioService {

    @Autowired
    private UsuarioRepository Repository;

    @Override
    public List<Usuario> listarTodosLosUsuarios() {
        return Repository.findAll();
    }

    @Override
    public Usuario guardarUsuario(Usuario usuario) {
        return Repository.save(usuario);
    }

    @Override
    public Usuario obtenerUsuarioPorId(Long id) {
        return Repository.findById(id).get();
    }

    @Override
    public Usuario actualizarUsuario(Usuario usuario) {
        return Repository.save(usuario);
    }

    @Override
    public void eliminarUsuario(Long id) {
        Repository.deleteById(id);
    }

}

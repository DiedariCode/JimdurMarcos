package com.diedari.jimdur.controller;

import com.diedari.jimdur.model.Usuario;
import com.diedari.jimdur.service.UsuarioService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioRestController {

    @Autowired
    private UsuarioService service;

    // Obtener todos los usuarios
    @GetMapping
    public List<Usuario> listarUsuarios() {
        return service.listarTodosLosUsuarios();
    }

    // Obtener usuario por ID
    @GetMapping("/{id}")
    public Usuario obtenerUsuarioPorId(@PathVariable Long id) {
        return service.obtenerUsuarioPorId(id);
    }

    // Crear nuevo usuario
    @PostMapping
    public Usuario guardarUsuario(@RequestBody Usuario usuario) {
        return service.guardarUsuario(usuario);
    }

    // Actualizar usuario existente
    @PutMapping("/{id}")
    public Usuario actualizarUsuario(@PathVariable Long id, @RequestBody Usuario usuario) {
        Usuario existente = service.obtenerUsuarioPorId(id);
        if (existente != null) {
            existente.setNombre(usuario.getNombre());
            existente.setApellido(usuario.getApellido());
            existente.setCorreo(usuario.getCorreo());
            existente.setContraseña(usuario.getContraseña());
            return service.actualizarUsuario(existente);
        }
        return null; // O lanzar excepción si no se encuentra
    }

    // Eliminar usuario
    @DeleteMapping("/{id}")
    public void eliminarUsuario(@PathVariable Long id) {
        service.eliminarUsuario(id);
    }
    
}

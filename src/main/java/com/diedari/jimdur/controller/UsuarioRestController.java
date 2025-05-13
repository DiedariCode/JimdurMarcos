package com.diedari.jimdur.controller;

import com.diedari.jimdur.model.Usuario;
import com.diedari.jimdur.service.UsuarioService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
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
        // ! TODAVIA NO ESTA IMPLEMENTADO EL REGISTRO DE USUARIO
        usuario.setEstado(true);
        usuario.setUltimoAcceso(LocalDateTime.of(2023, 5, 1, 14, 32));

        return service.guardarUsuario(usuario);
    }

    // Actualizar usuario existente
    @PutMapping("/{id}")
    public ResponseEntity<Usuario> actualizarUsuario(@PathVariable Long id, @RequestBody Usuario usuario) {
        Usuario existente = service.obtenerUsuarioPorId(id);
        if (existente == null) {
            return ResponseEntity.notFound().build(); // Devuelve 404 si no se encuentra
        }

        // Actualizar los campos
        existente.setNombres(usuario.getNombres());
        existente.setCorreo(usuario.getCorreo());
        existente.setNumeroTelefono(usuario.getNumeroTelefono());
        existente.setContraseña(usuario.getContraseña());

        Usuario actualizado = service.actualizarUsuario(existente);
        return ResponseEntity.ok(actualizado);
    }

    // Eliminar usuario
    @DeleteMapping("/{id}")
    public void eliminarUsuario(@PathVariable Long id) {
        service.eliminarUsuario(id);
    }

}

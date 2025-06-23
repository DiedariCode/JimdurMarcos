package com.diedari.jimdur.service;

import com.diedari.jimdur.model.Rol;
import java.util.List;
import java.util.Optional;

public interface RolService {
    List<Rol> listarTodos();
    Optional<Rol> buscarPorId(Long id);
    Optional<Rol> buscarPorNombre(String nombre);
    Rol guardar(Rol rol);
    void eliminarPorId(Long id);
    boolean existePorNombre(String nombre);
    boolean existePorNombreExcluyendoId(String nombre, Long id);
    boolean puedeEliminar(Long id);
} 
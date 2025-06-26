package com.diedari.jimdur.service;

import com.diedari.jimdur.model.Rol;
import com.diedari.jimdur.model.Permiso;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface RolService {
    
    List<Rol> findAll();
    
    List<Rol> findAllActivos();
    
    Optional<Rol> findById(Long id);
    
    Optional<Rol> findByNombre(String nombre);
    
    Rol save(Rol rol);
    
    void deleteById(Long id);
    
    void activar(Long id);
    
    void desactivar(Long id);
    
    boolean existsByNombre(String nombre);
    
    boolean existsByNombreAndIdNot(String nombre, Long id);
    
    Rol asignarPermisos(Long rolId, Set<Long> permisosIds);
    
    List<Permiso> getPermisosDisponibles();
} 
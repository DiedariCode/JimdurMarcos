package com.diedari.jimdur.repository;

import com.diedari.jimdur.model.Permiso;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PermisoRepository extends JpaRepository<Permiso, Long> {
    
    Optional<Permiso> findByNombre(String nombre);
    
    boolean existsByNombre(String nombre);
} 
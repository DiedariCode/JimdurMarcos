package com.diedari.jimdur.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.diedari.jimdur.model.Carrito;

@Repository
public interface CarritoRepository extends JpaRepository<Carrito, Long> {
    // Custom query methods can be defined here if needed
    // For example:
    // List<Carrito> findByUsuarioId(Long usuarioId);
    
}

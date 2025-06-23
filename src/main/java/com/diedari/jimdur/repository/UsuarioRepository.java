package com.diedari.jimdur.repository;

import java.util.Optional;
import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.diedari.jimdur.model.Usuario;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    // Método para buscar un usuario por su email
    @Query("SELECT u FROM Usuario u LEFT JOIN FETCH u.roles WHERE u.email = :email")
    Optional<Usuario> findByEmail(@Param("email") String email);

    // Método para verificar si un email ya existe
    boolean existsByEmail(String email);

    @Query("SELECT DISTINCT u FROM Usuario u LEFT JOIN FETCH u.roles r WHERE r.nombre <> 'CLIENTE'")
    List<Usuario> findTrabajadores();
    
    @Query("SELECT u FROM Usuario u LEFT JOIN FETCH u.roles WHERE EXISTS (SELECT r FROM u.roles r WHERE r.nombre = 'CLIENTE') AND NOT EXISTS (SELECT r2 FROM u.roles r2 WHERE r2.nombre <> 'CLIENTE')")
    List<Usuario> findClientesPuros();
}

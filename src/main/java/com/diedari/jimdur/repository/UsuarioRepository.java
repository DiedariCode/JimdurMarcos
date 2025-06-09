package com.diedari.jimdur.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.diedari.jimdur.model.Usuario;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    // Método para buscar un usuario por su email
    Optional<Usuario> findByEmail(String email);

    // Método para verificar si un email ya existe
    boolean existsByEmail(String email);
}

package com.diedari.jimdur.repository;

import com.diedari.jimdur.model.Rol;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import java.util.Set;
import java.util.List;

public interface RolRepository extends JpaRepository<Rol, Long> {
    Optional<Rol> findByNombre(String nombre);

    List<Rol> findByNombreIn(Set<String> nombres);

    Set<Rol> findByIdIn(Set<Long> ids);
} 
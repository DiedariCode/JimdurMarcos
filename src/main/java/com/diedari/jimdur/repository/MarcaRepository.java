package com.diedari.jimdur.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.diedari.jimdur.model.Marca;

@Repository
public interface MarcaRepository extends JpaRepository<Marca, Long> {

    // Búsqueda paginada por nombre
    Page<Marca> findByNombreMarcaContainingIgnoreCase(String nombreMarca, Pageable pageable);

    // Búsqueda paginada por estado
    Page<Marca> findByEstadoMarca(Boolean estadoMarca, Pageable pageable);

    // Búsqueda paginada por nombre y estado
    Page<Marca> findByNombreMarcaContainingIgnoreCaseAndEstadoMarca(String nombreMarca, Boolean estadoMarca, Pageable pageable);

    // Búsqueda por estado (sin paginación)
    List<Marca> findByEstadoMarca(Boolean estadoMarca);

    // Búsqueda ordenada de marcas activas
    List<Marca> findByEstadoMarcaTrueOrderByNombreMarcaAsc();

    // Búsqueda por nombre exacto
    List<Marca> findByNombreMarca(String nombre);
}

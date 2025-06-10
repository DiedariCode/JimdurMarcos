package com.diedari.jimdur.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.diedari.jimdur.model.Marca;

@Repository
public interface MarcaRepository extends JpaRepository<Marca, Long> {

    // Cambiar el tipo de retorno a Page<Marca> para permitir paginación
    Page<Marca> findByNombreMarcaContainingIgnoreCase(String nombreMarca, Pageable pageable);

    List<Marca> findByEstadoMarca(Boolean estadoMarca);

    List<Marca> findByEstadoMarcaTrueOrderByNombreMarcaAsc();

    List<Marca> findByNombreMarca(String nombre);
}

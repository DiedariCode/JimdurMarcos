package com.diedari.jimdur.repository;

import java.util.List;


import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.diedari.jimdur.model.Producto;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Long> {
    
    // Usé @Query porque Spring Data JPA no tiene una convención directa para obtener los N últimos productos ordenados por ID descendente.

    // ESTO ES PARA LA PAGINACIÓN, AUN NO ESTA IMPLEMENTADO EN EL CONTROLADOR
    @Query("SELECT p FROM Producto p ORDER BY p.id DESC")
    List<Producto> findTopNByOrderByIdDesc(Pageable pageable);

    // Método para obtener un producto por su slug
    @Query("SELECT p FROM Producto p WHERE p.slug = ?1")
    Producto findBySlug(String slug);

}

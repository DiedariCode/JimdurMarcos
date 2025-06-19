package com.diedari.jimdur.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.diedari.jimdur.model.Categoria;

@Repository
// ? Se encarga del acceso a datos en la base de datos para la entidad Categoria
public interface CategoriaRepository extends JpaRepository<Categoria, Long> {
    // Aquí puedes agregar métodos personalizados si es necesario

    // Búsqueda por estadoActiva (sin paginación)
    List<Categoria> findByEstadoActiva(boolean estadoActiva);

    // Búsqueda por nombreCategoria
    List<Categoria> findByNombreCategoria(String nombreCategoria);

    // Búsqueda por nombreCategoria o descripcionCategoria (ignorando mayúsculas)
    List<Categoria> findByNombreCategoriaContainingIgnoreCaseOrDescripcionCategoriaContainingIgnoreCase(
            String nombreCategoria, String descripcionCategoria);

    // Búsqueda ordenada de categorías activas
    List<Categoria> findByEstadoActivaTrueOrderByNombreCategoriaAsc();

    // Búsqueda paginada por nombre
    Page<Categoria> findByNombreCategoriaContainingIgnoreCase(String nombreCategoria, Pageable pageable);

    // Búsqueda paginada por estado
    Page<Categoria> findByEstadoActiva(boolean estadoActiva, Pageable pageable);

    // Búsqueda paginada por nombre y estado
    Page<Categoria> findByNombreCategoriaContainingIgnoreCaseAndEstadoActiva(String nombreCategoria, boolean estadoActiva, Pageable pageable);
}

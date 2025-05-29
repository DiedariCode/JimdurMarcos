package com.diedari.jimdur.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.diedari.jimdur.model.Categoria;

@Repository
// ? Se encarga del acceso a datos en la base de datos para la entidad Categoria
public interface CategoriaRepository extends JpaRepository<Categoria, Long> {
    // Aquí puedes agregar métodos personalizados si es necesario

    // Buscar por estadoActiva (nombre correcto del campo)
    List<Categoria> findByEstadoActiva(boolean estadoActiva);

    // Buscar por nombreCategoria
    List<Categoria> findByNombreCategoria(String nombreCategoria);

    // Buscar por nombreCategoria o descripcionCategoria (ignorando mayúsculas)
    List<Categoria> findByNombreCategoriaContainingIgnoreCaseOrDescripcionCategoriaContainingIgnoreCase(
            String nombreCategoria, String descripcionCategoria);

    List<Categoria> findByEstadoActivaTrueOrderByNombreCategoriaAsc();
}

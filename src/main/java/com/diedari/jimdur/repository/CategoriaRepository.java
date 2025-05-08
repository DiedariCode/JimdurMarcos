package com.diedari.jimdur.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.diedari.jimdur.model.Categoria;

@Repository
public interface CategoriaRepository extends JpaRepository<Categoria, Long> {
    // Aquí puedes agregar métodos personalizados si es necesario

    // Método para encontrar una categoría por su estado "activa"
    public List<Categoria> findByActiva(boolean activa);

    // Método para encontrar una categoría por su nombre
    public List<Categoria> findByNombre(String nombre);

    // Método para encontrar una categoría por su nombre o descripción, ignorando mayúsculas y minúsculas
    public List<Categoria> findByNombreContainingIgnoreCaseOrDescripcionContainingIgnoreCase(String nombre,
            String descripcion);

}

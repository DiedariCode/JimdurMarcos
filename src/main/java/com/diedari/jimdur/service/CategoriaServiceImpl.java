package com.diedari.jimdur.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.diedari.jimdur.model.Categoria;
import com.diedari.jimdur.repository.CategoriaRepository;

@Service
public class CategoriaServiceImpl implements CategoriaService {

    // Inyectar el repositorio de categoría
    private final CategoriaRepository categoriaRepository;

    public CategoriaServiceImpl(CategoriaRepository categoriaRepository) {
        this.categoriaRepository = categoriaRepository;
    }

    @Override
    public List<Categoria> obtenerTodasLasCategorias() {
        return categoriaRepository.findAll();
    }

    @Override
    public Categoria obtenerCategoriaPorId(Long id) {
        Optional<Categoria> categoriaOpt = categoriaRepository.findById(id);
        return categoriaOpt.orElse(null);  // Devuelve null si no se encuentra la categoría
    }

    @Override
    public Categoria crearCategoria(Categoria categoria) {
        return categoriaRepository.save(categoria);
    }

    @Override
    public Categoria actualizarCategoria(Categoria categoria) {
        return categoriaRepository.save(categoria);
    }

    @Override
    public void eliminarCategoriaPorId(Long id) {
        categoriaRepository.deleteById(id);
    }

    @Override
    public List<Categoria> obtenerCategoriaPorEstado(boolean activa) {
        return categoriaRepository.findByEstadoActiva(activa); 
    }

    @Override
    public List<Categoria> obtenerCategoriaPorNombre(String nombre) {
        if (nombre == null || nombre.isEmpty()) {
            return categoriaRepository.findAll(); // Si no se proporciona un nombre, devuelve todas las categorías   
        }
        return categoriaRepository.findByNombreCategoria(nombre); 
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Categoria> obtenerCategoriasFiltradas(String nombreCategoria, String estadoCategoria, Pageable pageable) {
        // Si no hay filtros, retornar todas las categorías paginadas
        if ((nombreCategoria == null || nombreCategoria.isEmpty()) && 
            (estadoCategoria == null || estadoCategoria.isEmpty())) {
            return categoriaRepository.findAll(pageable);
        }

        // Si solo hay filtro por nombre
        if (estadoCategoria == null || estadoCategoria.isEmpty()) {
            return categoriaRepository.findByNombreCategoriaContainingIgnoreCase(nombreCategoria, pageable);
        }

        // Si solo hay filtro por estado
        if (nombreCategoria == null || nombreCategoria.isEmpty()) {
            Boolean estado = "activa".equalsIgnoreCase(estadoCategoria);
            return categoriaRepository.findByEstadoActiva(estado, pageable);
        }

        // Si hay ambos filtros
        Boolean estado = "activa".equalsIgnoreCase(estadoCategoria);
        return categoriaRepository.findByNombreCategoriaContainingIgnoreCaseAndEstadoActiva(nombreCategoria, estado, pageable);
    }
}

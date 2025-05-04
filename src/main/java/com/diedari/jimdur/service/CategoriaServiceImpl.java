package com.diedari.jimdur.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

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
        return categoriaRepository.findByActiva(activa); 
    }

    @Override
    public List<Categoria> obtenerCategoriaPorNombre(String nombre) {
        if (nombre == null || nombre.isEmpty()) {
            return categoriaRepository.findAll(); // Si no se proporciona un nombre, devuelve todas las categorías   
        }
        return categoriaRepository.findByNombre(nombre); 
    }
    
}

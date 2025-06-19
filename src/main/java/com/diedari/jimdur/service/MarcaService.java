package com.diedari.jimdur.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.diedari.jimdur.model.Marca;

public interface MarcaService {

    // Método para obtener todas las marcas
    public List<Marca> listarTodasLasMarcas();

    // Método para listar Marca por ID
    public Marca obtenerMarcaPorId(Long id);

    // Método para listar Marca por nombre
    public List<Marca> obtenerMarcaPorNombres(String nombre);

    // Método para listar Marca por estado
    public List<Marca> obtenerMarcasPorEstado(Boolean activo);

    // Método para crear una nueva marca y guardarla en la base de datos
    public Marca guardarMarcaNuevo(Marca marca);

    // Método para actualizar una marca existente
    public Marca actualizarMarca(Marca marca);

    // Método para eliminar una marca por su ID
    public void eliminarMarca(Long id);

    // Método para obtener marcas filtradas con paginación
    public Page<Marca> obtenerMarcasFiltradas(
        String nombreMarca,
        String estadoMarca,
        Pageable pageable
    );
} 
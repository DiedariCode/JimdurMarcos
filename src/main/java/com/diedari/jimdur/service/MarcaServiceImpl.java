package com.diedari.jimdur.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.diedari.jimdur.model.Marca;
import com.diedari.jimdur.repository.MarcaRepository;

@Service
public class MarcaServiceImpl implements MarcaService {

    @Autowired
    private MarcaRepository marcaRepository;

    @Override
    public Marca actualizarMarca(Marca marca) {
        return marcaRepository.save(marca);
    }

    @Override
    public void eliminarMarca(Long id) {
        marcaRepository.deleteById(id);

    }

    @Override
    public Marca guardarMarcaNuevo(Marca marca) {
        return marcaRepository.save(marca);
    }

    @Override
    public List<Marca> listarTodasLasMarcas() {
        return marcaRepository.findAll();
    }

    @Override
    public Marca obtenerMarcaPorId(Long id) {
        return marcaRepository.findById(id).get();
    }

    @Override
    public List<Marca> obtenerMarcaPorNombres(String nombre) {
        if (nombre == null || nombre.isEmpty()) {
            return marcaRepository.findAll();
        }
        return marcaRepository.findByNombreMarca(nombre);
    }

    @Override
    public List<Marca> obtenerMarcasPorEstado(Boolean activo) {
        return marcaRepository.findByEstadoMarca(activo);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Marca> obtenerMarcasFiltradas(String nombreMarca, String estadoMarca, Pageable pageable) {
        // Si no hay filtros, retornar todas las marcas paginadas
        if ((nombreMarca == null || nombreMarca.isEmpty()) && 
            (estadoMarca == null || estadoMarca.isEmpty())) {
            return marcaRepository.findAll(pageable);
        }

        // Si solo hay filtro por nombre
        if (estadoMarca == null || estadoMarca.isEmpty()) {
            return marcaRepository.findByNombreMarcaContainingIgnoreCase(nombreMarca, pageable);
        }

        // Si solo hay filtro por estado
        if (nombreMarca == null || nombreMarca.isEmpty()) {
            Boolean estado = "activa".equalsIgnoreCase(estadoMarca);
            return marcaRepository.findByEstadoMarca(estado, pageable);
        }

        // Si hay ambos filtros
        Boolean estado = "activa".equalsIgnoreCase(estadoMarca);
        return marcaRepository.findByNombreMarcaContainingIgnoreCaseAndEstadoMarca(nombreMarca, estado, pageable);
    }

}

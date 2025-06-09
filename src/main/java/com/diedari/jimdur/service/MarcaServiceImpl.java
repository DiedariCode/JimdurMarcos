package com.diedari.jimdur.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

}

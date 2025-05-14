package com.diedari.jimdur.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.diedari.jimdur.model.Ubicacion;
import com.diedari.jimdur.repository.UbicacionRepository;

@Service
public class UbicacionServiceImpl implements UbicacionService {

    @Autowired
    private UbicacionRepository ubicacionRepository;

    @Override
    public Ubicacion actualizarUbicacion(Ubicacion ubicacion) {
        // Verifica que la ubicación exista antes de actualizarla
        if (!ubicacionRepository.existsById(ubicacion.getIdUbicacion())) {
            throw new RuntimeException(
                    "No se puede actualizar. Ubicación no encontrada con id: " + ubicacion.getIdUbicacion());
        }
        return ubicacionRepository.save(ubicacion);
    }

    @Override
    public void eliminarUbicacion(Integer idUbicacion) {
        // Verifica si hay productos asociados a esta ubicación antes de eliminarla
        Ubicacion ubicacion = obtenerUbicacionPorId(idUbicacion);
        if (ubicacion.getProductos() != null && !ubicacion.getProductos().isEmpty()) {
            throw new RuntimeException("No se puede eliminar la ubicación porque tiene productos asociados");
        }
        ubicacionRepository.deleteById(idUbicacion);

    }

    @Override
    public Ubicacion guardarUbicacion(Ubicacion ubicacion) {
        return ubicacionRepository.save(ubicacion);
    }

    @Override
    public List<Ubicacion> listarUbicaciones() {
        return ubicacionRepository.findAll(); 
    }

    @Override
    public Ubicacion obtenerUbicacionPorId(Integer idUbicacion) {
        return ubicacionRepository.findById(idUbicacion)
                .orElseThrow(() -> new RuntimeException("Ubicación no encontrada con id: " + idUbicacion));
    }

}

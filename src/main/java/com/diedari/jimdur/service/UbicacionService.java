package com.diedari.jimdur.service;

import java.util.List;

import com.diedari.jimdur.dto.UbicacionDTO;
import com.diedari.jimdur.model.Ubicaciones;

public interface UbicacionService {
    public List<Ubicaciones> listarUbicaciones();
    public Ubicaciones obtenerUbicacionPorId(Long idUbicacion);
    public Ubicaciones guardarUbicacion(Ubicaciones ubicacion);
    public void eliminarUbicacion(Long idUbicacion);
    public Ubicaciones actualizarUbicacion(Ubicaciones ubicacion);
    
    // Nuevos métodos para UX mejorada
    public List<UbicacionDTO> listarUbicacionesConOcupacion();
    public UbicacionDTO obtenerUbicacionConOcupacion(Long idUbicacion);
    public boolean validarCapacidadDisponible(Long idUbicacion, Integer cantidadAgregar);
    public Integer obtenerEspacioDisponible(Long idUbicacion);
    public Double calcularPorcentajeOcupacion(Long idUbicacion);
}

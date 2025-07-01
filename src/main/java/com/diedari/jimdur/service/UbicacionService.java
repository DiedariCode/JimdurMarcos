package com.diedari.jimdur.service;

import java.util.List;

import com.diedari.jimdur.model.Ubicaciones;

public interface UbicacionService {
    public List<Ubicaciones> listarUbicaciones();
    public Ubicaciones obtenerUbicacionPorId(Long idUbicacion);
    public Ubicaciones guardarUbicacion(Ubicaciones ubicacion);
    public void eliminarUbicacion(Long idUbicacion);
    public Ubicaciones actualizarUbicacion(Ubicaciones ubicacion);
}

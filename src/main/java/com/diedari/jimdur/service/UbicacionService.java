package com.diedari.jimdur.service;

import java.util.List;

import com.diedari.jimdur.model.Ubicacion;

public interface UbicacionService {
    public List<Ubicacion> listarUbicaciones();
    public Ubicacion obtenerUbicacionPorId(Integer idUbicacion);
    public Ubicacion guardarUbicacion(Ubicacion ubicacion);
    public void eliminarUbicacion(Integer idUbicacion);
    public Ubicacion actualizarUbicacion(Ubicacion ubicacion);
    
}

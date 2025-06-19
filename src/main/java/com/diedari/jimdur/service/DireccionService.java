package com.diedari.jimdur.service;

import com.diedari.jimdur.model.Direccion;
import java.util.List;

public interface DireccionService {
    List<Direccion> listarDireccionesPorUsuario(Long idUsuario);
    Direccion guardarDireccion(Direccion direccion);
    Direccion obtenerDireccionPorId(Long idDireccion);
    void eliminarDireccion(Long idDireccion);
} 
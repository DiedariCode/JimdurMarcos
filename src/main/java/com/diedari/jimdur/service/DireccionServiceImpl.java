package com.diedari.jimdur.service;

import com.diedari.jimdur.model.Direccion;
import com.diedari.jimdur.model.Usuario;
import com.diedari.jimdur.repository.DireccionRepository;
import com.diedari.jimdur.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DireccionServiceImpl implements DireccionService {
    @Autowired
    private DireccionRepository direccionRepository;
    @Autowired
    private UsuarioRepository usuarioRepository;

    @Override
    public List<Direccion> listarDireccionesPorUsuario(Long idUsuario) {
        return direccionRepository.findAll().stream()
                .filter(d -> d.getUsuario().getId().equals(idUsuario))
                .collect(Collectors.toList());
    }

    @Override
    public Direccion guardarDireccion(Direccion direccion) {
        return direccionRepository.save(direccion);
    }

    @Override
    public Direccion obtenerDireccionPorId(Long idDireccion) {
        return direccionRepository.findById(idDireccion).orElse(null);
    }

    @Override
    public void eliminarDireccion(Long idDireccion) {
        direccionRepository.deleteById(idDireccion);
    }
} 
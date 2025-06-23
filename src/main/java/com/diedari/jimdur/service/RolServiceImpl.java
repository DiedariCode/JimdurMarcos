package com.diedari.jimdur.service;

import com.diedari.jimdur.model.Rol;
import com.diedari.jimdur.repository.RolRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RolServiceImpl implements RolService {

    private final RolRepository rolRepository;
    private static final List<String> ROLES_BASE = List.of("ADMIN", "CLIENTE", "GESTOR_PRODUCTOS");

    public RolServiceImpl(RolRepository rolRepository) {
        this.rolRepository = rolRepository;
    }

    @Override
    public List<Rol> listarTodos() {
        return rolRepository.findAll();
    }

    @Override
    public Optional<Rol> buscarPorId(Long id) {
        return rolRepository.findById(id);
    }

    @Override
    public Optional<Rol> buscarPorNombre(String nombre) {
        return rolRepository.findByNombre(nombre);
    }

    @Override
    public Rol guardar(Rol rol) {
        return rolRepository.save(rol);
    }

    @Override
    public void eliminarPorId(Long id) {
        rolRepository.deleteById(id);
    }

    @Override
    public boolean existePorNombre(String nombre) {
        return rolRepository.findByNombre(nombre).isPresent();
    }

    @Override
    public boolean existePorNombreExcluyendoId(String nombre, Long id) {
        Optional<Rol> rol = rolRepository.findByNombre(nombre);
        return rol.isPresent() && !rol.get().getId().equals(id);
    }

    @Override
    public boolean puedeEliminar(Long id) {
        Optional<Rol> rolOpt = rolRepository.findById(id);
        if (rolOpt.isEmpty()) return false;
        Rol rol = rolOpt.get();
        if (ROLES_BASE.contains(rol.getNombre())) return false;
        return (rol.getUsuarios() == null || rol.getUsuarios().isEmpty())
            && (rol.getVistas() == null || rol.getVistas().isEmpty());
    }
} 
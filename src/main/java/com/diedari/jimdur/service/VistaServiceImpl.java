package com.diedari.jimdur.service;

import com.diedari.jimdur.model.Vista;
import com.diedari.jimdur.repository.VistaRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class VistaServiceImpl implements VistaService {

    private final VistaRepository vistaRepository;

    public VistaServiceImpl(VistaRepository vistaRepository) {
        this.vistaRepository = vistaRepository;
    }

    @Override
    public List<Vista> findAll() {
        return vistaRepository.findAll();
    }

    @Override
    public Vista save(Vista vista) {
        return vistaRepository.save(vista);
    }

    @Override
    public List<Vista> findAllWithRoles() {
        return vistaRepository.findAllWithRoles();
    }

    @Override
    public Optional<Vista> findByIdWithRoles(Long id) {
        return vistaRepository.findByIdWithRoles(id);
    }
} 
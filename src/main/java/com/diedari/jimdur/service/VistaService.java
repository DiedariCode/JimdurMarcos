package com.diedari.jimdur.service;

import com.diedari.jimdur.model.Vista;

import java.util.List;
import java.util.Optional;

public interface VistaService {
    List<Vista> findAll();
    Vista save(Vista vista);
    List<Vista> findAllWithRoles();
    Optional<Vista> findByIdWithRoles(Long id);
} 
package com.diedari.jimdur.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.diedari.jimdur.model.Direccion;

@Repository
public interface DireccionRepository extends JpaRepository<Direccion, Long> {

}


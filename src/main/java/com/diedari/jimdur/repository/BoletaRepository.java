package com.diedari.jimdur.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.diedari.jimdur.model.Boleta;

@Repository
public interface BoletaRepository extends JpaRepository<Boleta, Long> {

}

package com.diedari.jimdur.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.diedari.jimdur.model.Ubicaciones;

@Repository
public interface UbicacionRepository extends JpaRepository<Ubicaciones, Long> {
    
}

package com.diedari.jimdur.repository;

import com.diedari.jimdur.model.MedioPago;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MedioPagoRepository extends JpaRepository<MedioPago, Long> {
    // Métodos personalizados si se requieren
} 
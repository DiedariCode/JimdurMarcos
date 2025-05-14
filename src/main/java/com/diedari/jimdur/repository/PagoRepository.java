package com.diedari.jimdur.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.diedari.jimdur.model.Pago;

@Repository
public interface PagoRepository extends JpaRepository<Pago, Long> {

}

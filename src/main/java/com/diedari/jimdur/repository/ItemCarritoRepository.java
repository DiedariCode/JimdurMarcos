package com.diedari.jimdur.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.diedari.jimdur.model.ItemCarrito;

@Repository
public interface ItemCarritoRepository extends JpaRepository<ItemCarrito, Long> {
 
}

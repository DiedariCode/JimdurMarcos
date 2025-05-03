package com.diedari.jimdur.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.diedari.jimdur.model.Producto;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Long> 
{

}

package com.diedari.jimdur.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.diedari.jimdur.model.Pedido;

@Repository
public interface PedidoRepository extends JpaRepository<Pedido, Long> {

   
}

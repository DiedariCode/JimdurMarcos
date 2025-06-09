package com.diedari.jimdur.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.diedari.jimdur.model.Proveedor;

@Repository
public interface ProveedorRepository extends JpaRepository<Proveedor, Long> {

    List<Proveedor> findByEstadoActivoOrderByNombreAsc(String estado);

    List<Proveedor> findByEstadoActivo(String estadoActivo);
}

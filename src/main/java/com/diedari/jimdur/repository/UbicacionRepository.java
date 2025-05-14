package com.diedari.jimdur.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.diedari.jimdur.model.Ubicacion;

@Repository
public interface UbicacionRepository extends JpaRepository<Ubicacion, Long> {
    
    // Aquí puedes agregar métodos personalizados si es necesario
    // Por ejemplo, para buscar ubicaciones por nombre o código postal
    // List<Ubicacion> findByNombre(String nombre);
    // List<Ubicacion> findByCodigoPostal(String codigoPostal);
    
}

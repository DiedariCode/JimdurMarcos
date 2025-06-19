package com.diedari.jimdur.repository;

import com.diedari.jimdur.model.Favorito;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface FavoritoRepository extends JpaRepository<Favorito, Long> {
    // Métodos personalizados si se requieren
    Optional<Favorito> findByUsuarioIdAndProductoIdProducto(Long idUsuario, Long idProducto);
} 
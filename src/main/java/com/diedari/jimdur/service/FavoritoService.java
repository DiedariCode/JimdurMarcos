package com.diedari.jimdur.service;

import com.diedari.jimdur.dto.FavoritoDTO;
import java.util.List;

public interface FavoritoService {
    FavoritoDTO guardarFavorito(FavoritoDTO favoritoDTO);
    void eliminarFavorito(Long id);
    List<FavoritoDTO> listarFavoritosPorUsuario(Long idUsuario);
} 
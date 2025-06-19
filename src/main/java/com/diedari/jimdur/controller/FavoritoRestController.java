package com.diedari.jimdur.controller;

import com.diedari.jimdur.dto.FavoritoDTO;
import com.diedari.jimdur.service.FavoritoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/favoritos")
public class FavoritoRestController {
    @Autowired
    private FavoritoService favoritoService;

    @PostMapping
    public FavoritoDTO agregarFavorito(@RequestBody FavoritoDTO dto) {
        return favoritoService.guardarFavorito(dto);
    }

    @DeleteMapping("/{id}")
    public void eliminarFavorito(@PathVariable Long id) {
        favoritoService.eliminarFavorito(id);
    }

    @GetMapping("/usuario/{idUsuario}")
    public List<FavoritoDTO> listarFavoritos(@PathVariable Long idUsuario) {
        return favoritoService.listarFavoritosPorUsuario(idUsuario);
    }
} 
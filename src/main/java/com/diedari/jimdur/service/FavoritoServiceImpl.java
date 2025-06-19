package com.diedari.jimdur.service;

import com.diedari.jimdur.dto.FavoritoDTO;
import com.diedari.jimdur.model.Favorito;
import com.diedari.jimdur.model.Producto;
import com.diedari.jimdur.model.Usuario;
import com.diedari.jimdur.repository.FavoritoRepository;
import com.diedari.jimdur.repository.ProductoRepository;
import com.diedari.jimdur.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class FavoritoServiceImpl implements FavoritoService {
    @Autowired
    private FavoritoRepository favoritoRepository;
    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private ProductoRepository productoRepository;

    @Override
    public FavoritoDTO guardarFavorito(FavoritoDTO dto) {
        // Validar que no exista ya un favorito para ese usuario y producto
        Optional<Favorito> existente = favoritoRepository.findByUsuarioIdAndProductoIdProducto(dto.getIdUsuario(), dto.getIdProducto());
        if (existente.isPresent()) {
            throw new IllegalStateException("Este producto ya está en tus favoritos.");
        }
        Usuario usuario = usuarioRepository.findById(dto.getIdUsuario()).orElseThrow();
        Producto producto = productoRepository.findById(dto.getIdProducto()).orElseThrow();
        Favorito favorito = Favorito.builder()
                .usuario(usuario)
                .producto(producto)
                .fechaAgregado(LocalDateTime.now())
                .build();
        favorito = favoritoRepository.save(favorito);
        return mapFavoritoToDTO(favorito);
    }

    @Override
    public void eliminarFavorito(Long id) {
        favoritoRepository.deleteById(id);
    }

    @Override
    public List<FavoritoDTO> listarFavoritosPorUsuario(Long idUsuario) {
        return favoritoRepository.findAll().stream()
                .filter(f -> f.getUsuario().getId().equals(idUsuario))
                .map(this::mapFavoritoToDTO)
                .collect(Collectors.toList());
    }

    private FavoritoDTO mapFavoritoToDTO(Favorito favorito) {
        Producto producto = favorito.getProducto();
        String imagenPortada = null;
        if (producto.getImagenes() != null && !producto.getImagenes().isEmpty()) {
            imagenPortada = producto.getImagenes().stream()
                .filter(img -> Boolean.TRUE.equals(img.getEsPortada()))
                .map(img -> img.getNombreArchivo())
                .findFirst().orElse(producto.getImagenes().get(0).getNombreArchivo());
        }
        return new FavoritoDTO(
            favorito.getId(),
            favorito.getUsuario().getId(),
            producto.getIdProducto(),
            favorito.getFechaAgregado(),
            producto.getNombre(),
            producto.getSku(),
            imagenPortada
        );
    }
} 
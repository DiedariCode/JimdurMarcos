package com.diedari.jimdur.service;

import com.diedari.jimdur.dto.MedioPagoDTO;
import com.diedari.jimdur.model.MedioPago;
import com.diedari.jimdur.model.Usuario;
import com.diedari.jimdur.repository.MedioPagoRepository;
import com.diedari.jimdur.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MedioPagoServiceImpl implements MedioPagoService {
    @Autowired
    private MedioPagoRepository medioPagoRepository;
    @Autowired
    private UsuarioRepository usuarioRepository;

    @Override
    public MedioPagoDTO guardarMedioPago(MedioPagoDTO dto) {
        Usuario usuario = usuarioRepository.findById(dto.getIdUsuario()).orElseThrow();
        MedioPago medioPago = MedioPago.builder()
                .usuario(usuario)
                .tipo(MedioPago.TipoMedioPago.valueOf(dto.getTipo()))
                .datos(dto.getDatos())
                .activo(dto.getActivo())
                .build();
        medioPago = medioPagoRepository.save(medioPago);
        return new MedioPagoDTO(medioPago.getId(), usuario.getId(), medioPago.getTipo().name(), medioPago.getDatos(), medioPago.getActivo());
    }

    @Override
    public void eliminarMedioPago(Long id) {
        medioPagoRepository.deleteById(id);
    }

    @Override
    public List<MedioPagoDTO> listarMediosPagoPorUsuario(Long idUsuario) {
        return medioPagoRepository.findAll().stream()
                .filter(m -> m.getUsuario().getId().equals(idUsuario))
                .map(m -> new MedioPagoDTO(m.getId(), m.getUsuario().getId(), m.getTipo().name(), m.getDatos(), m.getActivo()))
                .collect(Collectors.toList());
    }
} 
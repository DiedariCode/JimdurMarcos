package com.diedari.jimdur.service;

import com.diedari.jimdur.dto.MedioPagoDTO;
import java.util.List;

public interface MedioPagoService {
    MedioPagoDTO guardarMedioPago(MedioPagoDTO medioPagoDTO);
    void eliminarMedioPago(Long id);
    List<MedioPagoDTO> listarMediosPagoPorUsuario(Long idUsuario);
} 
package com.diedari.jimdur.controller;

import com.diedari.jimdur.dto.MedioPagoDTO;
import com.diedari.jimdur.service.MedioPagoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/mediospago")
public class MedioPagoController {
    @Autowired
    private MedioPagoService medioPagoService;

    @PostMapping
    public MedioPagoDTO agregarMedioPago(@RequestBody MedioPagoDTO dto) {
        return medioPagoService.guardarMedioPago(dto);
    }

    @DeleteMapping("/{id}")
    public void eliminarMedioPago(@PathVariable Long id) {
        medioPagoService.eliminarMedioPago(id);
    }

    @GetMapping("/usuario/{idUsuario}")
    public List<MedioPagoDTO> listarMediosPago(@PathVariable Long idUsuario) {
        return medioPagoService.listarMediosPagoPorUsuario(idUsuario);
    }
} 
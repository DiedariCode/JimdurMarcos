package com.diedari.jimdur.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.diedari.jimdur.model.Ubicaciones;
import com.diedari.jimdur.service.UbicacionService;

@RestController
@RequestMapping("/api/ubicaciones")
public class UbicacionRestController {

    @Autowired
    private UbicacionService ubicacionService;

    // Listar todas las ubicaciones
    @GetMapping
    public List<Ubicaciones> listarUbicaciones() {
        return ubicacionService.listarUbicaciones();
    }

    // Obtener una ubicación por ID
    @GetMapping("/{id}")
    public Ubicaciones obtenerUbicacionPorId(@PathVariable Integer id) {
        return ubicacionService.obtenerUbicacionPorId(id);
    }

    // Guardar nueva ubicación
    @PostMapping
    public Ubicaciones guardarUbicacion(@RequestBody Ubicaciones ubicacion) {
        return ubicacionService.guardarUbicacion(ubicacion);
    }

    // Actualizar ubicación existente
    @PutMapping("/{id}")
    public Ubicaciones actualizarUbicacion(@PathVariable Long id, @RequestBody Ubicaciones ubicacion) {
        ubicacion.setIdUbicacion(id);
        return ubicacionService.actualizarUbicacion(ubicacion);
    }

    // Eliminar ubicación por ID
    @DeleteMapping("/{id}")
    public void eliminarUbicacion(@PathVariable Integer id) {
        ubicacionService.eliminarUbicacion(id);
    }
}

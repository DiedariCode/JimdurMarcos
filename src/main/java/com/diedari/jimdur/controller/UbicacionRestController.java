package com.diedari.jimdur.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.diedari.jimdur.dto.UbicacionDTO;
import com.diedari.jimdur.model.Ubicaciones;
import com.diedari.jimdur.service.UbicacionService;

@RestController
@RequestMapping("/api/ubicaciones")
public class UbicacionRestController {

    @Autowired
    private UbicacionService ubicacionService;

    // Listar todas las ubicaciones como DTOs
    @GetMapping
    public List<UbicacionDTO> listarUbicaciones() {
        return ubicacionService.listarUbicaciones()
                .stream()
                .map(this::convertirAUbicacionDTO)
                .collect(Collectors.toList());
    }

    // Obtener una ubicación por ID como DTO
    @GetMapping("/{id}")
    public UbicacionDTO obtenerUbicacionPorId(@PathVariable Long id) {
        Ubicaciones ubicacion = ubicacionService.obtenerUbicacionPorId(id);
        return convertirAUbicacionDTO(ubicacion);
    }

    // Guardar nueva ubicación
    @PostMapping
    public UbicacionDTO guardarUbicacion(@RequestBody UbicacionDTO ubicacionDTO) {
        Ubicaciones ubicacion = convertirAEntidad(ubicacionDTO);
        Ubicaciones ubicacionGuardada = ubicacionService.guardarUbicacion(ubicacion);
        return convertirAUbicacionDTO(ubicacionGuardada);
    }

    // Actualizar ubicación existente
    @PutMapping("/{id}")
    public UbicacionDTO actualizarUbicacion(@PathVariable Long id, @RequestBody UbicacionDTO ubicacionDTO) {
        Ubicaciones ubicacion = convertirAEntidad(ubicacionDTO);
        ubicacion.setIdUbicacion(id);
        Ubicaciones ubicacionActualizada = ubicacionService.actualizarUbicacion(ubicacion);
        return convertirAUbicacionDTO(ubicacionActualizada);
    }

    // Eliminar ubicación por ID
    @DeleteMapping("/{id}")
    public void eliminarUbicacion(@PathVariable Long id) {
        ubicacionService.eliminarUbicacion(id);
    }

    // Método auxiliar para convertir entidad a DTO
    private UbicacionDTO convertirAUbicacionDTO(Ubicaciones ubicacion) {
        return UbicacionDTO.builder()
            .idUbicacion(ubicacion.getIdUbicacion())
            .nombre(ubicacion.getNombre())
            .descripcion(ubicacion.getDescripcion())
            .codigo(ubicacion.getCodigo())
            .capacidad(ubicacion.getCapacidad())
            .activo(true) // Por defecto activo
            .build();
    }

    // Método auxiliar para convertir DTO a entidad  
    private Ubicaciones convertirAEntidad(UbicacionDTO ubicacionDTO) {
        return Ubicaciones.builder()
            .idUbicacion(ubicacionDTO.getIdUbicacion())
            .nombre(ubicacionDTO.getNombre())
            .descripcion(ubicacionDTO.getDescripcion())
            .codigo(ubicacionDTO.getCodigo())
            .capacidad(ubicacionDTO.getCapacidad())
            .tipoUbicacion(Ubicaciones.TipoUbicacion.ESTANTE) // Valor por defecto
            .build();
    }
}

package com.diedari.jimdur.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.diedari.jimdur.dto.UbicacionDTO;
import com.diedari.jimdur.model.Ubicaciones;
import com.diedari.jimdur.repository.InventarioRepository;
import com.diedari.jimdur.repository.UbicacionRepository;

@Service
public class UbicacionServiceImpl implements UbicacionService {

    @Autowired
    private UbicacionRepository ubicacionRepository;
    
    @Autowired
    private InventarioRepository inventarioRepository;

    @Override
    public List<Ubicaciones> listarUbicaciones() {
        return ubicacionRepository.findAll();
    }

    @Override
    public Ubicaciones obtenerUbicacionPorId(Long idUbicacion) {
        if (idUbicacion == null) return null;
        Optional<Ubicaciones> ubicacion = ubicacionRepository.findById(idUbicacion);
        return ubicacion.orElse(null); // Retorna null si no se encuentra
    }

    @Override
    public Ubicaciones guardarUbicacion(Ubicaciones ubicacion) {
        return ubicacionRepository.save(ubicacion);
    }

    @Override
    public void eliminarUbicacion(Long idUbicacion) {
        if (idUbicacion != null) {
            ubicacionRepository.deleteById(idUbicacion);
        }
    }

    @Override
    public Ubicaciones actualizarUbicacion(Ubicaciones ubicacion) {
        return ubicacionRepository.save(ubicacion); // save() también se usa para actualizar
    }

    // 🎯 NUEVOS MÉTODOS PARA UX MEJORADA

    @Override
    public List<UbicacionDTO> listarUbicacionesConOcupacion() {
        return ubicacionRepository.findAll().stream()
            .map(this::convertirAUbicacionDTOConOcupacion)
            .collect(Collectors.toList());
    }

    @Override
    public UbicacionDTO obtenerUbicacionConOcupacion(Long idUbicacion) {
        Optional<Ubicaciones> ubicacion = ubicacionRepository.findById(idUbicacion);
        return ubicacion.map(this::convertirAUbicacionDTOConOcupacion).orElse(null);
    }

    @Override
    public boolean validarCapacidadDisponible(Long idUbicacion, Integer cantidadAgregar) {
        Integer espacioDisponible = obtenerEspacioDisponible(idUbicacion);
        return espacioDisponible != null && espacioDisponible >= cantidadAgregar;
    }

    @Override
    public Integer obtenerEspacioDisponible(Long idUbicacion) {
        Optional<Ubicaciones> ubicacionOpt = ubicacionRepository.findById(idUbicacion);
        if (ubicacionOpt.isEmpty()) return null;
        
        Ubicaciones ubicacion = ubicacionOpt.get();
        Integer ocupacionActual = inventarioRepository.getTotalStockByUbicacion(idUbicacion);
        ocupacionActual = ocupacionActual != null ? ocupacionActual : 0;
        
        return ubicacion.getCapacidad() - ocupacionActual;
    }

    @Override
    public Double calcularPorcentajeOcupacion(Long idUbicacion) {
        Optional<Ubicaciones> ubicacionOpt = ubicacionRepository.findById(idUbicacion);
        if (ubicacionOpt.isEmpty()) return 0.0;
        
        Ubicaciones ubicacion = ubicacionOpt.get();
        if (ubicacion.getCapacidad() == 0) return 0.0;
        
        Integer ocupacionActual = inventarioRepository.getTotalStockByUbicacion(idUbicacion);
        ocupacionActual = ocupacionActual != null ? ocupacionActual : 0;
        
        return (ocupacionActual.doubleValue() / ubicacion.getCapacidad().doubleValue()) * 100.0;
    }

    // 🔄 MÉTODO AUXILIAR PARA CONVERTIR A DTO CON INFORMACIÓN DE OCUPACIÓN
    private UbicacionDTO convertirAUbicacionDTOConOcupacion(Ubicaciones ubicacion) {
        Integer ocupacionActual = inventarioRepository.getTotalStockByUbicacion(ubicacion.getIdUbicacion());
        ocupacionActual = ocupacionActual != null ? ocupacionActual : 0;
        
        Integer espacioDisponible = ubicacion.getCapacidad() - ocupacionActual;
        Double porcentajeOcupacion = calcularPorcentajeOcupacion(ubicacion.getIdUbicacion());
        
        // Determinar estado visual basado en ocupación
        String estadoOcupacion;
        String colorIndicador;
        
        if (porcentajeOcupacion <= 25) {
            estadoOcupacion = "VACIO";
            colorIndicador = "#28a745"; // Verde
        } else if (porcentajeOcupacion <= 75) {
            estadoOcupacion = "MEDIO";
            colorIndicador = "#ffc107"; // Amarillo
        } else if (porcentajeOcupacion <= 100) {
            estadoOcupacion = "LLENO";
            colorIndicador = "#fd7e14"; // Naranja
        } else {
            estadoOcupacion = "SOBRECARGADO";
            colorIndicador = "#dc3545"; // Rojo
        }

        return UbicacionDTO.builder()
            .idUbicacion(ubicacion.getIdUbicacion())
            .codigo(ubicacion.getCodigo())
            .nombre(ubicacion.getNombre())
            .descripcion(ubicacion.getDescripcion())
            .capacidad(ubicacion.getCapacidad())
            .activo(true)
            // Nuevos campos para UX
            .ocupacionActual(ocupacionActual)
            .espacioDisponible(espacioDisponible)
            .porcentajeOcupacion(porcentajeOcupacion)
            .estadoOcupacion(estadoOcupacion)
            .colorIndicador(colorIndicador)
            .tipoUbicacion(ubicacion.getTipoUbicacion().name())
            .build();
    }
}

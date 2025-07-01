package com.diedari.jimdur.controller;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.diedari.jimdur.dto.MovimientoInventarioDTO;
import com.diedari.jimdur.service.MovimientoInventarioService;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/admin/movimientos")
@RequiredArgsConstructor
@PreAuthorize("hasAuthority('LEER_MOVIMIENTOS')")
public class MovimientoInventarioController {

    private final MovimientoInventarioService movimientoInventarioService;

    @GetMapping
    public String listarMovimientos(
            Model model,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "15") int size,
            @RequestParam(defaultValue = "fechaMovimiento") String sortField,
            @RequestParam(defaultValue = "desc") String sortDirection,
            @RequestParam(required = false) String busqueda,
            @RequestParam(required = false) String tipoBusqueda,
            @RequestParam(required = false) String fechaInicio,
            @RequestParam(required = false) String fechaFin) {

        Pageable pageable = PageRequest.of(page, size,
                Sort.by(sortDirection.equals("asc") ? Sort.Direction.ASC : Sort.Direction.DESC, sortField));

        Page<MovimientoInventarioDTO> movimientos;

        if (busqueda != null && !busqueda.trim().isEmpty()) {
            switch (tipoBusqueda != null ? tipoBusqueda : "nombre") {
                case "sku":
                    movimientos = movimientoInventarioService.buscarMovimientosPorSkuProducto(busqueda, pageable);
                    break;
                default:
                    movimientos = movimientoInventarioService.buscarMovimientosPorNombreProducto(busqueda, pageable);
                    break;
            }
        } else {
            movimientos = movimientoInventarioService.obtenerTodosLosMovimientos(pageable);
        }

        model.addAttribute("movimientos", movimientos);
        model.addAttribute("pageTitle", "Historial de Movimientos");
        
        // Atributos para la paginación
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", movimientos.getTotalPages());
        model.addAttribute("totalItems", movimientos.getTotalElements());
        model.addAttribute("pageSize", size);
        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDirection", sortDirection);
        model.addAttribute("reverseSortDirection", sortDirection.equals("asc") ? "desc" : "asc");
        model.addAttribute("busqueda", busqueda);
        model.addAttribute("tipoBusqueda", tipoBusqueda);
        model.addAttribute("fechaInicio", fechaInicio);
        model.addAttribute("fechaFin", fechaFin);

        return "admin/movimientos/listar";
    }

    @GetMapping("/producto/{idProducto}")
    @ResponseBody
    public ResponseEntity<List<MovimientoInventarioDTO>> obtenerMovimientosPorProducto(@PathVariable Long idProducto) {
        try {
            List<MovimientoInventarioDTO> movimientos = movimientoInventarioService.obtenerMovimientosPorProducto(idProducto);
            return ResponseEntity.ok(movimientos);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/producto/{idProducto}/ultimos")
    @ResponseBody
    public ResponseEntity<List<MovimientoInventarioDTO>> obtenerUltimosMovimientosDeProducto(@PathVariable Long idProducto) {
        try {
            List<MovimientoInventarioDTO> movimientos = movimientoInventarioService.obtenerUltimosMovimientosDeProducto(idProducto);
            return ResponseEntity.ok(movimientos);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/ubicacion/{idUbicacion}")
    @ResponseBody
    public ResponseEntity<List<MovimientoInventarioDTO>> obtenerMovimientosPorUbicacion(@PathVariable Long idUbicacion) {
        try {
            List<MovimientoInventarioDTO> movimientos = movimientoInventarioService.obtenerMovimientosPorUbicacion(idUbicacion);
            return ResponseEntity.ok(movimientos);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/tipo/{tipoMovimiento}")
    @ResponseBody
    public ResponseEntity<List<MovimientoInventarioDTO>> obtenerMovimientosPorTipo(@PathVariable String tipoMovimiento) {
        try {
            List<MovimientoInventarioDTO> movimientos = movimientoInventarioService.obtenerMovimientosPorTipo(tipoMovimiento);
            return ResponseEntity.ok(movimientos);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/reporte")
    @PreAuthorize("hasAuthority('VER_REPORTES_INVENTARIO')")
    public String verReporte(
            Model model,
            @RequestParam(required = false) String fechaInicio,
            @RequestParam(required = false) String fechaFin,
            @RequestParam(required = false) String tipoMovimiento) {

        List<MovimientoInventarioDTO> movimientos;

        try {
            if (fechaInicio != null && fechaFin != null && !fechaInicio.isEmpty() && !fechaFin.isEmpty()) {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                LocalDateTime inicio = LocalDateTime.parse(fechaInicio + "T00:00:00");
                LocalDateTime fin = LocalDateTime.parse(fechaFin + "T23:59:59");
                
                movimientos = movimientoInventarioService.obtenerMovimientosPorFechas(inicio, fin);
            } else {
                // Mostrar movimientos de los últimos 30 días por defecto
                LocalDateTime fin = LocalDateTime.now();
                LocalDateTime inicio = fin.minusDays(30);
                movimientos = movimientoInventarioService.obtenerMovimientosPorFechas(inicio, fin);
            }

            // Filtrar por tipo de movimiento si se especifica
            if (tipoMovimiento != null && !tipoMovimiento.isEmpty() && !tipoMovimiento.equals("TODOS")) {
                movimientos = movimientos.stream()
                        .filter(m -> m.getTipoMovimiento().equals(tipoMovimiento))
                        .toList();
            }

            model.addAttribute("movimientos", movimientos);
            model.addAttribute("fechaInicio", fechaInicio);
            model.addAttribute("fechaFin", fechaFin);
            model.addAttribute("tipoMovimiento", tipoMovimiento);
            model.addAttribute("pageTitle", "Reporte de Movimientos");

            return "admin/movimientos/reporte";
        } catch (Exception e) {
            model.addAttribute("error", "Error al generar el reporte: " + e.getMessage());
            return "admin/movimientos/reporte";
        }
    }
} 
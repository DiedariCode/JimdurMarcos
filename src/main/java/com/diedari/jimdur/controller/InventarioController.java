package com.diedari.jimdur.controller;

import java.security.Principal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.diedari.jimdur.dto.AsignarStockDTO;
import com.diedari.jimdur.dto.InventarioDTO;
import com.diedari.jimdur.dto.MovimientoInventarioDTO;
import com.diedari.jimdur.repository.ProductoRepository;
import com.diedari.jimdur.repository.UbicacionRepository;
import com.diedari.jimdur.repository.UsuarioRepository;
import com.diedari.jimdur.service.InventarioService;
import com.diedari.jimdur.service.MovimientoInventarioService;
import com.diedari.jimdur.service.UbicacionService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/admin/inventario")
@RequiredArgsConstructor
@PreAuthorize("hasAuthority('LEER_INVENTARIO')")
public class InventarioController {

    private final InventarioService inventarioService;
    private final ProductoRepository productoRepository;
    private final UbicacionRepository ubicacionRepository;
    private final UsuarioRepository usuarioRepository;
    private final UbicacionService ubicacionService;
    private final MovimientoInventarioService movimientoInventarioService;

    @GetMapping
    public String listarInventarios(
            Model model,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "idInventario") String sortField,
            @RequestParam(defaultValue = "desc") String sortDirection,
            @RequestParam(required = false) String busqueda,
            @RequestParam(required = false) String tipoBusqueda) {

        Pageable pageable = PageRequest.of(page, size,
                Sort.by(sortDirection.equals("asc") ? Sort.Direction.ASC : Sort.Direction.DESC, sortField));

        Page<InventarioDTO> inventarios;

        if (busqueda != null && !busqueda.trim().isEmpty()) {
            switch (tipoBusqueda != null ? tipoBusqueda : "nombre") {
                case "sku":
                    inventarios = inventarioService.buscarInventariosPorSkuProducto(busqueda, pageable);
                    break;
                case "ubicacion":
                    inventarios = inventarioService.buscarInventariosPorCodigoUbicacion(busqueda, pageable);
                    break;
                default:
                    inventarios = inventarioService.buscarInventariosPorNombreProducto(busqueda, pageable);
                    break;
            }
        } else {
            inventarios = inventarioService.obtenerTodosLosInventarios(pageable);
        }

        model.addAttribute("inventarios", inventarios);
        model.addAttribute("pageTitle", "Gestión de Inventario");
        
        // Atributos para la paginación
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", inventarios.getTotalPages());
        model.addAttribute("totalItems", inventarios.getTotalElements());
        model.addAttribute("pageSize", size);
        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDirection", sortDirection);
        model.addAttribute("reverseSortDirection", sortDirection.equals("asc") ? "desc" : "asc");
        model.addAttribute("busqueda", busqueda);
        model.addAttribute("tipoBusqueda", tipoBusqueda);
        model.addAttribute("claseActiva", "inventario");

        return "admin/inventario/listar";
    }

    @GetMapping("/nuevo")
    @PreAuthorize("hasAuthority('CREAR_INVENTARIO')")
    public String mostrarFormularioNuevo(Model model) {
        InventarioDTO inventario = new InventarioDTO();
        
        model.addAttribute("inventario", inventario);
        model.addAttribute("productos", productoRepository.findByActivo(true));
        model.addAttribute("ubicaciones", ubicacionRepository.findAll());
        model.addAttribute("pageTitle", "Nuevo Inventario");
        model.addAttribute("claseActiva", "inventario");

        return "admin/inventario/nuevo";
    }

    @PostMapping("/guardar")
    @PreAuthorize("hasAuthority('CREAR_INVENTARIO')")
    public String guardarInventario(
            @Valid InventarioDTO inventarioDTO,
            BindingResult result,
            Model model,
            Principal principal,
            RedirectAttributes redirectAttributes) {

        if (result.hasErrors()) {
            model.addAttribute("productos", productoRepository.findByActivo(true));
            model.addAttribute("ubicaciones", ubicacionRepository.findAll());
            model.addAttribute("pageTitle", "Nuevo Inventario");
            return "admin/inventario/nuevo";
        }

        try {
            // Obtener ID del usuario autenticado
            Long idUsuario = usuarioRepository.findByEmail(principal.getName()).get().getId();
            
            // Convertir a AsignarStockDTO para usar el método correspondiente
            AsignarStockDTO asignarStockDTO = AsignarStockDTO.builder()
                    .idProducto(inventarioDTO.getIdProducto())
                    .idUbicacion(inventarioDTO.getIdUbicacion())
                    .cantidad(inventarioDTO.getCantidad())
                    .valorUnitario(inventarioDTO.getValorUnitario())
                    .descripcion("Creación de inventario inicial")
                    .build();

            inventarioService.asignarStock(asignarStockDTO, idUsuario);
            redirectAttributes.addFlashAttribute("success", "Inventario creado exitosamente");
            return "redirect:/admin/inventario";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al crear el inventario: " + e.getMessage());
            return "redirect:/admin/inventario/nuevo";
        }
    }

    @GetMapping("/{id}/editar")
    @PreAuthorize("hasAuthority('EDITAR_INVENTARIO')")
    public String mostrarFormularioEditar(@PathVariable Long id, Model model) {
        try {
            InventarioDTO inventario = inventarioService.obtenerInventarioPorId(id);
            
            model.addAttribute("inventario", inventario);
            model.addAttribute("productos", productoRepository.findByActivo(true));
            model.addAttribute("ubicaciones", ubicacionRepository.findAll());
            model.addAttribute("pageTitle", "Editar Inventario");
            model.addAttribute("claseActiva", "inventario");

            return "admin/inventario/editar";
        } catch (Exception e) {
            return "redirect:/admin/inventario?error=not_found";
        }
    }

    @PostMapping("/{id}/actualizar")
    @PreAuthorize("hasAuthority('EDITAR_INVENTARIO')")
    public String actualizarInventario(
            @PathVariable Long id,
            @Valid InventarioDTO inventarioDTO,
            BindingResult result,
            Model model,
            Principal principal,
            RedirectAttributes redirectAttributes) {

        if (result.hasErrors()) {
            model.addAttribute("productos", productoRepository.findByActivo(true));
            model.addAttribute("ubicaciones", ubicacionRepository.findAll());
            model.addAttribute("pageTitle", "Editar Inventario");
            return "admin/inventario/editar";
        }

        try {
            // Obtener ID del usuario autenticado
            Long idUsuario = usuarioRepository.findByEmail(principal.getName()).get().getId();
            
            inventarioDTO.setIdInventario(id);
            inventarioService.actualizarInventario(inventarioDTO, idUsuario);
            redirectAttributes.addFlashAttribute("success", "Inventario actualizado exitosamente");
            return "redirect:/admin/inventario";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al actualizar el inventario: " + e.getMessage());
            return "redirect:/admin/inventario/" + id + "/editar";
        }
    }

    @PostMapping("/{id}/eliminar")
    @PreAuthorize("hasAuthority('ELIMINAR_INVENTARIO')")
    public String eliminarInventario(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            inventarioService.eliminarInventario(id);
            redirectAttributes.addFlashAttribute("success", "Inventario eliminado exitosamente");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al eliminar el inventario: " + e.getMessage());
        }
        return "redirect:/admin/inventario";
    }

    @PostMapping("/asignar-stock")
    @PreAuthorize("hasAuthority('ASIGNAR_STOCK')")
    @ResponseBody
    public ResponseEntity<?> asignarStock(
            @RequestBody @Valid AsignarStockDTO asignarStockDTO,
            Principal principal) {
        try {
            // Obtener ID del usuario autenticado
            Long idUsuario = usuarioRepository.findByEmail(principal.getName()).get().getId();
            
            InventarioDTO inventario = inventarioService.asignarStock(asignarStockDTO, idUsuario);
            return ResponseEntity.ok(inventario);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error al asignar stock: " + e.getMessage());
        }
    }

    @PostMapping("/actualizar-stock")
    @PreAuthorize("hasAuthority('EDITAR_INVENTARIO') or hasAuthority('ASIGNAR_STOCK')")
    @ResponseBody
    public ResponseEntity<?> actualizarStock(
            @RequestBody AsignarStockDTO stockDTO,
            Principal principal) {
        
        try {
            Long idUsuario = usuarioRepository.findByEmail(principal.getName()).get().getId();
            
            // Validar que tenga tipo de operación para actualizaciones
            if (stockDTO.getTipoOperacion() == null || stockDTO.getTipoOperacion().isEmpty()) {
                return ResponseEntity.badRequest().body("Error: Tipo de operación requerido para actualizar stock");
            }
            
            // Obtener inventario existente
            List<InventarioDTO> inventariosExistentes = inventarioService.obtenerInventariosPorProducto(stockDTO.getIdProducto());
            InventarioDTO inventarioExistente = inventariosExistentes.stream()
                .filter(inv -> inv.getIdUbicacion().equals(stockDTO.getIdUbicacion()))
                .findFirst()
                .orElse(null);
            
            if (inventarioExistente == null) {
                return ResponseEntity.badRequest().body("Error: No existe inventario para este producto en la ubicación especificada");
            }
            
            // Procesar según tipo de operación
            InventarioDTO resultado = null;
            String tipoOp = stockDTO.getTipoOperacion().toUpperCase();
            
                         // Validar que no se reduzca más stock del disponible para SALIDA
            if (tipoOp.equals("SALIDA") && stockDTO.getCantidad() > inventarioExistente.getCantidad()) {
                return ResponseEntity.badRequest().body(
                    String.format("Error: No se puede reducir %d unidades. Stock disponible: %d", 
                        stockDTO.getCantidad(), inventarioExistente.getCantidad()));
            }
            
            // Establecer descripción por defecto si no se proporciona
            if (stockDTO.getDescripcion() == null || stockDTO.getDescripcion().trim().isEmpty()) {
                switch (tipoOp) {
                    case "ENTRADA":
                        stockDTO.setDescripcion("Entrada de stock");
                        break;
                    case "SALIDA":
                        stockDTO.setDescripcion("Salida de stock");
                        break;
                    case "AJUSTE":
                        stockDTO.setDescripcion("Ajuste de inventario");
                        break;
                }
            }
            
            // Establecer tipo de operación y procesar
            stockDTO.setTipoOperacion(tipoOp);
            resultado = inventarioService.asignarStock(stockDTO, idUsuario);
            
            return ResponseEntity.ok(resultado);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error al actualizar stock: " + e.getMessage());
        }
    }

    @GetMapping("/producto/{idProducto}")
    @ResponseBody
    public ResponseEntity<List<InventarioDTO>> obtenerInventarioPorProducto(@PathVariable Long idProducto) {
        try {
            List<InventarioDTO> inventarios = inventarioService.obtenerInventariosPorProducto(idProducto);
            return ResponseEntity.ok(inventarios);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/stock-bajo")
    public String verStockBajo(
            Model model, 
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) Long ubicacion,
            @RequestParam(required = false) String estado) {
        
        // Obtener inventarios con stock bajo (menos de 5 unidades)
        List<InventarioDTO> todosInventariosBajos = inventarioService.obtenerInventariosConStockBajo(5);
        
        // Calcular estadísticas
        long stockCritico = todosInventariosBajos.stream()
                .filter(inv -> inv.getCantidad() == 0)
                .count();
        long stockBajo = todosInventariosBajos.stream()
                .filter(inv -> inv.getCantidad() > 0 && inv.getCantidad() <= 5)
                .count();
        double valorEnRiesgo = todosInventariosBajos.stream()
                .mapToDouble(InventarioDTO::getValorTotal)
                .sum();
        
        // Paginación manual de la lista
        int fromIndex = page * size;
        int toIndex = Math.min(fromIndex + size, todosInventariosBajos.size());
        List<InventarioDTO> inventariosPaginados = todosInventariosBajos.subList(fromIndex, toIndex);
        int totalPages = (int) Math.ceil((double) todosInventariosBajos.size() / size);
        
        model.addAttribute("inventarios", inventariosPaginados);
        model.addAttribute("stockCritico", stockCritico);
        model.addAttribute("stockBajo", stockBajo);
        model.addAttribute("totalAfectados", stockCritico + stockBajo);
        model.addAttribute("valorEnRiesgo", valorEnRiesgo);
        model.addAttribute("ubicaciones", ubicacionRepository.findAll());
        model.addAttribute("pageTitle", "Productos con Stock Bajo");
        model.addAttribute("claseActiva", "stock-bajo");
        
        // Atributos para la paginación
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("totalItems", todosInventariosBajos.size());

        return "admin/inventario/stock-bajo";
    }

    @GetMapping("/ubicacion/{idUbicacion}")
    @ResponseBody
    public ResponseEntity<List<InventarioDTO>> obtenerInventarioPorUbicacion(@PathVariable Long idUbicacion) {
        try {
            List<InventarioDTO> inventarios = inventarioService.obtenerInventariosPorUbicacion(idUbicacion);
            return ResponseEntity.ok(inventarios);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/producto/{idProducto}/movimientos")
    @ResponseBody
    public ResponseEntity<List<MovimientoInventarioDTO>> obtenerMovimientosPorProducto(@PathVariable Long idProducto) {
        try {
            List<MovimientoInventarioDTO> movimientos = movimientoInventarioService.obtenerMovimientosPorProducto(idProducto);
            return ResponseEntity.ok(movimientos);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/producto/{idProducto}/estadisticas")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> obtenerEstadisticasProducto(@PathVariable Long idProducto) {
        try {
            Map<String, Object> estadisticas = new HashMap<>();
            
            // Obtener todos los movimientos del producto
            List<MovimientoInventarioDTO> todosMovimientos = movimientoInventarioService.obtenerMovimientosPorProducto(idProducto);
            
            // Contar movimientos del mes actual (simplificado)
            long movimientosRecientes = todosMovimientos.stream()
                .filter(m -> m.getFechaMovimiento().isAfter(java.time.LocalDateTime.now().minusDays(30)))
                .count();
            estadisticas.put("movimientosMes", (int) movimientosRecientes);
            
            // Contar por tipo de movimiento
            long totalEntradas = todosMovimientos.stream()
                .filter(m -> "ENTRADA".equals(m.getTipoMovimiento()))
                .count();
            long totalSalidas = todosMovimientos.stream()
                .filter(m -> "SALIDA".equals(m.getTipoMovimiento()))
                .count();
            
            estadisticas.put("totalEntradas", (int) totalEntradas);
            estadisticas.put("totalSalidas", (int) totalSalidas);
            
            return ResponseEntity.ok(estadisticas);
        } catch (Exception e) {
            Map<String, Object> estadisticasVacias = new HashMap<>();
            estadisticasVacias.put("movimientosMes", 0);
            estadisticasVacias.put("totalEntradas", 0);
            estadisticasVacias.put("totalSalidas", 0);
            return ResponseEntity.ok(estadisticasVacias);
        }
    }

    @GetMapping("/reporte")
    @PreAuthorize("hasAuthority('LEER_INVENTARIO')")
    public String mostrarReporteInventario(Model model) {
        try {
            // Obtener todos los inventarios usando paginación
            Pageable pageable = PageRequest.of(0, Integer.MAX_VALUE);
            Page<InventarioDTO> pageInventarios = inventarioService.obtenerTodosLosInventarios(pageable);
            List<InventarioDTO> todosInventarios = pageInventarios.getContent();
            
            // Log para debugging
            System.out.println("=== REPORTE DE INVENTARIO ===");
            System.out.println("Total inventarios encontrados: " + todosInventarios.size());
            
            // Calcular estadísticas generales
            int totalProductos = todosInventarios.size();
            int totalUnidades = todosInventarios.stream()
                .mapToInt(InventarioDTO::getCantidad)
                .sum();
            double valorTotalInventario = todosInventarios.stream()
                .mapToDouble(InventarioDTO::getValorTotal)
                .sum();
            
            // Productos con stock bajo
            List<InventarioDTO> stockBajo = inventarioService.obtenerInventariosConStockBajo(5);
            int productosStockBajo = stockBajo.size();
            
            // Productos sin stock
            long productosSinStock = todosInventarios.stream()
                .filter(inv -> inv.getCantidad() == 0)
                .count();
            
            // Ubicaciones más utilizadas
            Map<String, Long> ubicacionesUsadas = todosInventarios.stream()
                .collect(java.util.stream.Collectors.groupingBy(
                    InventarioDTO::getNombreUbicacion,
                    java.util.stream.Collectors.counting()
                ));
            
            // Productos más valiosos
            List<InventarioDTO> productosMasValiosos = todosInventarios.stream()
                .sorted((a, b) -> Double.compare(b.getValorTotal(), a.getValorTotal()))
                .limit(10)
                .collect(java.util.stream.Collectors.toList());
            
            // Log de estadísticas para debugging
            System.out.println("Estadísticas calculadas:");
            System.out.println("- Total productos: " + totalProductos);
            System.out.println("- Productos stock bajo: " + productosStockBajo);
            System.out.println("- Productos sin stock: " + productosSinStock);
            System.out.println("- Stock normal: " + (totalProductos - productosStockBajo - (int)productosSinStock));
            System.out.println("- Ubicaciones utilizadas: " + ubicacionesUsadas.size());
            System.out.println("================================");
            
            // Agregar datos al modelo
            model.addAttribute("totalProductos", totalProductos);
            model.addAttribute("totalUnidades", totalUnidades);
            model.addAttribute("valorTotalInventario", valorTotalInventario);
            model.addAttribute("productosStockBajo", productosStockBajo);
            model.addAttribute("productosSinStock", (int)productosSinStock);
            model.addAttribute("ubicacionesUsadas", ubicacionesUsadas);
            model.addAttribute("productosMasValiosos", productosMasValiosos);
            model.addAttribute("pageTitle", "Reporte de Inventario");
            model.addAttribute("claseActiva", "reporte-inventario");
            
            return "admin/inventario/reporte";
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("error", "Error al generar el reporte: " + e.getMessage());
            model.addAttribute("totalProductos", 0);
            model.addAttribute("totalUnidades", 0);
            model.addAttribute("valorTotalInventario", 0.0);
            model.addAttribute("productosStockBajo", 0);
            model.addAttribute("productosSinStock", 0);
            model.addAttribute("ubicacionesUsadas", new HashMap<>());
            model.addAttribute("productosMasValiosos", new ArrayList<>());
            model.addAttribute("pageTitle", "Reporte de Inventario");
            model.addAttribute("claseActiva", "reporte-inventario");
            return "admin/inventario/reporte";
        }
    }
} 
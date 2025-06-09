package com.diedari.jimdur.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.diedari.jimdur.dto.ProductoDTO;
import com.diedari.jimdur.dto.ProductoProveedorDTO;
import com.diedari.jimdur.model.Categoria;
import com.diedari.jimdur.model.Marca;
import com.diedari.jimdur.model.Proveedor;
import com.diedari.jimdur.repository.CategoriaRepository;
import com.diedari.jimdur.repository.MarcaRepository;
import com.diedari.jimdur.repository.ProveedorRepository;
import com.diedari.jimdur.service.ProductoService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/productos")
@RequiredArgsConstructor
public class ProductoRestController {

    private final ProductoService productoService;
    private final CategoriaRepository categoriaRepository;
    private final MarcaRepository marcaRepository;
    private final ProveedorRepository proveedorRepository;

    @PostMapping("/crear")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> crearProducto(@Valid @ModelAttribute("producto") ProductoDTO producto,
            BindingResult result) {
        Map<String, Object> response = new HashMap<>();

        // Validar SKU único
        if (productoService.existeSkuProducto(producto.getSku(), null)) {
            response.put("success", false);
            response.put("message", "El SKU ya existe");
            return ResponseEntity.ok(response);
        }

        // if (result.hasErrors()) {
        //     response.put("success", false);
        //     response.put("message", "Error en la validación del formulario");
        //     return ResponseEntity.ok(response);
        // }

        if (result.hasErrors()) {
            List<String> errores = result.getFieldErrors().stream() // Convierte el objeto BindingResult en una lista de errores.
                .map(error -> error.getDefaultMessage()) // Toma cada error y extraer su mensaje por defecto (el q se puso en el DTO).
                .collect(Collectors.toList()); // Convierte la lista de errores en una lista de strings.
        
            response.put("success", false);
            response.put("message", "Errores de validación");
            response.put("errors", errores); // lista de errores específicos
        
            return ResponseEntity.ok(response);
        }

        try {
            // Limpiar listas que no se usarán en esta fase
            producto.setProveedores(new ArrayList<>());

            ProductoDTO productoGuardado = productoService.guardarProducto(producto);

            response.put("success", true);
            response.put("message", "Producto creado exitosamente");
            response.put("idProducto", productoGuardado.getIdProducto());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error al guardar el producto: " + e.getMessage());
            return ResponseEntity.ok(response);
        }
    }

    @PostMapping("/guardar-proveedores")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> guardarProveedores(@RequestBody Map<String, Object> request) {
        Map<String, Object> response = new HashMap<>();

        try {
            Long idProducto = Long.valueOf(request.get("idProducto").toString());
            List<Map<String, Object>> proveedoresData = (List<Map<String, Object>>) request.get("proveedores");

            List<ProductoProveedorDTO> proveedores = proveedoresData.stream()
                    .map(data -> ProductoProveedorDTO.builder()
                            .idProducto(idProducto)
                            .idProveedor(Long.valueOf(data.get("idProveedor").toString()))
                            .precioCompra(Double.valueOf(data.get("precioCompra").toString()))
                            .build())
                    .collect(Collectors.toList());

            System.out.println(">>> Llamando a productoService.guardarProveedoresProducto con ID: " + idProducto);
            productoService.guardarProveedoresProducto(idProducto, proveedores);
            System.out.println(">>> Llamada completada");

            response.put("success", true);
            response.put("message", "Proveedores guardados exitosamente");
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error al guardar los proveedores: " + e.getMessage());
        }

        return ResponseEntity.ok(response);
    }

    @PostMapping("/editar")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> editarProducto(@Valid @ModelAttribute ProductoDTO producto,
            BindingResult result) {
        Map<String, Object> response = new HashMap<>();

        // Validar SKU único
        if (productoService.existeSkuProducto(producto.getSku(), producto.getIdProducto())) {
            response.put("success", false);
            response.put("message", "El SKU ya existe");
            return ResponseEntity.ok(response);
        }

        // Validar que haya al menos un proveedor
        if (producto.getProveedores() == null || producto.getProveedores().isEmpty()) {
            response.put("success", false);
            response.put("message", "Debe agregar al menos un proveedor");
            return ResponseEntity.ok(response);
        }

        // Validar que haya al menos una especificación
        if (producto.getEspecificaciones() == null || producto.getEspecificaciones().isEmpty()) {
            response.put("success", false);
            response.put("message", "Debe agregar al menos una especificación");
            return ResponseEntity.ok(response);
        }

        // Validar que haya al menos una compatibilidad
        if (producto.getCompatibilidades() == null || producto.getCompatibilidades().isEmpty()) {
            response.put("success", false);
            response.put("message", "Debe agregar al menos una compatibilidad");
            return ResponseEntity.ok(response);
        }

        if (result.hasErrors()) {
            response.put("success", false);
            response.put("message", "Error en la validación del formulario");
            return ResponseEntity.ok(response);
        }

        try {
            // Limpiar elementos vacíos antes de guardar
            if (producto.getProveedores() != null) {
                producto.getProveedores().removeIf(p -> p.getIdProveedor() == null || p.getPrecioCompra() == null);
            }
            if (producto.getEspecificaciones() != null) {
                producto.getEspecificaciones().removeIf(e -> e.getNombre() == null || e.getNombre().trim().isEmpty());
            }
            if (producto.getCompatibilidades() != null) {
                producto.getCompatibilidades()
                        .removeIf(c -> c.getModeloCompatible() == null || c.getModeloCompatible().trim().isEmpty());
            }

            // Actualizar el producto
            ProductoDTO productoActualizado = productoService.actualizarProducto(producto);

            response.put("success", true);
            response.put("message", "Producto actualizado exitosamente");
            response.put("idProducto", productoActualizado.getIdProducto());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error al actualizar el producto: " + e.getMessage());
            return ResponseEntity.ok(response);
        }
    }

    // API REST para AJAX
    @PostMapping("/rest/validar-sku")
    @ResponseBody
    public ResponseEntity<Map<String, Boolean>> validarSku(@RequestBody Map<String, Object> request) {
        String sku = (String) request.get("sku");
        Long idProducto = request.get("idProducto") != null ? Long.valueOf(request.get("idProducto").toString()) : null;

        boolean existe = productoService.existeSkuProducto(sku, idProducto);

        Map<String, Boolean> response = new HashMap<>();
        response.put("existe", existe);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/rest/calcular-precio-oferta")
    @ResponseBody
    public ResponseEntity<Map<String, Double>> calcularPrecioOferta(@RequestBody Map<String, Double> request) {
        Double precio = request.get("precio");
        Double descuento = request.get("descuento");

        Map<String, Double> response = new HashMap<>();

        if (precio != null && descuento != null && descuento > 0) {
            double precioOferta = precio - (precio * descuento / 100);
            response.put("precioOferta", Math.round(precioOferta * 100.0) / 100.0);
        } else {
            response.put("precioOferta", null);
        }

        return ResponseEntity.ok(response);
    }

    @GetMapping("/rest/datos-formulario")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> obtenerDatosFormulario() {
        Map<String, Object> datos = new HashMap<>();

        List<Categoria> categorias = categoriaRepository.findByEstadoActivaTrueOrderByNombreCategoriaAsc();
        List<Marca> marcas = marcaRepository.findByEstadoMarcaTrueOrderByNombreMarcaAsc();

        // Obtener y filtrar proveedores
        List<Proveedor> todosLosProveedores = proveedorRepository.findAll();
        System.out.println("\n=== DIAGNÓSTICO DE PROVEEDORES ===");
        System.out.println("Total de proveedores encontrados: " + todosLosProveedores.size());

        // Filtrar y mapear a DTO simplificado
        List<Map<String, Object>> proveedoresDTO = todosLosProveedores.stream()
                .filter(p -> "Activo".equals(p.getEstadoActivo()))
                .map(p -> {
                    Map<String, Object> dto = new HashMap<>();
                    dto.put("idProveedor", p.getIdProveedor());
                    dto.put("nombre", p.getNombre());
                    return dto;
                })
                .collect(Collectors.toList());

        System.out.println("\n=== DATOS A ENVIAR ===");
        System.out.println("Número de proveedores en datos: " + proveedoresDTO.size());
        proveedoresDTO.forEach(p -> System.out.println("Proveedor a enviar: " + p.get("nombre")));

        datos.put("categorias", categorias);
        datos.put("marcas", marcas);
        datos.put("proveedores", proveedoresDTO);

        return ResponseEntity.ok(datos);
    }

    @PostMapping("/eliminar-imagen/{id}")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> eliminarImagen(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();
        try {
            productoService.eliminarImagen(id);
            response.put("success", true);
            response.put("message", "Imagen eliminada exitosamente");
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error al eliminar la imagen: " + e.getMessage());
        }
        return ResponseEntity.ok(response);
    }

    @PostMapping("/actualizar-portada/{id}")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> actualizarPortada(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();
        try {
            productoService.actualizarPortada(id);
            response.put("success", true);
            response.put("message", "Portada actualizada exitosamente");
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error al actualizar la portada: " + e.getMessage());
        }
        return ResponseEntity.ok(response);
    }
}

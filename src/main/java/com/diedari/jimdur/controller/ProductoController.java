package com.diedari.jimdur.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.diedari.jimdur.dto.CompatibilidadProductoDTO;
import com.diedari.jimdur.dto.EspecificacionProductoDTO;
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

@Controller
@RequestMapping("/admin/productos")
@RequiredArgsConstructor
public class ProductoController {

    private final ProductoService productoService;
    private final CategoriaRepository categoriaRepository;
    private final MarcaRepository marcaRepository;
    private final ProveedorRepository proveedorRepository;

    @GetMapping
    public String listarProductos(Model model) {
        List<ProductoDTO> productos = productoService.obtenerTodosLosProductos();
        List<Categoria> categorias = categoriaRepository.findByEstadoActiva(true);
        List<Marca> marcas = marcaRepository.findByEstadoMarca(true);
        
        model.addAttribute("productos", productos);
        model.addAttribute("categorias", categorias);
        model.addAttribute("marcas", marcas);
        model.addAttribute("pageTitle", "Gestión de Productos");
        return "admin/productos/listar";
    }

    @GetMapping("/nuevo")
    public String mostrarFormularioNuevo(Model model) {
        ProductoDTO producto = new ProductoDTO();
        producto.setActivo(true); // Por defecto activo
        
        // Inicializar listas vacías
        producto.setProveedores(new ArrayList<>());
        producto.getProveedores().add(new ProductoProveedorDTO());
        
        producto.setEspecificaciones(new ArrayList<>());
        producto.getEspecificaciones().add(new EspecificacionProductoDTO());
        
        producto.setCompatibilidades(new ArrayList<>());
        producto.getCompatibilidades().add(new CompatibilidadProductoDTO());

        cargarDatosFormulario(model, producto);
        return "admin/productos/nuevo";
    }

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

        if (result.hasErrors()) {
            response.put("success", false);
            response.put("message", "Error en la validación del formulario");
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

            productoService.guardarProveedoresProducto(idProducto, proveedores);
            
            response.put("success", true);
            response.put("message", "Proveedores guardados exitosamente");
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error al guardar los proveedores: " + e.getMessage());
        }
        
        return ResponseEntity.ok(response);
    }

    @PostMapping("/editar")
    public String editarProducto(@Valid @ModelAttribute("producto") ProductoDTO producto,
            BindingResult result,
            Model model,
            RedirectAttributes redirectAttributes) {

        // Validar SKU único
        if (productoService.existeSkuProducto(producto.getSku(), producto.getIdProducto())) {
            result.rejectValue("sku", "error.producto", "El SKU ya existe");
        }

        if (result.hasErrors()) {
            cargarDatosFormulario(model, producto);
            return "admin/productos/editar";
        }

        try {
            // Limpiar listas vacías
            if (producto.getProveedores() != null) {
                producto.getProveedores().removeIf(p -> p.getIdProveedor() == null);
            }
            if (producto.getEspecificaciones() != null) {
                producto.getEspecificaciones().removeIf(e -> e.getNombre() == null || e.getNombre().trim().isEmpty());
            }
            if (producto.getCompatibilidades() != null) {
                producto.getCompatibilidades().removeIf(c -> c.getModeloCompatible() == null || c.getModeloCompatible().trim().isEmpty());
            }

            productoService.actualizarProducto(producto);
            redirectAttributes.addFlashAttribute("success", "Producto actualizado exitosamente");
            return "redirect:/admin/productos";
        } catch (Exception e) {
            cargarDatosFormulario(model, producto);
            model.addAttribute("error", "Error al actualizar el producto: " + e.getMessage());
            return "admin/productos/editar";
        }
    }

    @GetMapping("/{id}/editar")
    public String mostrarFormularioEditar(@PathVariable Long id, Model model) {
        try {
            ProductoDTO producto = productoService.obtenerProductoPorId(id);
            cargarDatosFormulario(model, producto);
            return "admin/productos/nuevo";
        } catch (RuntimeException e) {
            return "redirect:/admin/productos?error=not_found";
        }
    }

    @PostMapping("/{id}/eliminar")
    public String eliminarProducto(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            productoService.eliminarProducto(id);
            redirectAttributes.addFlashAttribute("success", "Producto eliminado exitosamente");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al eliminar el producto: " + e.getMessage());
        }
        return "redirect:/admin/productos";
    }

    @GetMapping("/{id}/detalle")
    public String verDetalle(@PathVariable Long id, Model model) {
        try {
            ProductoDTO producto = productoService.obtenerProductoPorId(id);
            model.addAttribute("producto", producto);
            return "admin/productos/detalle";
        } catch (RuntimeException e) {
            return "redirect:/admin/productos?error=not_found";
        }
    }

    private void cargarDatosFormulario(Model model, ProductoDTO producto) {
        List<Categoria> categorias = categoriaRepository.findByEstadoActiva(true);
        List<Marca> marcas = marcaRepository.findByEstadoMarca(true);
        List<Proveedor> proveedores = proveedorRepository.findAll();

        model.addAttribute("producto", producto);
        model.addAttribute("categorias", categorias);
        model.addAttribute("marcas", marcas);
        model.addAttribute("proveedores", proveedores);

        String pageTitle = producto.getIdProducto() != null ? "Editar Producto" : "Nuevo Producto";
        model.addAttribute("pageTitle", pageTitle);
    }

    // API REST para AJAX
    @PostMapping("/api/validar-sku")
    @ResponseBody
    public ResponseEntity<Map<String, Boolean>> validarSku(@RequestBody Map<String, Object> request) {
        String sku = (String) request.get("sku");
        Long idProducto = request.get("idProducto") != null ? Long.valueOf(request.get("idProducto").toString()) : null;

        boolean existe = productoService.existeSkuProducto(sku, idProducto);

        Map<String, Boolean> response = new HashMap<>();
        response.put("existe", existe);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/api/calcular-precio-oferta")
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

    @GetMapping("/api/datos-formulario")
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
                .filter(p -> "1".equals(p.getEstadoActivo()))
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
}
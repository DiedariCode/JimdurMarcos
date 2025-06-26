package com.diedari.jimdur.controller;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.diedari.jimdur.model.Categoria;
import com.diedari.jimdur.model.Permiso;
import com.diedari.jimdur.model.Rol;
import com.diedari.jimdur.model.Usuario;
import com.diedari.jimdur.dto.ProductoDTO;
import com.diedari.jimdur.repository.UsuarioRepository;
import com.diedari.jimdur.service.CategoriaService;
import com.diedari.jimdur.service.MarcaService;
import com.diedari.jimdur.service.ProductoService;

@Controller
@RequestMapping("/")
public class HomeController {

    private static final Logger logger = LoggerFactory.getLogger(HomeController.class);

    @Autowired
    private ProductoService productoService;

    @Autowired
    private CategoriaService categoriaService;

    @Autowired
    private MarcaService marcaService;
    
    @Autowired
    private UsuarioRepository usuarioRepository;

    @GetMapping
    public String index(Model model) {
        List<Categoria> categorias = categoriaService.obtenerCategoriaPorEstado(true);
        model.addAttribute("categorias", categorias);

        // NAVBAR DINAMICA:
        model.addAttribute("activePage", "inicio");

        return "index";
    }

    @GetMapping("/nosotros")
    public String nosotrosForm(Model model) {

        // NAVBAR DINAMICA:
        model.addAttribute("activePage", "nosotros");
        return "/user/nosotros";
    }

    @GetMapping("/productos")
    public String productosForm(Model model) {
        List<ProductoDTO> productos = productoService.obtenerTodosLosProductos();
        
        model.addAttribute("productos", productos);

        model.addAttribute("categorias", categoriaService.obtenerCategoriaPorEstado(true));

        model.addAttribute("marcas", marcaService.obtenerMarcasPorEstado(true));

        // NAVBAR DINAMICA:
        model.addAttribute("activePage", "productos");

        return "/user/productos";
    }

    @GetMapping("/contacto")
    public String contactoForm(Model model) {
        String mapUrl = "https://www.google.com/maps/embed?pb=!1m18!1m12!1m3!1d3973.6374988954863!2d-80.65618082542369!3d-5.161883952143082!2m3!1f0!2f0!3f0!3m2!1i1024!2i768!4f13.1!3m3!1m2!1s0x904a1b88948eae13%3A0x1d2875411b61bb19!2sGrupo%20Jimdur%20%26%20Motorepuestos%20Jimenez!5e0!3m2!1ses!2spe!4v1730094624124!5m2!1ses!2spe";
        model.addAttribute("mapUrl", mapUrl);

        // NAVBAR DINAMICA:
        model.addAttribute("activePage", "contacto");

        return "/user/contacto";
    }

    @GetMapping("/producto/{slug}")
    public String detalleProductoForm(@PathVariable String slug, Model model) {
        ProductoDTO producto = productoService.obtenerProductoPorId(productoService.obtenerProductoPorSlug(slug).getIdProducto());
        if (producto == null) {
            return "redirect:/productos";
        }
        model.addAttribute("producto", producto);
        return "/user/detalle-producto";
    }

    @GetMapping("/acceso-denegado")
    public String accesoDenegado(Model model) {
        logger.warn("Un usuario intentó acceder a una página sin los permisos necesarios.");
        model.addAttribute("titulo", "Acceso Denegado");
        return "acceso-denegado";
    }

    @GetMapping("/debug/permisos")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> debugPermisos(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(401).body(Map.of("error", "No autenticado"));
        }
        
        Map<String, Object> info = new HashMap<>();
        info.put("usuario", authentication.getName());
        info.put("authorities", authentication.getAuthorities().stream()
            .map(auth -> auth.getAuthority())
            .collect(Collectors.toList()));
        info.put("roles", authentication.getAuthorities().stream()
            .map(auth -> auth.getAuthority())
            .filter(auth -> auth.startsWith("ROLE_"))
            .collect(Collectors.toList()));
        info.put("permisos", authentication.getAuthorities().stream()
            .map(auth -> auth.getAuthority())
            .filter(auth -> !auth.startsWith("ROLE_"))
            .collect(Collectors.toList()));
        
        return ResponseEntity.ok(info);
    }

    @GetMapping("/admin/debug/usuario-actual")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> debugUsuarioActual(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(401).body(Map.of("error", "No autenticado"));
        }
        
        // Obtener usuario actualizado de la base de datos
        Optional<Usuario> usuarioOpt = usuarioRepository.findByEmail(authentication.getName());
        if (!usuarioOpt.isPresent()) {
            return ResponseEntity.status(404).body(Map.of("error", "Usuario no encontrado"));
        }
        
        Usuario usuario = usuarioOpt.get();
        Map<String, Object> info = new HashMap<>();
        info.put("emailActual", authentication.getName());
        info.put("permisosEnSesion", authentication.getAuthorities().stream()
            .map(auth -> auth.getAuthority())
            .filter(auth -> !auth.startsWith("ROLE_"))
            .sorted()
            .collect(Collectors.toList()));
        
        // Obtener permisos actuales de la base de datos
        Set<String> permisosDB = new HashSet<>();
        for (Rol rol : usuario.getRoles()) {
            for (Permiso permiso : rol.getPermisos()) {
                permisosDB.add(permiso.getNombre());
            }
        }
        info.put("permisosEnBaseDatos", permisosDB.stream().sorted().collect(Collectors.toList()));
        
        return ResponseEntity.ok(info);
    }
}

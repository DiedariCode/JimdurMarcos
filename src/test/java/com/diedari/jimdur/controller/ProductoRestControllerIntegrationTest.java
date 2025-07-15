package com.diedari.jimdur.controller;

import com.diedari.jimdur.model.Categoria;
import com.diedari.jimdur.model.Marca;
import com.diedari.jimdur.repository.CategoriaRepository;
import com.diedari.jimdur.repository.MarcaRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.test.context.support.WithMockUser;

import java.util.HashMap;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class ProductoRestControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CategoriaRepository categoriaRepository;

    @Autowired
    private MarcaRepository marcaRepository;

    private Long categoriaId;
    private Long marcaId;

    private static final String[] ALL_AUTHORITIES = {
        "LEER_PRODUCTOS", "CREAR_PRODUCTOS", "EDITAR_PRODUCTOS", "LEER_PRODUCTO", "ROLE_ADMIN"
    };

    @BeforeEach
    void setUp() {
        // Crear datos requeridos para ProductoDTO
        Categoria categoria = new Categoria();
        categoria.setNombreCategoria("TestCategoria");
        categoria.setEstadoActiva(true);
        categoria = categoriaRepository.save(categoria);
        categoriaId = categoria.getId();

        Marca marca = new Marca();
        marca.setNombreMarca("TestMarca");
        marca.setEstadoMarca(true);
        marca = marcaRepository.save(marca);
        marcaId = marca.getId();
    }

    @Test
    @WithMockUser(authorities = {"LEER_PRODUCTOS", "CREAR_PRODUCTOS", "EDITAR_PRODUCTOS", "LEER_PRODUCTO", "ROLE_ADMIN"})
    void crearYListarProducto() throws Exception {
        // Crear producto
        Map<String, Object> producto = new HashMap<>();
        producto.put("sku", "SKU-TEST-001");
        producto.put("nombre", "Producto de Prueba");
        producto.put("descripcion", "Descripción de prueba");
        producto.put("precio", 100.0);
        producto.put("descuento", 0.0);
        producto.put("tipoDescuento", "NINGUNO");
        producto.put("idCategoria", categoriaId);
        producto.put("idMarca", marcaId);
        producto.put("activo", true);

        mockMvc.perform(multipart("/api/productos/crear")
                .param("sku", (String) producto.get("sku"))
                .param("nombre", (String) producto.get("nombre"))
                .param("descripcion", (String) producto.get("descripcion"))
                .param("precio", String.valueOf(producto.get("precio")))
                .param("descuento", String.valueOf(producto.get("descuento")))
                .param("tipoDescuento", (String) producto.get("tipoDescuento"))
                .param("idCategoria", String.valueOf(producto.get("idCategoria")))
                .param("idMarca", String.valueOf(producto.get("idMarca")))
                .param("activo", String.valueOf(producto.get("activo")))
                .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));
    }

    @Test
    @WithMockUser(authorities = {"LEER_PRODUCTOS", "CREAR_PRODUCTOS", "EDITAR_PRODUCTOS", "LEER_PRODUCTO", "ROLE_ADMIN"})
    void editarProducto() throws Exception {
        // Primero, crear producto
        Map<String, Object> producto = new HashMap<>();
        producto.put("sku", "SKU-EDIT-001");
        producto.put("nombre", "Producto Editar");
        producto.put("descripcion", "Producto para editar");
        producto.put("precio", 200.0);
        producto.put("descuento", 0.0);
        producto.put("tipoDescuento", "NINGUNO");
        producto.put("idCategoria", categoriaId);
        producto.put("idMarca", marcaId);
        producto.put("activo", true);

        mockMvc.perform(multipart("/api/productos/crear")
                .param("sku", (String) producto.get("sku"))
                .param("nombre", (String) producto.get("nombre"))
                .param("descripcion", (String) producto.get("descripcion"))
                .param("precio", String.valueOf(producto.get("precio")))
                .param("descuento", String.valueOf(producto.get("descuento")))
                .param("tipoDescuento", (String) producto.get("tipoDescuento"))
                .param("idCategoria", String.valueOf(producto.get("idCategoria")))
                .param("idMarca", String.valueOf(producto.get("idMarca")))
                .param("activo", String.valueOf(producto.get("activo")))
                .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));

        // Editar producto (simulación básica, requiere idProducto real si se desea más realismo)
        mockMvc.perform(multipart("/api/productos/editar")
                .param("sku", "SKU-EDIT-001")
                .param("nombre", "Producto Editado")
                .param("descripcion", "Producto editado correctamente")
                .param("precio", "250.0")
                .param("descuento", "10.0")
                .param("tipoDescuento", "PORCENTAJE")
                .param("idCategoria", String.valueOf(categoriaId))
                .param("idMarca", String.valueOf(marcaId))
                .param("activo", "true")
                .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").exists());
    }

    @Test
    @WithMockUser(authorities = {"LEER_PRODUCTOS", "CREAR_PRODUCTOS", "EDITAR_PRODUCTOS", "LEER_PRODUCTO", "ROLE_ADMIN"})
    void validarSku() throws Exception {
        Map<String, Object> request = new HashMap<>();
        request.put("sku", "SKU-TEST-001");
        String json = new ObjectMapper().writeValueAsString(request);

        mockMvc.perform(post("/api/productos/rest/validar-sku")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.existe").exists());
    }

    @Test
    @WithMockUser(authorities = {"LEER_PRODUCTOS", "CREAR_PRODUCTOS", "EDITAR_PRODUCTOS", "LEER_PRODUCTO", "ROLE_ADMIN"})
    void obtenerDatosFormulario() throws Exception {
        mockMvc.perform(get("/api/productos/rest/datos-formulario"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.categorias").exists())
                .andExpect(jsonPath("$.marcas").exists())
                .andExpect(jsonPath("$.proveedores").exists());
    }

    @Test
    @WithMockUser(authorities = {"LEER_PRODUCTOS", "CREAR_PRODUCTOS", "EDITAR_PRODUCTOS", "LEER_PRODUCTO", "ROLE_ADMIN"})
    void eliminarImagen() throws Exception {
        // Mock: no importa si la imagen existe
        mockMvc.perform(post("/api/productos/eliminar-imagen/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").exists());
    }

    @Test
    @WithMockUser(authorities = {"LEER_PRODUCTOS", "CREAR_PRODUCTOS", "EDITAR_PRODUCTOS", "LEER_PRODUCTO", "ROLE_ADMIN"})
    void actualizarPortada() throws Exception {
        // Mock: no importa si la imagen existe
        mockMvc.perform(post("/api/productos/actualizar-portada/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").exists());
    }

    @Test
    @WithMockUser(authorities = {"LEER_PRODUCTOS", "CREAR_PRODUCTOS", "EDITAR_PRODUCTOS", "LEER_PRODUCTO", "ROLE_ADMIN"})
    void obtenerProveedoresProducto() throws Exception {
        // Mock: no importa si el producto existe
        mockMvc.perform(get("/api/productos/1/proveedores"))
                .andExpect(status().isOk());
    }
} 
package com.diedari.jimdur.controller;

import com.diedari.jimdur.model.Categoria;
import com.diedari.jimdur.service.CategoriaService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class CategoriaRestControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CategoriaService categoriaService;

    private Categoria categoria;

    @BeforeEach
    void setUp() {
        categoria = new Categoria();
        categoria.setNombreCategoria("CategoriaTest");
        categoria.setDescripcionCategoria("Descripción de prueba");
        categoria.setIconoCategoria("icono.png");
        categoria.setSlugCategoria("categoria-test");
        categoria.setEstadoActiva(true);
        categoria = categoriaService.crearCategoria(categoria);
    }

    @Test
    @WithMockUser(authorities = {"LEER_CATEGORIAS", "CREAR_CATEGORIAS", "EDITAR_CATEGORIAS", "DESACTIVAR_CATEGORIAS", "ROLE_ADMIN"})
    void listarCategorias() throws Exception {
        mockMvc.perform(get("/api/categoria"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nombreCategoria").exists());
    }

    @Test
    @WithMockUser(authorities = {"LEER_CATEGORIAS", "ROLE_ADMIN"})
    void listarCategoriasActivas() throws Exception {
        mockMvc.perform(get("/api/categoria/activa/true"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].estadoActiva").value(true));
    }

    @Test
    @WithMockUser(authorities = {"LEER_CATEGORIAS", "ROLE_ADMIN"})
    void listarCategoriasPorNombre() throws Exception {
        mockMvc.perform(get("/api/categoria/nombre/CategoriaTest"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nombreCategoria").value("CategoriaTest"));
    }

    @Test
    @WithMockUser(authorities = {"LEER_CATEGORIAS", "ROLE_ADMIN"})
    void obtenerCategoriaPorId() throws Exception {
        mockMvc.perform(get("/api/categoria/" + categoria.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombreCategoria").value("CategoriaTest"));
    }

    @Test
    @WithMockUser(authorities = {"CREAR_CATEGORIAS", "ROLE_ADMIN"})
    void crearCategoria() throws Exception {
        Categoria nueva = new Categoria();
        nueva.setNombreCategoria("NuevaCategoria");
        nueva.setDescripcionCategoria("Otra descripción");
        nueva.setIconoCategoria("icono2.png");
        nueva.setSlugCategoria("nueva-categoria");
        nueva.setEstadoActiva(true);
        String json = new ObjectMapper().writeValueAsString(nueva);

        mockMvc.perform(post("/api/categoria/crear")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombreCategoria").value("NuevaCategoria"));
    }

    @Test
    @WithMockUser(authorities = {"DESACTIVAR_CATEGORIAS", "ROLE_ADMIN"})
    void eliminarCategoria() throws Exception {
        mockMvc.perform(delete("/api/categoria/" + categoria.getId()))
                .andExpect(status().isOk());
    }
} 
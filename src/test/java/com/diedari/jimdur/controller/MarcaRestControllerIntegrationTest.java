package com.diedari.jimdur.controller;

import com.diedari.jimdur.model.Marca;
import com.diedari.jimdur.service.MarcaService;
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
public class MarcaRestControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private MarcaService marcaService;

    private Marca marca;

    @BeforeEach
    void setUp() {
        marca = new Marca();
        marca.setNombreMarca("MarcaTest");
        marca.setDescripcionMarca("Descripción de prueba");
        marca.setLogourlMarca("logo.png");
        marca.setPaisOrigenMarca("Perú");
        marca.setSitioWebMarca("https://marca.com");
        marca.setSlugMarca("marca-test");
        marca.setEstadoMarca(true);
        marca = marcaService.guardarMarcaNuevo(marca);
    }

    @Test
    @WithMockUser(authorities = {"LEER_MARCAS", "CREAR_MARCAS", "EDITAR_MARCAS", "DESACTIVAR_MARCAS", "ROLE_ADMIN"})
    void listarTodasLasMarcas() throws Exception {
        mockMvc.perform(get("/api/marca"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nombreMarca").exists());
    }

    @Test
    @WithMockUser(authorities = {"LEER_MARCAS", "ROLE_ADMIN"})
    void obtenerMarcaPorId() throws Exception {
        mockMvc.perform(get("/api/marca/id/" + marca.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombreMarca").value("MarcaTest"));
    }

    @Test
    @WithMockUser(authorities = {"LEER_MARCAS", "ROLE_ADMIN"})
    void obtenerMarcaPorNombre() throws Exception {
        mockMvc.perform(get("/api/marca/nombre/MarcaTest"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nombreMarca").value("MarcaTest"));
    }

    @Test
    @WithMockUser(authorities = {"LEER_MARCAS", "ROLE_ADMIN"})
    void obtenerMarcaPorEstado() throws Exception {
        mockMvc.perform(get("/api/marca/estado/true"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].estadoMarca").value(true));
    }

    @Test
    @WithMockUser(authorities = {"CREAR_MARCAS", "ROLE_ADMIN"})
    void crearMarca() throws Exception {
        Marca nueva = new Marca();
        nueva.setNombreMarca("NuevaMarca");
        nueva.setDescripcionMarca("Otra descripción");
        nueva.setLogourlMarca("logo2.png");
        nueva.setPaisOrigenMarca("Chile");
        nueva.setSitioWebMarca("https://nueva.com");
        nueva.setSlugMarca("nueva-marca");
        nueva.setEstadoMarca(true);
        String json = new ObjectMapper().writeValueAsString(nueva);

        mockMvc.perform(post("/api/marca/crear")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombreMarca").value("NuevaMarca"));
    }

    @Test
    @WithMockUser(authorities = {"DESACTIVAR_MARCAS", "ROLE_ADMIN"})
    void eliminarMarca() throws Exception {
        mockMvc.perform(delete("/api/marca/eliminar/" + marca.getId()))
                .andExpect(status().isOk());
    }
} 
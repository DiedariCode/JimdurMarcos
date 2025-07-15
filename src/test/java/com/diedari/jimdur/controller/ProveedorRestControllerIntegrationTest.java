package com.diedari.jimdur.controller;

import com.diedari.jimdur.model.Proveedor;
import com.diedari.jimdur.service.ProveedorService;
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
public class ProveedorRestControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ProveedorService proveedorService;

    private Proveedor proveedor;

    @BeforeEach
    void setUp() {
        proveedor = new Proveedor();
        proveedor.setNombre("ProveedorTest");
        proveedor.setNombreComercial("ComercialTest");
        proveedor.setTipoProveedor("Nacional");
        proveedor.setEstadoActivo("Activo");
        proveedor.setRuc("12345678901");
        proveedor.setTelefono("999999999");
        proveedor.setCorreo("proveedor@test.com");
        proveedor = proveedorService.guardarProveedor(proveedor);
    }

    @Test
    @WithMockUser(authorities = {"LEER_PROVEEDORES", "CREAR_PROVEEDORES", "EDITAR_PROVEEDORES", "DESACTIVAR_PROVEEDORES", "ROLE_ADMIN"})
    void listarProveedores() throws Exception {
        mockMvc.perform(get("/api/proveedores"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nombre").exists());
    }

    @Test
    @WithMockUser(authorities = {"LEER_PROVEEDORES", "ROLE_ADMIN"})
    void obtenerProveedorPorId() throws Exception {
        mockMvc.perform(get("/api/proveedores/" + proveedor.getIdProveedor()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("ProveedorTest"));
    }

    @Test
    @WithMockUser(authorities = {"CREAR_PROVEEDORES", "ROLE_ADMIN"})
    void guardarProveedor() throws Exception {
        Proveedor nuevo = new Proveedor();
        nuevo.setNombre("NuevoProveedor");
        nuevo.setNombreComercial("ComercialNuevo");
        nuevo.setTipoProveedor("Internacional");
        nuevo.setEstadoActivo("Activo");
        nuevo.setRuc("10987654321");
        nuevo.setTelefono("988888888");
        nuevo.setCorreo("nuevo@proveedor.com");
        String json = new ObjectMapper().writeValueAsString(nuevo);

        mockMvc.perform(post("/api/proveedores")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("NuevoProveedor"));
    }

    @Test
    @WithMockUser(authorities = {"EDITAR_PROVEEDORES", "ROLE_ADMIN"})
    void actualizarProveedor() throws Exception {
        proveedor.setNombre("ProveedorActualizado");
        proveedor.setRuc("12345678901");
        proveedor.setTelefono("999999999");
        proveedor.setCorreo("proveedor@test.com");
        String json = new ObjectMapper().writeValueAsString(proveedor);

        mockMvc.perform(put("/api/proveedores/" + proveedor.getIdProveedor())
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("ProveedorActualizado"));
    }

    @Test
    @WithMockUser(authorities = {"DESACTIVAR_PROVEEDORES", "ROLE_ADMIN"})
    void eliminarProveedor() throws Exception {
        mockMvc.perform(delete("/api/proveedores/" + proveedor.getIdProveedor()))
                .andExpect(status().isOk());
    }
} 
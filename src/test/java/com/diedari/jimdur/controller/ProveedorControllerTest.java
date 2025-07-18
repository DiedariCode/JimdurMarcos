package com.diedari.jimdur.controller;

import com.diedari.jimdur.dto.AgregarProveedorDTO;
import com.diedari.jimdur.model.Proveedor;
import com.diedari.jimdur.service.ProveedorService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

@WebMvcTest(ProveedorController.class)
public class ProveedorControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProveedorService proveedorService;

    @Test
    @WithMockUser(username = "admin", authorities = {"LEER_PROVEEDORES"})
    void listarProveedores_debeRetornarOk() throws Exception {
        Page<Proveedor> page = new PageImpl<>(List.of(), PageRequest.of(0, 5), 0);
        given(proveedorService.obtenerProveedoresFiltrados(any(), any(), any(), any(PageRequest.class))).willReturn(page);

        mockMvc.perform(get("/admin/proveedor"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "admin", authorities = {"LEER_PROVEEDORES", "CREAR_PROVEEDORES"})
    void mostrarFormularioNuevo_debeRetornarOk() throws Exception {
        mockMvc.perform(get("/admin/proveedor/agregar"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "admin", authorities = {"LEER_PROVEEDORES", "CREAR_PROVEEDORES"})
    void agregarProveedor_debeRedirigir() throws Exception {
        given(proveedorService.guardarProveedor(any(Proveedor.class))).willReturn(new Proveedor());

        mockMvc.perform(post("/admin/proveedor/agregar")
                .param("nombre", "Proveedor Test")
                .param("telefono", "123456789")
                .param("correo", "proveedor@test.com")
                .with(csrf()))
                .andExpect(status().is3xxRedirection());
    }

    @Test
    @WithMockUser(username = "admin", authorities = {"LEER_PROVEEDORES", "EDITAR_PROVEEDORES"})
    void mostrarFormularioEditar_debeRetornarOk() throws Exception {
        Proveedor proveedor = new Proveedor();
        proveedor.setIdProveedor(1L);
        given(proveedorService.obtenerProveedorPorId(1L)).willReturn(proveedor);

        mockMvc.perform(get("/admin/proveedor/1/editar"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "admin", authorities = {"LEER_PROVEEDORES", "EDITAR_PROVEEDORES"})
    void editarProveedor_debeRedirigir() throws Exception {
        Proveedor proveedor = new Proveedor();
        proveedor.setIdProveedor(1L);
        given(proveedorService.obtenerProveedorPorId(1L)).willReturn(proveedor);
        given(proveedorService.guardarProveedor(any(Proveedor.class))).willReturn(proveedor);

        mockMvc.perform(post("/admin/proveedor/1/editar")
                .param("nombre", "Proveedor Editado")
                .param("telefono", "987654321")
                .param("correo", "editado@test.com")
                .with(csrf()))
                .andExpect(status().is3xxRedirection());
    }

    @Test
    @WithMockUser(username = "admin", authorities = {"LEER_PROVEEDORES", "DESACTIVAR_PROVEEDORES"})
    void eliminarProveedor_debeRedirigir() throws Exception {
        Mockito.doNothing().when(proveedorService).eliminarProveedor(anyLong());
        mockMvc.perform(post("/admin/proveedor/eliminar/1").with(csrf()))
                .andExpect(status().is3xxRedirection());
    }

    @Test
    @WithMockUser(username = "admin", authorities = {"LEER_PROVEEDORES", "DESACTIVAR_PROVEEDORES"})
    void cambiarEstado_debeRedirigir() throws Exception {
        Proveedor proveedor = new Proveedor();
        proveedor.setIdProveedor(1L);
        given(proveedorService.obtenerProveedorPorId(1L)).willReturn(proveedor);
        given(proveedorService.guardarProveedor(any(Proveedor.class))).willReturn(proveedor);
        mockMvc.perform(post("/admin/proveedor/1/cambiar-estado").with(csrf()))
                .andExpect(status().is3xxRedirection());
    }
} 
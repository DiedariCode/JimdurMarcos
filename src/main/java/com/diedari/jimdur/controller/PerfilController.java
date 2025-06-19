package com.diedari.jimdur.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import com.diedari.jimdur.service.UsuarioService;
import com.diedari.jimdur.service.MedioPagoService;
import com.diedari.jimdur.service.DireccionService;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.diedari.jimdur.dto.PerfilUsuarioDTO;
import com.diedari.jimdur.dto.MedioPagoDTO;
import com.diedari.jimdur.dto.DireccionProveedorDTO;
import com.diedari.jimdur.model.Direccion;
import org.springframework.web.bind.annotation.PathVariable;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Controller
@RequestMapping("/user")
public class PerfilController {

    @Autowired
    private UsuarioService usuarioService;
    @Autowired
    private MedioPagoService medioPagoService;
    @Autowired
    private DireccionService direccionService;

    // Configura el data binder ANTES de procesar la petición
    @InitBinder("usuario")
    // "No intentes asignar los valores del formulario llamados imagenPerfil y
    // imagenPortada al objeto usuario (PerfilUsuarioDTO), porque esos campos no le
    // pertenecen."
    public void initBinder(WebDataBinder binder) {
        binder.setDisallowedFields("imagenPerfil", "imagenPortada");
    }

    @GetMapping("/perfil")
    public String perfilCliente(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        PerfilUsuarioDTO usuario = usuarioService.obtenerPerfilPorEmail(email);
        Long idUsuario = usuario.getId();
        model.addAttribute("usuario", usuario);
        model.addAttribute("direcciones", direccionService.listarDireccionesPorUsuario(idUsuario));
        model.addAttribute("metodosPago", medioPagoService.listarMediosPagoPorUsuario(idUsuario));
        return "user/perfil";
    }

    @PostMapping("/perfil/editar")
    public String editarPerfilCliente(@ModelAttribute PerfilUsuarioDTO usuarioEditado, RedirectAttributes redirectAttributes) {
        usuarioService.actualizarPerfil(usuarioEditado.getId(), usuarioEditado);
        redirectAttributes.addFlashAttribute("success", "Datos actualizados correctamente.");
        return "redirect:/user/perfil";
    }

    @PostMapping("/perfil/foto")
    public String actualizarFotoPerfil(@RequestParam("id") Long id,
                                       @RequestParam("foto") MultipartFile foto,
                                       RedirectAttributes redirectAttributes) {
        if (foto != null && !foto.isEmpty()) {
            // Validar tamaño máximo (10 MB)
            if (foto.getSize() > 10 * 1024 * 1024) {
                redirectAttributes.addFlashAttribute("mensajeExito", "La imagen es demasiado pesada. El tamaño máximo permitido es 10 MB.");
                return "redirect:/user/perfil";
            }
            try {
                String uploadDir = "uploads";
                File uploadDirFile = new File(uploadDir);
                if (!uploadDirFile.exists()) {
                    uploadDirFile.mkdirs();
                }
                String extension = "";
                String originalName = foto.getOriginalFilename();
                int i = originalName.lastIndexOf('.');
                if (i > 0) {
                    extension = originalName.substring(i);
                }
                String nombreImagen = "perfil_" + id + "_" + UUID.randomUUID() + extension;
                Path rutaArchivo = Paths.get(uploadDir, nombreImagen);
                Files.write(rutaArchivo, foto.getBytes());
                usuarioService.actualizarFotoPerfil(id, nombreImagen);
                redirectAttributes.addFlashAttribute("mensajeExito", "Foto de perfil actualizada correctamente.");
            } catch (IOException e) {
                redirectAttributes.addFlashAttribute("mensajeExito", "Error al guardar la foto de perfil: " + e.getMessage());
            }
        } else {
            redirectAttributes.addFlashAttribute("mensajeExito", "No se seleccionó ninguna imagen.");
        }
        return "redirect:/user/perfil";
    }

    // CRUD DIRECCIONES
    @GetMapping("/perfil/direcciones/nueva")
    public String nuevaDireccion(Model model) {
        model.addAttribute("direccion", new Direccion());
        return "user/direccion-form";
    }

    @PostMapping("/perfil/direcciones/nueva")
    public String guardarDireccion(@ModelAttribute Direccion direccion) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        Long idUsuario = usuarioService.obtenerPerfilPorEmail(email).getId();
        direccion.setUsuario(usuarioService.obtenerUsuarioPorId(idUsuario));
        direccionService.guardarDireccion(direccion);
        return "redirect:/user/perfil#direcciones";
    }

    @GetMapping("/perfil/direcciones/editar/{id}")
    public String editarDireccion(@PathVariable Long id, Model model) {
        Direccion direccion = direccionService.obtenerDireccionPorId(id);
        model.addAttribute("direccion", direccion);
        return "user/direccion-form";
    }

    @PostMapping("/perfil/direcciones/editar/{id}")
    public String actualizarDireccion(@PathVariable Long id, @ModelAttribute Direccion direccion) {
        Direccion existente = direccionService.obtenerDireccionPorId(id);
        if (existente != null) {
            direccion.setIdDireccion(id);
            direccion.setUsuario(existente.getUsuario());
            direccionService.guardarDireccion(direccion);
        }
        return "redirect:/user/perfil#direcciones";
    }

    @PostMapping("/perfil/direcciones/eliminar/{id}")
    public String eliminarDireccion(@PathVariable Long id) {
        direccionService.eliminarDireccion(id);
        return "redirect:/user/perfil#direcciones";
    }
}

package com.diedari.jimdur.controller;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.diedari.jimdur.dto.EmailDTO;
import com.diedari.jimdur.dto.EmailFileDTO;
import com.diedari.jimdur.service.EmailService;


@RestController
@RequestMapping("/api/mail")
public class MailController {

    @Autowired
    private EmailService emailService;

    // * <?> significa cualquier tipo de dato
    @PostMapping("/sendMessage")
    public ResponseEntity<?> receiveRequestEmail(@RequestBody EmailDTO emailDTO) {

        System.out.println("Mensaje recibido: " + emailDTO);

        emailService.sendEmail(emailDTO.getToUser(), emailDTO.getSubject(), emailDTO.getMessage());

        Map<String, String> response = new HashMap<>();

        response.put("estado", "enviado");

        return ResponseEntity.ok(response);
    }

    @PostMapping("/sendMessageFile")
    // CUANDO TRABAJAMOS CON ARCHIVOS, NO SE PUEDE HACER CON @RequestBody, LOS
    // ARCHIVOS NO SON COMPATIBLES CON JSON
    public ResponseEntity<?> receiveRequestEmailWithFile(@ModelAttribute EmailFileDTO emailFileDTO) {

        try {
            String fileName = emailFileDTO.getFile().getName();

            Path path = Paths.get("src/main/resorces/files/" + fileName);

            Files.createDirectories(path.getParent()); // Crea el directorio si no existe
            Files.copy(emailFileDTO.getFile().getInputStream(), path, StandardCopyOption.REPLACE_EXISTING); // Copia el
                                                                                                            // archivo
                                                                                                            // al
                                                                                                            // directorio

            File file = path.toFile(); // Convierte el Path a File

            emailService.sendEmailWithFile(emailFileDTO.getToUser(), emailFileDTO.getSubject(),
                    emailFileDTO.getMessage(), file);

            Map<String, String> response = new HashMap<>();

            response.put("estado", "enviado");
            response.put("archivo", "fileName");

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            throw new RuntimeException("Error al enviar el correo con archivo adjunto", e);
        }
    }
}

package com.diedari.jimdur.service;

import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EmailVerificationServiceImpl implements EmailVerificationService {

    @Autowired
    private EmailService emailService;

    // Almacenamos los códigos temporales en memoria (email -> código)
    private Map<String, String> verificationCodes = new ConcurrentHashMap<>();

    private Random random = new Random();

    @Override
    public void sendVerificationCode(String email) {
        // Generar código de 6 dígitos
        String code = String.format("%06d", random.nextInt(999999));

        // Guardar el código para el email
        verificationCodes.put(email, code);

        // Enviar correo con el código
        String subject = "Código de verificación";
        String message = "Tu código de verificación es: " + code;

        emailService.sendEmail(new String[]{email}, subject, message);
    }

    @Override
    public boolean verifyCode(String email, String code) {
        String storedCode = verificationCodes.get(email);

        if (storedCode != null && storedCode.equals(code)) {
            // Código correcto: eliminamos para no reutilizar
            verificationCodes.remove(email);
            return true;
        }
        return false;
    }
}

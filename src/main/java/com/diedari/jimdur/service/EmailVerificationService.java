package com.diedari.jimdur.service;

public interface EmailVerificationService {
    // Genera y envía el código a un email
    void sendVerificationCode(String email);

    // Valida el código recibido por email
    boolean verifyCode(String email, String code);
}

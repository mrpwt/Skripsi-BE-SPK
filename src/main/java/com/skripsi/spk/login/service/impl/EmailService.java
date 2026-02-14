package com.skripsi.spk.login.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendResetPasswordEmail(String to, String token) {

        String tokenReset = token;

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("Reset Password SPK");
        message.setText(
                "Token untuk reset password (berlaku 15 menit):\n\n"
                        + tokenReset
        );

        mailSender.send(message);
    }
}


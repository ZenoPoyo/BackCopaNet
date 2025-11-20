package com.copanet.api.email;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void enviarCodigo(String correoDestino, String codigo) {
        SimpleMailMessage mensaje = new SimpleMailMessage();
        mensaje.setTo(correoDestino);
        mensaje.setSubject("Código de recuperación de contraseña");
        mensaje.setText("Tu código de recuperación es: " + codigo);

        mailSender.send(mensaje);
    }
}

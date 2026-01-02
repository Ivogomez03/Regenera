package proyecto.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MailService {

    private final JavaMailSender mailSender;

    @Value("${app.base-url}")
    private String baseUrl;

    public void enviarVerificacion(String email, String token){
        String link = baseUrl + "/api/auth/verify?token=" + token;
        send(email, "Verificá tu cuenta", "Hacé clic para verificar: " + link);
    }

    public void enviarReset(String email, String token){
        String link = baseUrl + "/auth/reset?token=" + token; // si tenés página de reset del front
        send(email, "Recuperación de contraseña", "Link para recuperar: " + link);
    }

    private void send(String to, String subject, String text){
        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setTo(to);
        msg.setSubject(subject);
        msg.setText(text);
        mailSender.send(msg);
    }
}

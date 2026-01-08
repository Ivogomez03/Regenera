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

    @Value("${mail.from}")
    private String fromEmail;

    @Value("${app.frontend-url:http://localhost:3000}")
    private String frontendUrl;

    public void enviarVerificacion(String email, String token) {
        String link = frontendUrl + "/verify-email?token=" + token;
        System.out.println("📧 Mail service enviando mail de verificacion " + email);

        send(email, "Verificá tu cuenta en Regenera",
                "Hola,\n\nGracias por registrarte. Hacé clic en el siguiente enlace para activar tu cuenta:\n\n"
                        + link);
    }

    public void enviarReset(String email, String token) {

        String link = frontendUrl + "/reset-password?token=" + token;

        send(email, "Recuperación de contraseña",
                "Para recuperar tu contraseña, hacé clic aquí: " + link);
    }

    private void send(String to, String subject, String text) {
        try {
            SimpleMailMessage msg = new SimpleMailMessage();

            msg.setFrom(fromEmail);

            msg.setTo(to);
            msg.setSubject(subject);
            msg.setText(text);

            mailSender.send(msg);
            System.out.println("✅ Email enviado correctamente a: " + to);

        } catch (Exception e) {
            System.err.println("❌ Error enviando email a " + to + ": " + e.getMessage());
            e.printStackTrace();
        }
    }
}

package proyecto.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import proyecto.dto.EmpresaDto;

@Service
public class EmailService {

    private final JavaMailSender mailSender;

    @Value("${mail.from}")
    private String fromEmail;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Value("${app.frontend-url:http://localhost:3000}")
    private String frontendUrl;

    public void enviarVerificacion(String email, String token) { // <--- Agregamos token
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromEmail);
            message.setTo(email);
            message.setSubject("Verifica tu cuenta en Regenera");

            // Construimos la URL real
            // El usuario llegará a: http://localhost:3000/verify-email?token=xyz...
            String link = frontendUrl + "/verify-email?token=" + token;

            message.setText(
                    "Hola,\n\n" +
                            "Gracias por registrarte. Por favor, haz clic en el siguiente enlace para activar tu cuenta:\n\n"
                            +
                            link + "\n\n" +
                            "Este enlace expira en 24 horas.");

            mailSender.send(message);
            System.out.println("✅ Email enviado a: " + email);
        } catch (Exception e) {
            System.out.println("❌ Error enviando email: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void enviarEmailSolicitudDemo(EmpresaDto empresa) {

        enviarEmailAlUsuario(empresa);

        enviarEmailAPepito(empresa);
    }

    private void enviarEmailAlUsuario(EmpresaDto empresa) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromEmail);
            message.setTo(empresa.getEmail());
            message.setSubject("Tu solicitud de Demo ha sido registrada");
            message.setText(construirCuerpoEmailUsuario(empresa));

            mailSender.send(message);
            System.out.println("✅ Email enviado a usuario: " + empresa.getEmail());
        } catch (Exception e) {
            System.out.println("❌ Error enviando email al usuario: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void enviarEmailAPepito(EmpresaDto empresa) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromEmail);
            message.setTo("pepito@gmail.com");
            message.setSubject("⭐ Nueva solicitud de Demo");
            message.setText(construirCuerpoEmailEmpresa(empresa));

            mailSender.send(message);
            System.out.println("✅ Email enviado a pepito@gmail.com");
        } catch (Exception e) {
            System.out.println("❌ Error enviando email a pepito: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private String construirCuerpoEmailUsuario(EmpresaDto empresa) {
        StringBuilder sb = new StringBuilder();
        sb.append("Hola ").append(empresa.getNombre()).append(",\n\n");
        sb.append("Tu solicitud de demo ha sido registrada exitosamente.\n");
        sb.append("Los datos capturados son:\n\n");

        sb.append("═══════════════════════════════════════════\n");
        sb.append("DATOS PERSONALES\n");
        sb.append("═══════════════════════════════════════════\n");
        sb.append("Nombre: ").append(empresa.getNombre()).append(" ").append(empresa.getApellido()).append("\n");
        sb.append("Email: ").append(empresa.getEmail()).append("\n");
        sb.append("Cargo: ").append(empresa.getCargo()).append("\n");
        sb.append("Teléfono: ").append(empresa.getNumeroCelular()).append("\n\n");

        sb.append("═══════════════════════════════════════════\n");
        sb.append("INFORMACIÓN DE LA EMPRESA\n");
        sb.append("═══════════════════════════════════════════\n");
        sb.append("Empresa: ").append(empresa.getNombreEmpresa()).append("\n");
        sb.append("País: ").append(empresa.getPais()).append("\n");
        sb.append("Tamaño: ").append(empresa.getTamanioEmpresa()).append("\n");
        sb.append("Tipo: ").append(empresa.getTipoEmpresa()).append("\n\n");

        sb.append("═══════════════════════════════════════════\n");
        sb.append("Nos pondremos en contacto en las próximas 24 horas\n");
        sb.append("para agendar tu demo.\n\n");
        sb.append("¡Gracias por tu interés!\n\n");
        sb.append("Equipo de Ventas");

        return sb.toString();
    }

    private String construirCuerpoEmailEmpresa(EmpresaDto empresa) {
        StringBuilder sb = new StringBuilder();
        sb.append("⭐ ¡NUEVA SOLICITUD DE DEMO!\n\n");

        sb.append("═══════════════════════════════════════════\n");
        sb.append("DATOS DEL SOLICITANTE\n");
        sb.append("═══════════════════════════════════════════\n");
        sb.append("Nombre Completo: ").append(empresa.getNombre()).append(" ").append(empresa.getApellido())
                .append("\n");
        sb.append("Email: ").append(empresa.getEmail()).append("\n");
        sb.append("Cargo: ").append(empresa.getCargo()).append("\n");
        sb.append("Teléfono: ").append(empresa.getNumeroCelular()).append("\n\n");

        sb.append("═══════════════════════════════════════════\n");
        sb.append("INFORMACIÓN DE LA EMPRESA\n");
        sb.append("═══════════════════════════════════════════\n");
        sb.append("Nombre de Empresa: ").append(empresa.getNombreEmpresa()).append("\n");
        sb.append("País: ").append(empresa.getPais()).append("\n");
        sb.append("Tamaño: ").append(empresa.getTamanioEmpresa()).append("\n");
        sb.append("Tipo: ").append(empresa.getTipoEmpresa()).append("\n\n");

        sb.append("═══════════════════════════════════════════\n");
        sb.append("Fecha de Solicitud: ").append(empresa.getCreadoEn()).append("\n");
        sb.append("═══════════════════════════════════════════\n\n");

        sb.append("Por favor, contactar al solicitante para agendar la demo.\n\n");
        sb.append("Datos de contacto:\n");
        sb.append("Email: ").append(empresa.getEmail()).append("\n");
        sb.append("Teléfono: ").append(empresa.getNumeroCelular()).append("\n");

        return sb.toString();
    }
}

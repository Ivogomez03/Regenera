package proyecto.config;

import jakarta.servlet.http.HttpServletRequest;

public class CurrentUser {

    public static Long idPersona(HttpServletRequest req) {
        String header = req.getHeader("X-Id-Persona");
        if (header != null) return Long.parseLong(header);
        throw new IllegalStateException("No se encontró id_persona");
    }
}

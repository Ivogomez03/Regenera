package proyecto.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RequestMapping("/api")
@RestController
public class DemoController {

    @GetMapping("/privado")
    public String privado() {
        return "Acceso con JWT OK";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/admin/solo-admin")
    public String soloAdmin() {
        return "Solo ADMIN";
    }
}

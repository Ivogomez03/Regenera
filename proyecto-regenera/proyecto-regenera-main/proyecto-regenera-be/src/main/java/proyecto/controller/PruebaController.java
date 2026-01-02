package proyecto.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import proyecto.request_response.CurrentUserResponse;
import proyecto.service.AuthService;

@RestController
@RequiredArgsConstructor
public class PruebaController {

    private final AuthService authService;

    @GetMapping("/prueba")
    public CurrentUserResponse me() {
        return authService.currentUser();
    }
}

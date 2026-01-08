package proyecto.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import proyecto.dto.*;
import proyecto.repository.PersonaRepository;
import proyecto.request_response.*;
import proyecto.service.AuthService;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthRestController {

    private final AuthService authService;

    private final PersonaRepository personaRepo;

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegistroRequest req) {
        System.out.println("📥 Registro request recibido: " + req);
        authService.register(req);
        return ResponseEntity.ok(Map.of("message", "Te enviamos un mail para verificar la cuenta"));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthRequest> login(@Valid @RequestBody LoginRequest req) {
        AuthRequest tokenResponse = authService.login(req);
        return ResponseEntity.ok(tokenResponse);
    }

    @GetMapping("/verify")
    public ResponseEntity<?> verify(@RequestParam("token") String token) {
        authService.verify(token);
        return ResponseEntity.ok(Map.of("message", "Cuenta verificada, ya podés iniciar sesión"));
    }

    @PostMapping("/forgot")
    public ResponseEntity<?> forgot(@Valid @RequestBody ForgotRequest req) {
        authService.forgot(req);
        return ResponseEntity.ok(Map.of("message", "Si el email existe, enviamos un link de recuperación"));
    }

    @PostMapping("/reset")
    public ResponseEntity<?> reset(@Valid @RequestBody ResetRequest req) {
        authService.reset(req);
        return ResponseEntity.ok(Map.of("message", "Contraseña actualizada"));
    }

    @GetMapping("/me")
    public ResponseEntity<CurrentUserResponse> me() {
        return ResponseEntity.ok(authService.currentUser());
    }
}
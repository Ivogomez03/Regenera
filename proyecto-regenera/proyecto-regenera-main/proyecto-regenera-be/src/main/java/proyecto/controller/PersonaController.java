package proyecto.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import proyecto.repository.PersonaRepository;
import proyecto.service.CurrentUserService;

import java.util.Map;

@RestController
@RequestMapping("/api/persona")
public class PersonaController {

    private final PersonaRepository personaRepo;
    private final CurrentUserService currentUserService;

    public PersonaController(PersonaRepository personaRepo, CurrentUserService currentUserService) {
        this.personaRepo = personaRepo;
        this.currentUserService = currentUserService;
    }

    @GetMapping
    public Map<String,Object> me(){
        var persona = personaRepo.findByCorreoElectronico(currentUserService.getUsername())
                .orElseThrow(() -> new org.springframework.web.server.ResponseStatusException(HttpStatus.NOT_FOUND, "Persona no encontrada"));
        return Map.of("idPersona", persona.getId(), "email", persona.getCorreoElectronico());
    }
}
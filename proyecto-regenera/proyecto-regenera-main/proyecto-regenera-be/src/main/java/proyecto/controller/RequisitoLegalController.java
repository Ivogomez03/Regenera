package proyecto.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import proyecto.dto.RequisitoLegalDto;
import proyecto.repository.UsuarioRepository;
import proyecto.request_response.RequisitoLegalCreateRequest;
import proyecto.request_response.RequisitoLegalResponse;
import proyecto.service.CurrentUserService;
import proyecto.service.RequisitoLegalService;

import java.util.List;

@RestController
@RequestMapping("/api/matriz-legal")
@Tag(name = "Matriz_Legal")
public class RequisitoLegalController {

    private final RequisitoLegalService service;
    private final UsuarioRepository usuarioRepo;
    private final CurrentUserService currentUserService;

    public RequisitoLegalController(RequisitoLegalService service, UsuarioRepository usuarioRepo,
            CurrentUserService currentUserService) {
        this.service = service;
        this.usuarioRepo = usuarioRepo;
        this.currentUserService = currentUserService;
    }

    @PostMapping
    public ResponseEntity<List<RequisitoLegalResponse>> crear(
            @RequestBody List<RequisitoLegalCreateRequest> requisitos) {

        String email = currentUserService.getUsername();
        Long idUsuario = usuarioRepo.findByEmail(email)
                .orElseThrow(() -> new IllegalStateException("Usuario no encontrado"))
                .getId();

        var responses = service.crear(idUsuario, requisitos);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(responses);
    }

    @PutMapping("/{id}")
    public ResponseEntity<RequisitoLegalDto> actualizar(
            @PathVariable Long id,
            @RequestBody RequisitoLegalCreateRequest req) {

        var actualizado = service.actualizar(id, req, currentUserService.getCurrentUserId());
        return ResponseEntity.ok(actualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(
            @PathVariable Long id) {

        service.eliminar(id, currentUserService.getCurrentUserId());
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public RequisitoLegalDto obtener(
            @PathVariable Long id) {

        return RequisitoLegalDto.of(service.obtener(id, currentUserService.getCurrentUserId()));
    }

    @GetMapping
    public List<RequisitoLegalDto> listar() {
        return service.listar(currentUserService.getUsername());
    }

    @GetMapping("/anios")
    public ResponseEntity<List<Integer>> listarAnios() {

        return ResponseEntity.ok(service.obtenerAnios());
    }

    @GetMapping("/plantillas/{idAspecto}")
    public ResponseEntity<List<RequisitoLegalResponse>> obtenerPlantillas(@PathVariable Long idAspecto) {
        return ResponseEntity.ok(service.buscarPlantillasPorAspecto(idAspecto));
    }

}

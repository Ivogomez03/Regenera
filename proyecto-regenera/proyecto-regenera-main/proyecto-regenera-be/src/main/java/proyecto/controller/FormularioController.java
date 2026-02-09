package proyecto.controller;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import proyecto.request_response.FormularioCreateRequest;
import proyecto.dto.FormularioDto;
import proyecto.model.FormularioModel;
import proyecto.repository.UsuarioRepository;
import proyecto.request_response.FormularioResponse;
import proyecto.service.CurrentUserService;
import proyecto.service.FormularioService;

import java.util.List;

@RestController
@RequestMapping("/api/formularios")
public class FormularioController {

    private final FormularioService service;
    private final UsuarioRepository usuarioRepo;
    private final CurrentUserService currentUserService;

    public FormularioController(FormularioService service, UsuarioRepository usuarioRepo,
            CurrentUserService currentUserService) {
        this.service = service;
        this.usuarioRepo = usuarioRepo;
        this.currentUserService = currentUserService;
    }

    @PostMapping
    public ResponseEntity<FormularioResponse> crear(
            @RequestBody @Valid FormularioCreateRequest req) {

        String email = currentUserService.getUsername();
        Long idUsuario = usuarioRepo.findByEmail(email)
                .orElseThrow(() -> new IllegalStateException("Usuario no encontrado"))
                .getId();

        var response = service.crearFormulario(idUsuario, req);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<FormularioDto> actualizar(
            @PathVariable Long id,
            @RequestBody FormularioModel payload) {

        var actualizado = service.actualizarPorEmail(id, payload, currentUserService.getUsername());
        return ResponseEntity.ok(actualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(
            @PathVariable Long id) {
        service.eliminar(id, currentUserService.getUsername());
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<FormularioResponse> obtener(@PathVariable Long id) {

        FormularioResponse response = service.obtener(id, currentUserService.getUsername());

        return ResponseEntity.ok(response);
    }

    @GetMapping("/listar")
    public ResponseEntity<List<FormularioResponse>> listar() {
        return ResponseEntity.ok(service.listar(currentUserService.getUsername()));
    }

}
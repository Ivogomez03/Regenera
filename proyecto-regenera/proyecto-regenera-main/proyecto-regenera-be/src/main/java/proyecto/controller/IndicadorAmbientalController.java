package proyecto.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import proyecto.model.IndicadorAmbientalModel;
import proyecto.repository.UsuarioRepository;
import proyecto.request_response.IndicadorAmbientalCreateRequest;
import proyecto.service.CurrentUserService;
import proyecto.service.IndicadorAmbientalService;

import java.util.List;

@RestController
@RequestMapping("/api/indicadores-ambientales")
@Tag(name = "Indicadores Ambientales", description = "Gestión de indicadores y matriz de objetivos")
public class IndicadorAmbientalController {

    private final IndicadorAmbientalService indicadorService;
    private final CurrentUserService currentUserService;
    private final UsuarioRepository usuarioRepo;

    public IndicadorAmbientalController(IndicadorAmbientalService indicadorService,
            CurrentUserService currentUserService, UsuarioRepository usuarioRepo) {
        this.indicadorService = indicadorService;
        this.usuarioRepo = usuarioRepo;
        this.currentUserService = currentUserService;
    }

    @GetMapping("/listar")
    public ResponseEntity<List<IndicadorAmbientalModel>> listar() {
        return ResponseEntity.ok(indicadorService.listar());
    }

    @GetMapping("/buscar")
    public ResponseEntity<List<IndicadorAmbientalModel>> buscarPorObjetivo(@RequestParam String objetivo) {
        return ResponseEntity.ok(indicadorService.listarPorObjetivo(objetivo));
    }

    @PostMapping("/crear")
    public ResponseEntity<IndicadorAmbientalModel> crear(@RequestBody @Valid IndicadorAmbientalCreateRequest req) {
        Long idUsuario = currentUserService.getCurrentUserId();
        System.out.println("DEBUG: Intentando crear indicador con ID Usuario: " + idUsuario);
        return ResponseEntity.ok(indicadorService.crear(req, idUsuario));
    }
}
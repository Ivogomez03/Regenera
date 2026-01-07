package proyecto.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import proyecto.model.IndicadorAmbientalModel;
import proyecto.request_response.IndicadorAmbientalCreateRequest;
import proyecto.service.IndicadorAmbientalService;

import java.util.List;

@RestController
@RequestMapping("/api/indicadores-ambientales")
@Tag(name = "Indicadores Ambientales", description = "Gestión de indicadores y matriz de objetivos")
public class IndicadorAmbientalController {

    private final IndicadorAmbientalService indicadorService;

    public IndicadorAmbientalController(IndicadorAmbientalService indicadorService) {
        this.indicadorService = indicadorService;
    }

    @GetMapping
    public ResponseEntity<List<IndicadorAmbientalModel>> listar() {
        return ResponseEntity.ok(indicadorService.listar());
    }

    // Endpoint útil para filtrar en el frontend por objetivo específico
    @GetMapping("/buscar")
    public ResponseEntity<List<IndicadorAmbientalModel>> buscarPorObjetivo(@RequestParam String objetivo) {
        return ResponseEntity.ok(indicadorService.listarPorObjetivo(objetivo));
    }

    @PostMapping
    public ResponseEntity<IndicadorAmbientalModel> crear(@RequestBody @Valid IndicadorAmbientalCreateRequest req) {
        return ResponseEntity.ok(indicadorService.crear(req));
    }
}
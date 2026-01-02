package proyecto.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import proyecto.request_response.ActividadCreateRequest;
import proyecto.request_response.ActividadUpdateRequest;
import proyecto.model.ActividadModel;
import proyecto.service.ActividadService;

import java.util.List;

@RestController
@RequestMapping("/api/actividades")
@Tag(name = "Actividades")
public class ActividadController {

    private final ActividadService actividadService;

    public ActividadController(ActividadService actividadService) {
        this.actividadService = actividadService;
    }

    @GetMapping
    public ResponseEntity<List<ActividadModel>> listar() {
        return ResponseEntity.ok(actividadService.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ActividadModel> obtener(@PathVariable Integer id) {
        return ResponseEntity.ok(actividadService.obtenerPorId(id));
    }

    @PostMapping
    public ResponseEntity<ActividadModel> crear(@RequestBody @Valid ActividadCreateRequest req) {
        return ResponseEntity.ok(actividadService.crear(req));
    }

    @PutMapping("/{id:\\d+}")
    public ResponseEntity<ActividadModel> actualizar(@PathVariable Integer id,
                                                     @RequestBody @Valid ActividadUpdateRequest req) {
        return ResponseEntity.ok(actividadService.actualizar(id, req));
    }

    @DeleteMapping("/{id:\\d+}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        actividadService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}

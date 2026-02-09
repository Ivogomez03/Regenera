package proyecto.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import proyecto.request_response.ImpactoAmbientalCreateRequest;
import proyecto.request_response.ImpactoAmbientalUpdateRequest;
import proyecto.model.ImpactoAmbientalModel;
import proyecto.service.ImpactoAmbientalService;

import java.util.List;

@RestController
@RequestMapping("/api/impactos-ambientales")
@RequiredArgsConstructor
public class ImpactoAmbientalController {

    private final ImpactoAmbientalService impactoService;

    @GetMapping
    public ResponseEntity<List<ImpactoAmbientalModel>> listar() {
        return ResponseEntity.ok(impactoService.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ImpactoAmbientalModel> obtener(@PathVariable Integer id) {
        return ResponseEntity.ok(impactoService.obtenerPorId(id));
    }

    @PostMapping
    public ResponseEntity<ImpactoAmbientalModel> crear(@RequestBody @Valid ImpactoAmbientalCreateRequest req) {
        return ResponseEntity.ok(impactoService.crear(req));
    }

    @PutMapping("/{id:\\d+}")
    public ResponseEntity<ImpactoAmbientalModel> actualizar(@PathVariable Integer id,
            @RequestBody @Valid ImpactoAmbientalUpdateRequest req) {
        return ResponseEntity.ok(impactoService.actualizar(id, req));
    }

    @DeleteMapping("/{id:\\d+}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        impactoService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}

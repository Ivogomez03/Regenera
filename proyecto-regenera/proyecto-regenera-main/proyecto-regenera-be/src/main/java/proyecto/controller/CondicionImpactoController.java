package proyecto.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import proyecto.model.CondicionImpactoModel;
import proyecto.service.CondicionImpactoService;

import java.util.List;

@RestController
@RequestMapping("/api/condiciones-impacto")
@RequiredArgsConstructor
public class CondicionImpactoController {

    private final CondicionImpactoService condicionImpactoService;

    @GetMapping
    public ResponseEntity<List<CondicionImpactoModel>> listar() {
        return ResponseEntity.ok(condicionImpactoService.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CondicionImpactoModel> obtener(@PathVariable Integer id) {
        return ResponseEntity.ok(condicionImpactoService.obtenerPorId(id));
    }

    @PostMapping
    public ResponseEntity<CondicionImpactoModel> crear(@RequestBody CondicionImpactoModel nueva) {
        return ResponseEntity.ok(condicionImpactoService.crear(nueva));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CondicionImpactoModel> actualizar(@PathVariable Integer id,
                                                            @RequestBody CondicionImpactoModel datos) {
        return ResponseEntity.ok(condicionImpactoService.actualizar(id, datos));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        condicionImpactoService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
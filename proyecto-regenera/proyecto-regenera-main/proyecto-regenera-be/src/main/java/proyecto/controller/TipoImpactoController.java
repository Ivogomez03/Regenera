package proyecto.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import proyecto.model.TipoImpactoModel;
import proyecto.service.TipoImpactoService;

import java.util.List;

@RestController
@RequestMapping("/api/tipos-impacto")
@RequiredArgsConstructor
@Tag(name = "Tipos de Impacto Ambiental")
public class TipoImpactoController {

    private final TipoImpactoService tipoImpactoService;

    @GetMapping
    public ResponseEntity<List<TipoImpactoModel>> listar() {
        return ResponseEntity.ok(tipoImpactoService.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<TipoImpactoModel> obtener(@PathVariable Integer id) {
        return ResponseEntity.ok(tipoImpactoService.obtenerPorId(id));
    }

    @PostMapping
    public ResponseEntity<TipoImpactoModel> crear(@RequestBody TipoImpactoModel nuevo) {
        return ResponseEntity.ok(tipoImpactoService.crear(nuevo));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TipoImpactoModel> actualizar(@PathVariable Integer id,
            @RequestBody TipoImpactoModel datos) {
        return ResponseEntity.ok(tipoImpactoService.actualizar(id, datos));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        tipoImpactoService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}

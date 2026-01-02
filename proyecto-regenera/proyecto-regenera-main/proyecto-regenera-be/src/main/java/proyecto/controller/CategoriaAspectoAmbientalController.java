package proyecto.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import proyecto.model.CategoriaAspectoAmbientalModel;
import proyecto.service.CategoriaAspectoAmbientalService;

import java.util.List;

@RestController
@RequestMapping("/api/categorias-aspecto-ambiental")
@RequiredArgsConstructor
public class CategoriaAspectoAmbientalController {

    private final CategoriaAspectoAmbientalService service;

    @GetMapping
    public ResponseEntity<List<CategoriaAspectoAmbientalModel>> listar() {
        return ResponseEntity.ok(service.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoriaAspectoAmbientalModel> obtener(@PathVariable Integer id) {
        return ResponseEntity.ok(service.obtener(id));
    }

    @PostMapping
    public ResponseEntity<CategoriaAspectoAmbientalModel> crear(@RequestBody CategoriaAspectoAmbientalModel req) {
        var creado = service.crear(req);
        return ResponseEntity.status(HttpStatus.CREATED).body(creado);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CategoriaAspectoAmbientalModel> actualizar(@PathVariable Integer id,
                                                                     @RequestBody CategoriaAspectoAmbientalModel req) {
        return ResponseEntity.ok(service.actualizar(id, req));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
package proyecto.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import proyecto.model.RubroIndustrialModel;
import proyecto.service.RubroIndustrialService;

import java.util.List;

@RestController
@RequestMapping("/api/rubro-industrial")
@Tag(name = "Rubro_industrial")
public class RubroIndustrialController {

    private final RubroIndustrialService service;

    public RubroIndustrialController(RubroIndustrialService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<RubroIndustrialModel>> listar() {
        return ResponseEntity.ok(service.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<RubroIndustrialModel> buscarPorId(@PathVariable(name = "id") Long id) {
        return service.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}

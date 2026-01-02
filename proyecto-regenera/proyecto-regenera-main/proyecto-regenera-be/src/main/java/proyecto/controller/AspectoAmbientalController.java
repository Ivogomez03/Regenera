package proyecto.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import proyecto.request_response.AspectoAmbientalCreateRequest;
import proyecto.request_response.AspectoAmbientalUpdateRequest;
import proyecto.model.AspectoAmbientalModel;
import proyecto.service.AspectoAmbientalService;

import java.util.List;

@RestController
@RequestMapping("/api/aspectos-ambientales")
@Tag(name = "Aspectos Ambientales")
public class AspectoAmbientalController {

    private final AspectoAmbientalService aspectoService;

    public AspectoAmbientalController(AspectoAmbientalService aspectoService) {
        this.aspectoService = aspectoService;
    }

    @GetMapping
    public ResponseEntity<List<AspectoAmbientalModel>> listar() {
        return ResponseEntity.ok(aspectoService.listar());
    }

    @GetMapping("/{id:\\d+}")
    public ResponseEntity<AspectoAmbientalModel> get(@PathVariable Integer id) {
        return ResponseEntity.ok(aspectoService.get(id));
    }

    @PostMapping
    public ResponseEntity<AspectoAmbientalModel> crear(@RequestBody @Valid AspectoAmbientalCreateRequest req) {
        return ResponseEntity.ok(aspectoService.crear(req));
    }

    @PutMapping("/{id:\\d+}")
    public ResponseEntity<AspectoAmbientalModel> update(@PathVariable Integer id,
                                                        @RequestBody @Valid AspectoAmbientalUpdateRequest req) {
        return ResponseEntity.ok(aspectoService.update(id, req));
    }

    @DeleteMapping("/{id:\\d+}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        aspectoService.delete(id);
        return ResponseEntity.noContent().build();
    }

}

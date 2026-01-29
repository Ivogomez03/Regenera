package proyecto.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import proyecto.request_response.AspectoAmbientalTemaCreateRequest;
import proyecto.request_response.AspectoAmbientalTemaUpdateRequest;
import proyecto.model.AspectoAmbientalTemaModel;
import proyecto.service.AspectoAmbientalTemaService;

import java.util.List;

@RestController
@RequestMapping("/api/aspectos-ambientales-temas")
@Tag(name = "Aspectos Ambientales Temas")
public class AspectoAmbientalTemaController {

    private final AspectoAmbientalTemaService aspectoTemaService;

    public AspectoAmbientalTemaController(AspectoAmbientalTemaService aspectoTemaService) {
        this.aspectoTemaService = aspectoTemaService;
    }

    @GetMapping
    public ResponseEntity<List<AspectoAmbientalTemaModel>> listar() {
        return ResponseEntity.ok(aspectoTemaService.listar());
    }

    @GetMapping("/{id:\\d+}")
    public ResponseEntity<AspectoAmbientalTemaModel> get(@PathVariable Integer id) {
        return ResponseEntity.ok(aspectoTemaService.get(id));
    }

    @PostMapping
    public ResponseEntity<AspectoAmbientalTemaModel> crear(@RequestBody @Valid AspectoAmbientalTemaCreateRequest req) {
        return ResponseEntity.ok(aspectoTemaService.crear(req));
    }

    @PutMapping("/{id:\\d+}")
    public ResponseEntity<AspectoAmbientalTemaModel> update(@PathVariable Integer id,
            @RequestBody @Valid AspectoAmbientalTemaUpdateRequest req) {
        return ResponseEntity.ok(aspectoTemaService.update(id, req));
    }

    @DeleteMapping("/{id:\\d+}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        aspectoTemaService.delete(id);
        return ResponseEntity.noContent().build();
    }

}

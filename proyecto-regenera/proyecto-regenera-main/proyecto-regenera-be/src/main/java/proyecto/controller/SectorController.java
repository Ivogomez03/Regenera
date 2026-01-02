package proyecto.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import proyecto.request_response.SectorRequest;
import proyecto.request_response.SectorResponse;
import proyecto.model.SectorModel;
import proyecto.service.SectorService;

import java.util.List;

@RestController
@RequestMapping("/api/sectores")
@RequiredArgsConstructor
public class SectorController {

    private final SectorService sectorService;

    @GetMapping
    public ResponseEntity<List<SectorModel>> listar() {

        return ResponseEntity.ok(sectorService.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<SectorModel> obtener(@PathVariable Integer id) {
        return ResponseEntity.ok(sectorService.obtenerPorId(id));
    }

    @PostMapping
    public ResponseEntity<SectorResponse> crear(@RequestBody SectorRequest req) {
        return ResponseEntity.status(HttpStatus.CREATED).body(sectorService.crear(req));
    }

    @PutMapping("/{id}")
    public ResponseEntity<SectorResponse> actualizar(@PathVariable Long id, @RequestBody SectorRequest req) {
        return ResponseEntity.ok(sectorService.actualizar(id, req));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        sectorService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
package proyecto.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import proyecto.service.CurrentUserService;

import proyecto.service.MatrizObjetivoService;
import proyecto.dto.MatrizObjetivoDto;
import java.util.List;

@RestController
@RequestMapping("/api/objetivos")
@RequiredArgsConstructor
public class MatrizObjetivoController {

    private final MatrizObjetivoService service;
    private final CurrentUserService currentUserService;

    @GetMapping
    public ResponseEntity<List<MatrizObjetivoDto>> listar() {
        return ResponseEntity.ok(service.listarPorUsuario(currentUserService.getCurrentUserId()));
    }

    @PostMapping
    public ResponseEntity<Void> guardar(@RequestBody List<MatrizObjetivoDto> dtos) {
        service.guardarLista(currentUserService.getCurrentUserId(), dtos);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        service.eliminar(id, currentUserService.getUsername());
        return ResponseEntity.noContent().build();
    }
}
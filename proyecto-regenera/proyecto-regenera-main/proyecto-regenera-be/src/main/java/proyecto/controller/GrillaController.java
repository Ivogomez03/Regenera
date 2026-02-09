package proyecto.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import proyecto.model.GrillaModel;
import proyecto.repository.GrillaRepository;
import proyecto.request_response.GrillaCreateRequest;
import proyecto.request_response.GrillaResponse;
import proyecto.service.CurrentUserService;
import proyecto.service.FormularioService;
import proyecto.service.GrillaService;

import java.util.List;

@RestController
@RequestMapping("/api/grilla")
public class GrillaController {

    private final GrillaService service;
    private final FormularioService formularioService;
    private final GrillaRepository grillaRepository;
    private final CurrentUserService currentUserService;

    public GrillaController(GrillaService service, FormularioService formularioService,
            GrillaRepository grillaRepository,
            CurrentUserService currentUserService) {
        this.service = service;
        this.formularioService = formularioService;
        this.grillaRepository = grillaRepository;
        this.currentUserService = currentUserService;
    }

    @PostMapping("/formularios/{idFormulario}/items")
    public ResponseEntity<GrillaModel> crear(
            @PathVariable Long idFormulario,
            @RequestBody GrillaCreateRequest req) {
        String username = currentUserService.getUsername();
        return ResponseEntity.ok(service.crear(username, idFormulario, req));
    }

    @PutMapping("/items/{idItem}")
    public ResponseEntity<GrillaModel> actualizar(
            @PathVariable Long idItem,
            @RequestBody GrillaCreateRequest req) {
        String username = currentUserService.getUsername();
        return ResponseEntity.ok(service.actualizar(username, idItem, req));
    }

    @DeleteMapping("/items/{idItem}")
    public ResponseEntity<Void> eliminar(
            @PathVariable Long idItem) {
        String username = currentUserService.getUsername();
        service.eliminar(username, idItem);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/formularios/{idFormulario}/items")
    public ResponseEntity<List<GrillaResponse>> listarPorFormulario(
            @PathVariable Long idFormulario,
            @RequestParam(name = "sector", required = false) Long sector) {

        String username = currentUserService.getUsername();

        var formularioResponse = formularioService.obtener(idFormulario, username);

        return ResponseEntity.ok(formularioResponse.getItems());
    }

    @GetMapping("/sectores")
    public List<Long> sectoresDelUsuario() {
        String username = currentUserService.getUsername();
        return grillaRepository.findDistinctSectoresByUsuarioEmail(username);
    }

    @GetMapping("/formularios/{idFormulario}/sectores")
    public List<Long> sectoresPorFormulario(
            @PathVariable Long idFormulario) {
        String username = currentUserService.getUsername();
        return grillaRepository.findDistinctSectoresByFormularioAndEmail(idFormulario, username);
    }

    @GetMapping
    public ResponseEntity<List<GrillaResponse>> listarPorUsuario() {

        return ResponseEntity.ok(service.listarPorUsuario());
    }
}

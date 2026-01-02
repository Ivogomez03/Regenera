package proyecto.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import proyecto.dto.FirmaAprobacionDto;
import proyecto.request_response.FirmaAprobacionCreateRequest;
import proyecto.service.CurrentUserService;
import proyecto.service.FirmaAprobacionService;

import java.util.List;

@RestController
@RequestMapping("/api/firmas")
public class FirmaAprobacionController {

    private final FirmaAprobacionService firmaService;
    private final CurrentUserService currentUserService;

    public FirmaAprobacionController(FirmaAprobacionService firmaService, CurrentUserService currentUserService) {
        this.firmaService = firmaService;
        this.currentUserService = currentUserService;
    }

    @PostMapping("/formularios/{idFormulario}")
    public ResponseEntity<FirmaAprobacionDto> guardarFirma(
            @PathVariable Long idFormulario,
            @RequestBody FirmaAprobacionCreateRequest req) {

        String email = currentUserService.getUsername();
        FirmaAprobacionDto resultado = firmaService.guardarFirma(idFormulario, email, req);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(resultado);
    }

    @GetMapping("/formularios/{idFormulario}/actual")
    public ResponseEntity<FirmaAprobacionDto> obtenerFirmaActual(
            @PathVariable Long idFormulario) {

        FirmaAprobacionDto firma = firmaService.obtenerFirmaActual(idFormulario);

        if (firma == null) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(firma);
    }

    @GetMapping("/formularios/{idFormulario}/historial")
    public ResponseEntity<List<FirmaAprobacionDto>> listarFirmas(
            @PathVariable Long idFormulario) {

        List<FirmaAprobacionDto> firmas = firmaService.listarFirmas(idFormulario);
        return ResponseEntity.ok(firmas);
    }
}

package proyecto.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import proyecto.dto.RevisionDto;
import proyecto.service.CurrentUserService;
import proyecto.service.RevisionService;

import java.util.List;

@RestController
@RequestMapping("/api/revisiones")
@RequiredArgsConstructor
public class RevisionController {

    private final RevisionService revisionService;
    private final CurrentUserService currentUserService;

    @GetMapping
    public ResponseEntity<List<RevisionDto>> listarHistorial() {
        return ResponseEntity.ok(revisionService.obtenerHistorial(currentUserService.getUsername()));
    }
}
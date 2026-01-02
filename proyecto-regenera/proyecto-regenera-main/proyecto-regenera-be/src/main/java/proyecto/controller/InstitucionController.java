package proyecto.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import proyecto.request_response.InstitucionCreateRequest;
import proyecto.request_response.InstitucionCreateResponse;
import proyecto.service.InstitucionService;

import java.net.URI;

@RestController
@RequestMapping("/api/institucion")
@RequiredArgsConstructor
public class InstitucionController {

    private final InstitucionService service;

    @PostMapping
    public ResponseEntity<InstitucionCreateResponse> crear(@Valid @RequestBody InstitucionCreateRequest req) {
        var res = service.registrar(req);
        URI location = URI.create("/api/instituciones/" + res.getIdInstitucion());
        return ResponseEntity.created(location).body(res);
    }
}

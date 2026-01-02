package proyecto.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import proyecto.dto.LocalidadModelDto;
import proyecto.dto.ProvinciaDto;
import proyecto.model.ProvinciaModel;
import proyecto.service.LocalidadService;
import proyecto.service.ProvinciaService;

import java.util.List;

@RestController
@RequestMapping("/api/provincias")
@RequiredArgsConstructor
@Tag(name = "Provincias")
public class ProvinciaController {

    private final ProvinciaService provinciaService;
    private final LocalidadService localidadService;

    @GetMapping
    public List<ProvinciaModel> listar() {
        return provinciaService.listar();
    }

    @GetMapping("/{id}")
    public ProvinciaModel obtener(@PathVariable(name = "id") Long id) {
        return provinciaService.obtener(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ProvinciaModel crear(@Valid @RequestBody ProvinciaDto req) {
        return provinciaService.crear(req);
    }

    @PutMapping("/{id}")
    public ProvinciaModel actualizar(@PathVariable(name = "id") Long id, @Valid @RequestBody ProvinciaDto req) {
        return provinciaService.actualizar(id, req);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminar(@PathVariable(name = "id") Long id) {
        provinciaService.eliminar(id);
    }

    @GetMapping("/{id}/localidades")
    public List<LocalidadModelDto> listarLocalidades(@PathVariable(name = "id") Integer id) {
        return localidadService.listar(id, null);
    }

}
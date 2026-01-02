package proyecto.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import proyecto.dto.LocalidadDto;
import proyecto.dto.LocalidadModelDto;
import proyecto.model.LocalidadModel;
import proyecto.service.LocalidadService;

import java.util.List;


@RestController
@RequestMapping("/api/localidades")
@Tag(name = "Localidades")
public class LocalidadController {

    private final LocalidadService localidadService;

    public LocalidadController(LocalidadService localidadService) {
        this.localidadService = localidadService;
    }

    @GetMapping
    public List<LocalidadModelDto> listar(@RequestParam(name = "idProvincia", required = false) Integer idProvincia,
                                          @RequestParam(name = "nombreLocalidad", required = false) String nombreLocalidad) {
        return localidadService.listar(idProvincia, nombreLocalidad);
    }

    @GetMapping("/{id}")
    public LocalidadModelDto obtener(@PathVariable(name = "id") Long id) {
        return localidadService.obtenerDto(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public LocalidadModelDto crear(@Valid @RequestBody LocalidadDto req) {
        return localidadService.crearDto(req);
    }

    @PutMapping("/{id}")
    public LocalidadModelDto actualizar(@PathVariable(name = "id") Long id, @Valid @RequestBody LocalidadDto req) {
        return localidadService.actualizarDto(id, req);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminar(@PathVariable(name = "id") Long id) {
        localidadService.eliminar(id);
    }
}

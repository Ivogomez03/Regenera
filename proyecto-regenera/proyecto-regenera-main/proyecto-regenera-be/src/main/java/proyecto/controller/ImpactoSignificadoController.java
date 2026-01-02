package proyecto.controller;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import proyecto.model.ImpactoSignificadoModel;
import proyecto.service.ImpactoSignificadoService;

import java.util.List;

@RestController
@RequestMapping("/api/impactos-significado")
public class ImpactoSignificadoController {


    private final ImpactoSignificadoService service;

    public ImpactoSignificadoController(ImpactoSignificadoService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<ImpactoSignificadoModel> create(@RequestBody @Valid ImpactoSignificadoModel m){
        return ResponseEntity.ok(service.create(m));
    }

    @GetMapping
    public List<ImpactoSignificadoModel> list(){ return service.list(); }

    @GetMapping("/{id}")
    public ImpactoSignificadoModel get(@PathVariable Long id){
        return service.get(id);
    }

    @PutMapping("/{id}")
    public ImpactoSignificadoModel update(@PathVariable Long id, @RequestBody @Valid ImpactoSignificadoModel m){
        return service.update(id, m);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id){
        service.delete(id);
    }

}

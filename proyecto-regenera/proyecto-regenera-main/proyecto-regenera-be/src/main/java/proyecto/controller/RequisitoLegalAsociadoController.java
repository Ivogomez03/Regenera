package proyecto.controller;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import proyecto.model.RequisitoLegalAsociadoModel;
import proyecto.service.RequisitoLegalAsociadoService;

import java.util.List;

@RestController
@RequestMapping("/api/requisitos-legales-asociados")
public class RequisitoLegalAsociadoController {

    private final RequisitoLegalAsociadoService service;

    public RequisitoLegalAsociadoController(RequisitoLegalAsociadoService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<RequisitoLegalAsociadoModel> create(@RequestBody @Valid RequisitoLegalAsociadoModel m){
        return ResponseEntity.ok(service.create(m));
    }

    @GetMapping
    public List<RequisitoLegalAsociadoModel> list(){
        return service.list();
    }

    @GetMapping("/{id}")
    public RequisitoLegalAsociadoModel get(@PathVariable Long id){
        return service.get(id);
    }

    @PutMapping("/{id}")
    public RequisitoLegalAsociadoModel update(@PathVariable Long id, @RequestBody @Valid RequisitoLegalAsociadoModel m){
        return service.update(id, m);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id){
        service.delete(id);
    }
}

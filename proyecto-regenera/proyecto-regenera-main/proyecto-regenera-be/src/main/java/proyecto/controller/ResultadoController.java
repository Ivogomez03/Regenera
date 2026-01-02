package proyecto.controller;

import org.springframework.web.bind.annotation.*;
import proyecto.model.ResultadoModel;
import proyecto.service.ResultadoService;

import java.util.List;

@RestController
@RequestMapping("/api/resultados")
public class ResultadoController {

    private final ResultadoService service;

    public ResultadoController(ResultadoService service){
        this.service = service;
    }

    @GetMapping
    public List<ResultadoModel> list(){

        return service.list();
    }

    @GetMapping("/{id}")
    public ResultadoModel get(@PathVariable Long id){

        return service.get(id);
    }

    @PostMapping
    public ResultadoModel create(@RequestBody ResultadoModel r){

        return service.create(r);
    }

    @PutMapping("/{id}")
    public ResultadoModel update(@PathVariable Long id, @RequestBody ResultadoModel r){

        return service.update(id, r);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id){
        service.delete(id);
    }
}

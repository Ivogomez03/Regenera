package proyecto.controller;

import org.springframework.web.bind.annotation.*;
import proyecto.model.TipoModel;
import proyecto.service.TipoService;

import java.util.List;

@RestController
@RequestMapping("/api/tipos")
public class TipoController {

    private final TipoService service;

    public TipoController(TipoService service){
        this.service = service;
    }

    @GetMapping
    public List<TipoModel> list(){
        return service.list();
    }

    @GetMapping("/{id}") public TipoModel get(@PathVariable Long id){
        return service.get(id);
    }

    @PostMapping
    public TipoModel create(@RequestBody TipoModel t){
        return service.create(t);
    }

    @PutMapping("/{id}")
    public TipoModel update(@PathVariable Long id, @RequestBody TipoModel t){
        return service.update(id, t);
    }

    @DeleteMapping("/{id}") public void delete(@PathVariable Long id){
        service.delete(id);
    }
}

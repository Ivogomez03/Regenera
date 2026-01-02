package proyecto.controller;

import org.springframework.web.bind.annotation.*;
import proyecto.model.AmbitoModel;
import proyecto.service.AmbitoService;

import java.util.List;

@RestController
@RequestMapping("/api/ambitos")
public class AmbitoController {

    private final AmbitoService service;

    public AmbitoController(AmbitoService service){
        this.service = service;
    }

    @GetMapping
    public List<AmbitoModel> list(){
        return service.list();
    }

    @GetMapping("/{id}")
    public AmbitoModel get(@PathVariable Long id){
        return service.get(id);
    }

    @PostMapping
    public AmbitoModel create(@RequestBody AmbitoModel a){
        return service.create(a);
    }

    @PutMapping("/{id}")
    public AmbitoModel update(@PathVariable Long id, @RequestBody AmbitoModel a){
        return service.update(id, a);
    }

    @DeleteMapping("/{id}") public void delete(@PathVariable Long id){
        service.delete(id);
    }
}

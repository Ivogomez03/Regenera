package proyecto.service;

import org.springframework.stereotype.Service;
import proyecto.model.AmbitoModel;
import proyecto.repository.AmbitoRepository;

import java.util.List;

@Service
public class AmbitoService {

    private final AmbitoRepository repo;

    public AmbitoService(AmbitoRepository repo) {
        this.repo = repo;
    }

    public AmbitoModel create(AmbitoModel a){
        return repo.save(a);
    }

    public AmbitoModel update(Long id, AmbitoModel a){
        AmbitoModel db = repo.findById(id).orElseThrow();
        db.setAmbito(a.getAmbito());
        return repo.save(db);
    }

    public void delete(Long id){
        repo.deleteById(id);
    }

    public AmbitoModel get(Long id){
        return repo.findById(id).orElseThrow();
    }

    public List<AmbitoModel> list(){
        return repo.findAll();
    }

}

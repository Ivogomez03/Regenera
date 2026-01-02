package proyecto.service;

import org.springframework.stereotype.Service;
import proyecto.model.TipoModel;
import proyecto.repository.TipoRepository;

import java.util.List;

@Service
public class TipoService {

    private final TipoRepository repo;

    public TipoService(TipoRepository repo) {

        this.repo = repo;
    }

    public TipoModel create(TipoModel t){

        return repo.save(t);
    }

    public TipoModel update(Long id, TipoModel t){
        TipoModel db = repo.findById(id).orElseThrow();
        db.setTipo(t.getTipo());
        return repo.save(db);
    }

    public void delete(Long id){

        repo.deleteById(id);
    }

    public TipoModel get(Long id){

        return repo.findById(id).orElseThrow();
    }

    public List<TipoModel> list(){

        return repo.findAll();
    }
}

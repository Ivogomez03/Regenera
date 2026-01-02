package proyecto.service;

import org.springframework.stereotype.Service;
import proyecto.model.ResultadoModel;
import proyecto.repository.ResultadoRepository;

import java.util.List;

@Service
public class ResultadoService {

    private final ResultadoRepository repo;

    public ResultadoService(ResultadoRepository repo) {
        this.repo = repo;
    }

    public ResultadoModel create(ResultadoModel r){
        return repo.save(r);
    }

    public ResultadoModel update(Long id, ResultadoModel r){
        ResultadoModel db = repo.findById(id).orElseThrow();
        db.setResultado(r.getResultado());
        return repo.save(db);
    }

    public void delete(Long id){
        repo.deleteById(id);
    }

    public ResultadoModel get(Long id){
        return repo.findById(id).orElseThrow();
    }

    public List<ResultadoModel> list(){
        return repo.findAll();
    }
}

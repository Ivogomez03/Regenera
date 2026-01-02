package proyecto.service;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import proyecto.model.RequisitoLegalAsociadoModel;
import proyecto.repository.RequisitoLegalAsociadoRepository;

import java.util.List;

@Service
public class RequisitoLegalAsociadoService {

    private final RequisitoLegalAsociadoRepository repo;

    public RequisitoLegalAsociadoService(RequisitoLegalAsociadoRepository repo) {
        this.repo = repo;
    }

    @Transactional
    public RequisitoLegalAsociadoModel create(RequisitoLegalAsociadoModel m) {
        return repo.save(m);
    }

    @Transactional
    public RequisitoLegalAsociadoModel update(Long id, RequisitoLegalAsociadoModel m) {
        RequisitoLegalAsociadoModel db = repo.findById(id).orElseThrow();
        db.setRequisitoLegalAsociado(m.getRequisitoLegalAsociado());
        return repo.save(db);
    }

    @Transactional
    public void delete(Long id) {
        repo.deleteById(id);
    }

    public RequisitoLegalAsociadoModel get(Long id) {
        return repo.findById(id).orElseThrow();
    }

    public List<RequisitoLegalAsociadoModel> list() {
        return repo.findAll();
    }
}

package proyecto.service;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import proyecto.model.ImpactoSignificadoModel;
import proyecto.repository.ImpactoSignificadoRepository;

import java.util.List;

@Service
public class ImpactoSignificadoService {

    private final ImpactoSignificadoRepository repo;

    public ImpactoSignificadoService(ImpactoSignificadoRepository repo) {
        this.repo = repo;
    }

    @Transactional
    public ImpactoSignificadoModel create(ImpactoSignificadoModel m) {
        return repo.save(m);
    }

    @Transactional
    public ImpactoSignificadoModel update(Long id, ImpactoSignificadoModel m) {
        ImpactoSignificadoModel db = repo.findById(id).orElseThrow();
        db.setImpactoSignificado(m.getImpactoSignificado());
        return repo.save(db);
    }

    @Transactional
    public void delete(Long id) {
        repo.deleteById(id);
    }

    public ImpactoSignificadoModel get(Long id) {
        return repo.findById(id).orElseThrow();
    }

    public List<ImpactoSignificadoModel> list() {
        return repo.findAll();
    }
}

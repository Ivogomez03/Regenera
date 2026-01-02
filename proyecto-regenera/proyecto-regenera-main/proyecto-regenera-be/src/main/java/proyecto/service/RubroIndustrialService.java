package proyecto.service;

import org.springframework.stereotype.Service;
import proyecto.model.RubroIndustrialModel;
import proyecto.repository.RubroIndustrialRepository;

import java.util.List;
import java.util.Optional;

@Service
public class RubroIndustrialService {

    private final RubroIndustrialRepository repository;

    public RubroIndustrialService(RubroIndustrialRepository repository) {

        this.repository = repository;
    }

    public List<RubroIndustrialModel> listarTodos() {

        return repository.findAll();
    }

    public Optional<RubroIndustrialModel> buscarPorId(Long id) {

        return repository.findById(id);
    }
}

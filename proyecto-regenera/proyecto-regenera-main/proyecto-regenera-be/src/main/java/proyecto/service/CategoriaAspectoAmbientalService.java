package proyecto.service;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import proyecto.model.CategoriaAspectoAmbientalModel;
import proyecto.repository.CategoriaAspectoAmbientalRepository;

import java.util.List;

@Service
public class CategoriaAspectoAmbientalService {

    private final CategoriaAspectoAmbientalRepository repo;

    public CategoriaAspectoAmbientalService(CategoriaAspectoAmbientalRepository repo) {
        this.repo = repo;
    }

    public List<CategoriaAspectoAmbientalModel> listar() {
        return repo.findAll();
    }

    public CategoriaAspectoAmbientalModel obtener(Integer id) {
        return repo.findById(id).orElseThrow();
    }

    @Transactional
    public CategoriaAspectoAmbientalModel crear(CategoriaAspectoAmbientalModel req) {
        if (req.getCategoriaAspectoAmbiental()==null || req.getCategoriaAspectoAmbiental().isBlank()) {
            throw new IllegalArgumentException("La categoría no puede estar vacía");
        }
        var nombre = req.getCategoriaAspectoAmbiental().trim();
        if (repo.existsByCategoriaAspectoAmbientalIgnoreCase(nombre)) {
            throw new IllegalArgumentException("La categoría ya existe: " + nombre);
        }
        var m = new CategoriaAspectoAmbientalModel();
        m.setCategoriaAspectoAmbiental(nombre);
        return repo.save(m);
    }

    @Transactional
    public CategoriaAspectoAmbientalModel actualizar(Integer id, CategoriaAspectoAmbientalModel req) {
        var db = obtener(id);
        if (req.getCategoriaAspectoAmbiental()!=null && !req.getCategoriaAspectoAmbiental().isBlank()) {
            var nombre = req.getCategoriaAspectoAmbiental().trim();
            if (!nombre.equalsIgnoreCase(db.getCategoriaAspectoAmbiental())
                    && repo.existsByCategoriaAspectoAmbientalIgnoreCase(nombre)) {
                throw new IllegalArgumentException("La categoría ya existe: " + nombre);
            }
            db.setCategoriaAspectoAmbiental(nombre);
        }
        return repo.save(db);
    }

    @Transactional
    public void eliminar(Integer id) {
        if (!repo.existsById(id)) throw new IllegalArgumentException("No existe id: " + id);

        repo.deleteById(id);
    }
}

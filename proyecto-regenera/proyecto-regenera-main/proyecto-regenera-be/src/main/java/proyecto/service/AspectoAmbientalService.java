package proyecto.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import proyecto.request_response.AspectoAmbientalCreateRequest;
import proyecto.request_response.AspectoAmbientalUpdateRequest;
import proyecto.model.AspectoAmbientalModel;
import proyecto.repository.AspectoAmbientalRepository;
import proyecto.repository.CategoriaAspectoAmbientalRepository;
import proyecto.repository.ImpactoAmbientalRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AspectoAmbientalService {

    private final AspectoAmbientalRepository aspectoRepo;

    private final ImpactoAmbientalRepository impactoRepo;

    private final CategoriaAspectoAmbientalRepository categoriaRepo;


    public List<AspectoAmbientalModel> listar() {

        return aspectoRepo.findAll();
    }

    public AspectoAmbientalModel get(Integer id) {

        return aspectoRepo.findById(id).orElseThrow();
    }

    @Transactional
    public AspectoAmbientalModel crear(AspectoAmbientalCreateRequest req) {

        var impacto = impactoRepo.findById(req.idImpactoAmbiental()).orElseThrow();

        var categoria = categoriaRepo.findById(req.idCategoriaAspectoAmbiental()).orElseThrow();

        var nuevo = AspectoAmbientalModel.builder()
                .categoria(categoria)
                .aspectoAmbiental(req.aspectoAmbiental())
                .impacto(impacto)
                .build();

        var saved = aspectoRepo.save(nuevo);

        return aspectoRepo.findById(saved.getIdAspectoAmbiental()).orElseThrow();
    }

    @Transactional
    public AspectoAmbientalModel update(Integer id, AspectoAmbientalUpdateRequest req) {
        var db = aspectoRepo.findById(id).orElseThrow();

        if (req.idCategoriaAspectoAmbiental()!=null) {
            var cat = categoriaRepo.findById(req.idCategoriaAspectoAmbiental()).orElseThrow();
            db.setCategoria(cat);
        }

        if (req.aspectoAmbiental() != null) {
            db.setAspectoAmbiental(req.aspectoAmbiental());
        }

        if (req.idImpactoAmbiental() != null) {
            var impacto = impactoRepo.findById(req.idImpactoAmbiental()).orElseThrow();
            db.setImpacto(impacto);
        }

        aspectoRepo.save(db);

        return aspectoRepo.findById(id).orElseThrow();
    }

    @Transactional
    public void delete(Integer id) {
        if (!aspectoRepo.existsById(id)) {
            throw new jakarta.persistence.EntityNotFoundException("Aspecto no existe: " + id);
        }
        try {
            aspectoRepo.deleteById(id);
        } catch (org.springframework.dao.DataIntegrityViolationException e) {
            throw new IllegalStateException("No se puede eliminar: el aspecto está referenciado por otros registros.");
        }
    }
}

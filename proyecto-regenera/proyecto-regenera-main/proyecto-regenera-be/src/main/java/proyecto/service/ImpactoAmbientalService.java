package proyecto.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import proyecto.request_response.ImpactoAmbientalCreateRequest;
import proyecto.request_response.ImpactoAmbientalUpdateRequest;
import proyecto.model.ImpactoAmbientalModel;
import proyecto.repository.ImpactoAmbientalRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ImpactoAmbientalService {

    private final ImpactoAmbientalRepository impactoRepo;

    public List<ImpactoAmbientalModel> listar() {
        return impactoRepo.findAll();
    }

    public ImpactoAmbientalModel obtenerPorId(Integer idImpactoAmbiental) {
        return impactoRepo.findById(idImpactoAmbiental)
                .orElseThrow(
                        () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Impacto ambiental no encontrado"));
    }

    @Transactional
    public ImpactoAmbientalModel crear(ImpactoAmbientalCreateRequest req) {
        if (impactoRepo.existsByImpactoAmbientalIgnoreCase(req.impactoAmbiental().trim())) {
            throw new IllegalArgumentException("Ya existe un impacto ambiental con ese nombre.");
        }

        var entity = ImpactoAmbientalModel.builder()
                .impactoAmbiental(req.impactoAmbiental().trim())
                .resumido(req.resumido())
                .build();

        return impactoRepo.save(entity);
    }

    @Transactional
    public ImpactoAmbientalModel actualizar(Integer id, ImpactoAmbientalUpdateRequest req) {
        var db = impactoRepo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Impacto Ambiental no encontrado: " + id));

        if (req.impactoAmbiental() != null) {
            var nuevo = req.impactoAmbiental().trim();
            if (nuevo.isEmpty())
                throw new IllegalArgumentException("impactoAmbiental no puede quedar vacío.");

            if (!nuevo.equalsIgnoreCase(db.getImpactoAmbiental())
                    && impactoRepo.existsByImpactoAmbientalIgnoreCase(nuevo)) {
                throw new IllegalArgumentException("Ya existe un impacto ambiental con ese nombre.");
            }
            db.setImpactoAmbiental(nuevo);
        }

        if (req.resumido() != null) {
            db.setResumido(req.resumido());
        }

        return impactoRepo.save(db);
    }

    @Transactional
    public void eliminar(Integer id) {
        if (!impactoRepo.existsById(id)) {
            throw new IllegalArgumentException("Impacto Ambiental no encontrado: " + id);
        }
        try {
            impactoRepo.deleteById(id);
        } catch (DataIntegrityViolationException e) {
            throw new IllegalStateException(
                    "No se puede eliminar: el impacto está referenciado por uno o más Aspectos Ambientales.");
        }
    }
}

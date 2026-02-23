package proyecto.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import proyecto.request_response.AspectoAmbientalTemaCreateRequest;
import proyecto.request_response.AspectoAmbientalTemaUpdateRequest;
import proyecto.model.AspectoAmbientalTemaModel;
import proyecto.repository.AspectoAmbientalTemaRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AspectoAmbientalTemaService {

    private final AspectoAmbientalTemaRepository aspectoTemaRepo;

    public List<AspectoAmbientalTemaModel> listar() {

        return aspectoTemaRepo.findAll();
    }

    public AspectoAmbientalTemaModel get(Long id) {

        return aspectoTemaRepo.findById(id).orElseThrow();
    }

    @Transactional
    public AspectoAmbientalTemaModel crear(AspectoAmbientalTemaCreateRequest req) {
        var nuevo = AspectoAmbientalTemaModel.builder()
                .aspectoAmbientalTema(req.aspectoAmbientalTema())
                .build();
        var saved = aspectoTemaRepo.save(nuevo);

        return aspectoTemaRepo.findById(saved.getIdAspectoAmbientalTema()).orElseThrow();
    }

    @Transactional
    public AspectoAmbientalTemaModel update(Long id, AspectoAmbientalTemaUpdateRequest req) {
        var db = aspectoTemaRepo.findById(id).orElseThrow();

        if (req.aspectoAmbientalTema() != null) {
            db.setAspectoAmbientalTema(req.aspectoAmbientalTema());
        }

        aspectoTemaRepo.save(db);

        return aspectoTemaRepo.findById(id).orElseThrow();
    }

    @Transactional
    public void delete(Long id) {
        if (!aspectoTemaRepo.existsById(id)) {
            throw new jakarta.persistence.EntityNotFoundException("Aspecto no existe: " + id);
        }
        try {
            aspectoTemaRepo.deleteById(id);
        } catch (org.springframework.dao.DataIntegrityViolationException e) {
            throw new IllegalStateException("No se puede eliminar: el aspecto está referenciado por otros registros.");
        }
    }
}

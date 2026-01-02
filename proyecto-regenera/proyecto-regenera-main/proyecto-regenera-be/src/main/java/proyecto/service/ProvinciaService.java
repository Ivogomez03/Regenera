package proyecto.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import proyecto.dto.ProvinciaDto;
import proyecto.model.ProvinciaModel;
import proyecto.repository.ProvinciaRepository;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ProvinciaService {

    private final ProvinciaRepository provinciaRepo;

    public List<ProvinciaModel> listar() {
        return provinciaRepo.findAll();
    }

    public ProvinciaModel obtener(Long id) {
        return provinciaRepo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Provincia no encontrada"));
    }

    public ProvinciaModel crear(ProvinciaDto req) {

        if (provinciaRepo.existsByNombreProvinciaIgnoreCase(req.getNombreProvincia())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "La provincia ya existe: " + req.getNombreProvincia());
        }
        var p = new ProvinciaModel();
        p.setNombreProvincia(req.getNombreProvincia());
        return provinciaRepo.save(p);
    }

    public ProvinciaModel actualizar(Long id, ProvinciaDto req) {
        var p = obtener(id);

        if (provinciaRepo.existsByNombreProvinciaIgnoreCase(req.getNombreProvincia()) &&
                !p.getNombreProvincia().equalsIgnoreCase(req.getNombreProvincia())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Ya existe otra provincia con ese nombre");
        }

        p.setNombreProvincia(req.getNombreProvincia());
        return provinciaRepo.save(p);
    }

    public void eliminar(Long id) {
        provinciaRepo.delete(obtener(id));
    }
}

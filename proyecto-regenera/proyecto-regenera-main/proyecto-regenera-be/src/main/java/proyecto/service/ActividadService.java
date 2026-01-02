package proyecto.service;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import proyecto.request_response.ActividadCreateRequest;
import proyecto.request_response.ActividadUpdateRequest;
import proyecto.model.ActividadModel;
import proyecto.repository.ActividadRepository;
import proyecto.repository.SectorRepository;

import java.util.List;

@Service
public class ActividadService {

    private final ActividadRepository actividadRepository;

    private final SectorRepository sectorRepository;

    public ActividadService(ActividadRepository actividadRepository, SectorRepository sectorRepository) {
        this.actividadRepository = actividadRepository;
        this.sectorRepository = sectorRepository;
    }


    public List<ActividadModel> listar() {
        return actividadRepository.findAll();
    }

    public ActividadModel obtenerPorId(Integer idActividad) {
        return actividadRepository.findById(idActividad)
                .orElseThrow(() -> new IllegalArgumentException("Actividad no encontrada: " + idActividad));
    }

    @Transactional
    public ActividadModel crear(ActividadCreateRequest req) {

        if (actividadRepository.existsByActividadIgnoreCase(req.actividad().trim())) {
            throw new IllegalArgumentException("Ya existe una actividad con ese nombre.");
        }

        var sector = sectorRepository.findById(req.idSector())
                .orElseThrow(() -> new IllegalArgumentException("Sector no encontrado: " + req.idSector()));

        var nueva = ActividadModel.builder()
                .actividad(req.actividad().trim())
                .sector(sector)
                .build();

        var saved = actividadRepository.save(nueva);
        // Volvemos a leer con EntityGraph para devolver sector populado
        return actividadRepository.findById(saved.getIdActividad()).orElseThrow();
    }

    @Transactional
    public ActividadModel actualizar(Integer id, ActividadUpdateRequest req) {
        var db = actividadRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Actividad no encontrada: " + id));

        if (req.actividad() != null) {
            var nombre = req.actividad().trim();
            if (nombre.isEmpty()) throw new IllegalArgumentException("El nombre de la actividad no puede quedar vacío.");
            if (!nombre.equalsIgnoreCase(db.getActividad())
                    && actividadRepository.existsByActividadIgnoreCase(nombre)) {
                throw new IllegalArgumentException("Ya existe una actividad con ese nombre.");
            }
            db.setActividad(nombre);
        }

        if (req.idSector() != null) {
            var sector = sectorRepository.findById(req.idSector())
                    .orElseThrow(() -> new IllegalArgumentException("Sector no encontrado: " + req.idSector()));
            db.setSector(sector);
        }

        actividadRepository.save(db);
        return actividadRepository.findById(id).orElseThrow();
    }

    @Transactional
    public void eliminar(Integer id) {
        if (!actividadRepository.existsById(id)) {
            throw new IllegalArgumentException("Actividad no encontrada: " + id);
        }
        try {
            actividadRepository.deleteById(id);
        } catch (org.springframework.dao.DataIntegrityViolationException e) {

            throw new IllegalStateException("No se puede eliminar: la actividad está referenciada por otros registros.");
        }
    }
}

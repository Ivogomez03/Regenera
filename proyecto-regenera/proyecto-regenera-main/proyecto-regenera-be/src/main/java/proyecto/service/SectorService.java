package proyecto.service;

import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import proyecto.request_response.SectorRequest;
import proyecto.request_response.SectorResponse;
import proyecto.model.SectorModel;
import proyecto.repository.SectorRepository;

import java.util.List;

@Service
public class SectorService {

    private final SectorRepository sectorRepository;

    public SectorService(SectorRepository sectorRepository) {
        this.sectorRepository = sectorRepository;
    }

    public List<SectorModel> listar() {
        return sectorRepository.findAll();
    }

    public SectorModel obtenerPorId(Integer idSector) {
        return sectorRepository.findById(idSector)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Sector no encontrado"));
    }

    @Transactional
    public SectorResponse crear(SectorRequest req) {
        if (req.sector() == null || req.sector().isBlank())
            throw new IllegalArgumentException("El nombre del sector no puede estar vacío");

        if (sectorRepository.existsBySectorIgnoreCase(req.sector().trim()))
            throw new IllegalArgumentException("Ya existe un sector con ese nombre");

        var nuevo = SectorModel.builder()
                .sector(req.sector().trim())
                .build();

        return SectorResponse.of(sectorRepository.save(nuevo));
    }

    @Transactional
    public SectorResponse actualizar(Long id, SectorRequest req) {
        var sector = sectorRepository.findById(Math.toIntExact(id))
                .orElseThrow(() -> new IllegalStateException("No se encontró el sector con id " + id));

        if (req.sector() != null && !req.sector().isBlank()) {
            if (sectorRepository.existsBySectorIgnoreCase(req.sector().trim()) &&
                    !req.sector().trim().equalsIgnoreCase(sector.getSector())) {
                throw new IllegalArgumentException("Ya existe otro sector con ese nombre");
            }
            sector.setSector(req.sector().trim());
        }

        return SectorResponse.of(sectorRepository.save(sector));
    }

    @Transactional
    public void eliminar(Long id) {
        if (!sectorRepository.existsById(Math.toIntExact(id)))
            throw new IllegalStateException("No existe el sector con id " + id);
        sectorRepository.deleteById(Math.toIntExact(id));
    }
}

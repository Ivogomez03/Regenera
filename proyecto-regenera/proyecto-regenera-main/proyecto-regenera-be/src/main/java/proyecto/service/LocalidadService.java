package proyecto.service;

import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import proyecto.dto.LocalidadDto;
import proyecto.dto.LocalidadModelDto;
import proyecto.model.LocalidadModel;
import proyecto.model.ProvinciaModel;
import proyecto.repository.LocalidadRepository;
import proyecto.repository.ProvinciaRepository;

import java.util.List;

@Service
@Transactional
public class LocalidadService {

    private final LocalidadRepository locRepo;

    private final ProvinciaRepository provRepo;

    public LocalidadService(LocalidadRepository locRepo, ProvinciaRepository provRepo) {
        this.locRepo = locRepo;
        this.provRepo = provRepo;
    }

    public List<LocalidadModelDto> listar(Integer idProvincia, String nombreLocalidad) {

        List<LocalidadModel> entidades;

        if (idProvincia != null) {
            entidades = locRepo.findByProvincia_IdProvincia(idProvincia);
        } else if (nombreLocalidad != null && !nombreLocalidad.isBlank()) {
            entidades = locRepo.findByNombreLocalidadContainingIgnoreCase(nombreLocalidad);
        } else {
            entidades = locRepo.findAll();
        }

        return entidades.stream()
                .map(LocalidadModelDto::of)
                .toList();
    }

    public LocalidadModel obtener(Long id) {
        return locRepo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Localidad no encontrada"));
    }

    public LocalidadModelDto obtenerDto(Long id) {
        LocalidadModel localidad = obtener(id);
        return LocalidadModelDto.of(localidad);
    }

    public LocalidadModel crear(LocalidadDto req) {
        ProvinciaModel prov = provRepo.findById(req.getIdProvincia())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Provincia no encontrada"));

        if (locRepo.existsByNombreLocalidadIgnoreCaseAndProvincia_IdProvincia(req.getNombreLocalidad(), req.getIdProvincia())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "La localidad ya existe en esa provincia");
        }

        var l = new LocalidadModel();
        l.setNombreLocalidad(req.getNombreLocalidad());
        l.setProvincia(prov);
        return locRepo.save(l);
    }

    public LocalidadModelDto crearDto(LocalidadDto req) {
        LocalidadModel localidad = crear(req);
        return LocalidadModelDto.of(localidad);
    }

    public LocalidadModel actualizar(Long id, LocalidadDto req) {
        var l = obtener(id);

        ProvinciaModel prov = provRepo.findById(req.getIdProvincia())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Provincia no encontrada"));

        if (locRepo.existsByNombreLocalidadIgnoreCaseAndProvincia_IdProvincia(
                req.getNombreLocalidad(), req.getIdProvincia()) &&
                (!l.getNombreLocalidad().equalsIgnoreCase(req.getNombreLocalidad()) ||
                        !l.getProvincia().getIdProvincia().equals(req.getIdProvincia()))) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "La localidad ya existe en esa provincia");
        }

        l.setNombreLocalidad(req.getNombreLocalidad());
        l.setProvincia(prov);
        return locRepo.save(l);
    }

    public LocalidadModelDto actualizarDto(Long id, LocalidadDto req) {
        LocalidadModel localidad = actualizar(id, req);
        return LocalidadModelDto.of(localidad);
    }

    public void eliminar(Long id) {
        locRepo.delete(obtener(id));
    }
}

















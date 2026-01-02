package proyecto.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import proyecto.request_response.InstitucionCreateRequest;
import proyecto.request_response.InstitucionCreateResponse;
import proyecto.request_response.PersonaRequest;
import proyecto.model.InstitucionModel;
import proyecto.model.PersonaModel;
import proyecto.repository.InstitucionRepository;
import proyecto.repository.LocalidadRepository;
import proyecto.repository.PersonaRepository;
import proyecto.repository.RubroIndustrialRepository;

@Service
@RequiredArgsConstructor
public class InstitucionService {

    private final InstitucionRepository institucionRepo;

    private final PersonaRepository personaRepo;

    private final LocalidadRepository localidadRepo;

    private final RubroIndustrialRepository rubroRepo;

    @Transactional
    public InstitucionCreateResponse registrar(InstitucionCreateRequest req) {

        if (institucionRepo.existsByCuit(req.getCuit())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "CUIT ya registrado");
        }

        var institucion = new InstitucionModel();

        institucion.setNombreRazonSocial(req.getNombreRazonSocial());
        institucion.setCuit(req.getCuit());
        institucion.setDomicilioLegal(req.getDomicilioLegal());
        institucion.setDomicilioOperativo(req.getDomicilioOperativo());
        institucion.setCorreoElectronico(req.getCorreoElectronico());
        institucion.setTelefono(req.getTelefono());
        institucion.setDeclaracionAlcanceSga(req.getDeclaracionAlcanceSga());
        institucion.setDeclaracionPoliticaAmbiental(req.getDeclaracionPoliticaAmbiental());
        institucion.setFechaUltimaActualizacionPolitica(req.getFechaUltimaActualizacionPolitica());
        institucion.setLocalidad(localidadRepo.findById(req.getIdLocalidad().longValue())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Localidad no encontrada")));
        institucion.setRubroIndustrial(rubroRepo.findById(req.getIdRubroIndustrial().longValue())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Rubro industrial no encontrado")));

        institucion = institucionRepo.save(institucion);

        Long idAutoridad = guardarPersona(req.getAutoridadSocietaria(), institucion);
        Long idResponsable = guardarPersona(req.getResponsableSga(), institucion);

        return new InstitucionCreateResponse(institucion.getId(), idAutoridad, idResponsable);
    }

    private Long guardarPersona(PersonaRequest personaReq, InstitucionModel institucion) {

        var persona = new PersonaModel();

        persona.setNombre(personaReq.getNombre());
        persona.setApellido(personaReq.getApellido());
        persona.setCorreoElectronico(personaReq.getCorreoElectronico());
        persona.setTelefono(personaReq.getTelefono());
        persona.setCargo(personaReq.getCargo());
        persona.setTipoResponsabilidad(personaReq.getTipoResponsabilidad());
        persona.setAsignacionResponsabilidades(personaReq.getAsignacionResponsabilidades());
        persona.setInstitucion(institucion);
        return personaRepo.save(persona).getId();
    }
}

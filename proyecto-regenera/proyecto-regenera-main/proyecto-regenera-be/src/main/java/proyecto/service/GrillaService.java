package proyecto.service;

import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import proyecto.model.*;
import proyecto.repository.*;
import proyecto.request_response.GrillaCreateRequest;

import java.util.List;

@Service
public class GrillaService {

    private final GrillaRepository grillaRepo;
    private final ImpactoSignificadoRepository impactoSignificadoRepo;
    private final FormularioService formularioService;

    public GrillaService(
            GrillaRepository grillaRepo,
            ImpactoSignificadoRepository impactoSignificadoRepo,
            FormularioService formularioService
    ) {
        this.grillaRepo = grillaRepo;
        this.impactoSignificadoRepo = impactoSignificadoRepo;
        this.formularioService = formularioService;
    }

    private ImpactoSignificadoModel calcularImpacto(Integer s, Integer m, Integer f, Integer r) {
        int total = (s == null ? 0 : s) + (m == null ? 0 : m) + (f == null ? 0 : f) + (r == null ? 0 : r);
        Long idImpacto = (total <= 15) ? 1L : 2L;
        return impactoSignificadoRepo.getReferenceById(idImpacto);
    }

    private void recalcular(GrillaModel g) {
        int total = g.getSeveridad() + g.getMagnitud() + g.getFrecuencia() + g.getReversibilidad();
        g.setValoracion(total);
        g.setImpactoSignificado(calcularImpacto(g.getSeveridad(), g.getMagnitud(), g.getFrecuencia(), g.getReversibilidad()));
    }

    @Transactional
    public GrillaModel crear(String email, Long idFormulario, GrillaCreateRequest req) {

        var formulario = formularioService.mustBeMine(idFormulario, email);

        var grilla = new GrillaModel();
        grilla.setFormulario(formulario);
        grilla.setIdSector(req.idSector());

        ActividadModel actividad = new ActividadModel();
        actividad.setIdActividad(req.idActividad().intValue());
        grilla.setActividad(actividad);

        AspectoAmbientalModel aspectoAmbiental = new AspectoAmbientalModel();
        aspectoAmbiental.setIdAspectoAmbiental(req.idAspectoAmbiental().intValue());
        grilla.setAspectoAmbiental(aspectoAmbiental);

        ImpactoAmbientalModel impactoAmbiental = new ImpactoAmbientalModel();
        impactoAmbiental.setIdImpactoAmbiental(req.idImpactoAmbiental().intValue());
        grilla.setImpactoAmbiental(impactoAmbiental);

        TipoImpactoModel tipoImpacto = new TipoImpactoModel();
        tipoImpacto.setIdTipoImpacto(req.idTipoImpacto().intValue());
        grilla.setTipoImpacto(tipoImpacto);

        CondicionImpactoModel condicionImpacto = new CondicionImpactoModel();
        condicionImpacto.setIdCondicionImpacto(req.idCondicionImpacto().intValue());
        grilla.setCondicionImpacto(condicionImpacto);

        RequisitoLegalAsociadoModel requisitoLegal = new RequisitoLegalAsociadoModel();
        requisitoLegal.setIdRequisitoLegalAsociado(Long.valueOf(req.idRequisitoLegalAsociado().intValue()));
        grilla.setRequisitoLegalAsociado(requisitoLegal);

        grilla.setSeveridad(req.severidad());
        grilla.setMagnitud(req.magnitud());
        grilla.setFrecuencia(req.frecuencia());
        grilla.setReversibilidad(req.reversibilidad());
        grilla.setControl(req.control());
        grilla.setObservaciones(req.observaciones());

        recalcular(grilla);

        return grillaRepo.save(grilla);
    }

    @Transactional
    public GrillaModel actualizar(String email, Long idItem, GrillaCreateRequest req) {

        var db = grillaRepo.findById(idItem)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Item no encontrado"));

        formularioService.mustBeMine(db.getFormulario().getIdFormulario(), email);

        db.setIdSector(req.idSector());

        ActividadModel actividad = new ActividadModel();
        actividad.setIdActividad(req.idActividad().intValue());
        db.setActividad(actividad);

        AspectoAmbientalModel aspectoAmbiental = new AspectoAmbientalModel();
        aspectoAmbiental.setIdAspectoAmbiental(req.idAspectoAmbiental().intValue());
        db.setAspectoAmbiental(aspectoAmbiental);

        ImpactoAmbientalModel impactoAmbiental = new ImpactoAmbientalModel();
        impactoAmbiental.setIdImpactoAmbiental(req.idImpactoAmbiental().intValue());
        db.setImpactoAmbiental(impactoAmbiental);

        TipoImpactoModel tipoImpacto = new TipoImpactoModel();
        tipoImpacto.setIdTipoImpacto(req.idTipoImpacto().intValue());
        db.setTipoImpacto(tipoImpacto);

        CondicionImpactoModel condicionImpacto = new CondicionImpactoModel();
        condicionImpacto.setIdCondicionImpacto(req.idCondicionImpacto().intValue());
        db.setCondicionImpacto(condicionImpacto);

        RequisitoLegalAsociadoModel requisitoLegal = new RequisitoLegalAsociadoModel();
        requisitoLegal.setIdRequisitoLegalAsociado(Long.valueOf(req.idRequisitoLegalAsociado().intValue()));
        db.setRequisitoLegalAsociado(requisitoLegal);

        db.setSeveridad(req.severidad());
        db.setMagnitud(req.magnitud());
        db.setFrecuencia(req.frecuencia());
        db.setReversibilidad(req.reversibilidad());
        db.setControl(req.control());
        db.setObservaciones(req.observaciones());

        recalcular(db);

        return grillaRepo.save(db);
    }

    @Transactional
    public void eliminar(String email, Long idItem) {
        var db = grillaRepo.findById(idItem)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Item no encontrado"));
        formularioService.mustBeMine(db.getFormulario().getIdFormulario(), email);
        grillaRepo.delete(db);
    }

    public List<GrillaModel> listarItems(Long idFormulario, String email, Long sector) {
        formularioService.mustBeMine(idFormulario, email);
        if (sector != null) {
            return grillaRepo.findByFormulario_IdFormularioAndIdSector(idFormulario, sector);
        }
        return grillaRepo.findByFormulario_IdFormulario(idFormulario);
    }
}
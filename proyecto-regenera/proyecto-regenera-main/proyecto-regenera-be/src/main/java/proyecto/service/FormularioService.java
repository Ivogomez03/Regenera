package proyecto.service;

import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import proyecto.request_response.FormularioCreateRequest;
import proyecto.dto.FormularioDto;
import proyecto.dto.GrillaDto;
import proyecto.model.*;
import proyecto.repository.*;
import proyecto.request_response.FormularioResponse;
import proyecto.request_response.GrillaResponse;
import proyecto.request_response.SectorAgrupado;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class FormularioService {

    private final FormularioRepository formularioRepo;
    private final GrillaRepository grillaRepo;
    private final ActividadRepository actividadRepo;
    private final AspectoAmbientalRepository aspectoRepo;
    private final ImpactoAmbientalRepository impactoRepo;
    private final TipoImpactoRepository tipoImpactoRepo;
    private final CondicionImpactoRepository condicionRepo;
    private final ImpactoSignificadoRepository impactoSignificadoRepo;
    private final RequisitoLegalAsociadoRepository reqLegalAsocRepo;
    private final UsuarioRepository usuarioRepo;
    private final SectorRepository sectorRepo;

    public FormularioService(FormularioRepository formularioRepo, GrillaRepository grillaRepo,
                             ActividadRepository actividadRepo, AspectoAmbientalRepository aspectoRepo,
                             ImpactoAmbientalRepository impactoRepo, TipoImpactoRepository tipoImpactoRepo,
                             CondicionImpactoRepository condicionRepo, ImpactoSignificadoRepository impactoSignificadoRepo,
                             RequisitoLegalAsociadoRepository reqLegalAsocRepo, UsuarioRepository usuarioRepo, SectorRepository sectorRepo) {
        this.formularioRepo = formularioRepo;
        this.grillaRepo = grillaRepo;
        this.actividadRepo = actividadRepo;
        this.aspectoRepo = aspectoRepo;
        this.impactoRepo = impactoRepo;
        this.tipoImpactoRepo = tipoImpactoRepo;
        this.condicionRepo = condicionRepo;
        this.impactoSignificadoRepo = impactoSignificadoRepo;
        this.reqLegalAsocRepo = reqLegalAsocRepo;
        this.usuarioRepo = usuarioRepo;
        this.sectorRepo = sectorRepo;
    }

    public FormularioModel mustBeMine(Long idFormulario, String email) {
        return formularioRepo.findByIdFormularioAndUsuario_Email(idFormulario, email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.FORBIDDEN, "Sin permiso o no existe"));
    }

    @Transactional
    public FormularioResponse crearFormulario(Long idUsuario, FormularioCreateRequest req) {

        var usuario = usuarioRepo.findById(idUsuario)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Usuario no encontrado"
                ));

        var formulario = new FormularioModel();
        formulario.setUsuario(usuario);
        formulario.setNombreEmpresa(req.nombreEmpresa());
        formulario.setCodigo(req.codigo());
        formulario.setFecha(req.fecha());
        formularioRepo.save(formulario);

        List<GrillaModel> itemsCreados = new ArrayList<>();

        if (req.items() != null && !req.items().isEmpty()) {
            for (GrillaDto dto : req.items()) {
                var grilla = crearItemGrilla(formulario, req.idSector(), dto);
                itemsCreados.add(grilla);
            }
        }

        if (req.logoEmpresa() != null && !req.logoEmpresa().isEmpty()) {
            try {
                formulario.setLogoEmpresa(Base64.getDecoder().decode(req.logoEmpresa()));
            } catch (IllegalArgumentException e) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Logo inválido");
            }
        }

        return construirResponseAgrupado(formulario, itemsCreados);
    }

    private GrillaModel crearItemGrilla(FormularioModel formulario, Long idSector, GrillaDto dto) {

        var grilla = new GrillaModel();
        grilla.setFormulario(formulario);
        grilla.setIdSector(idSector);

        grilla.setActividad(actividadRepo.getReferenceById(Math.toIntExact(dto.idActividad())));
        grilla.setAspectoAmbiental(aspectoRepo.getReferenceById(Math.toIntExact(dto.idAspectoAmbiental())));
        grilla.setImpactoAmbiental(impactoRepo.getReferenceById(Math.toIntExact(dto.idImpactoAmbiental())));
        grilla.setTipoImpacto(tipoImpactoRepo.getReferenceById(Math.toIntExact(dto.idTipoImpacto())));
        grilla.setCondicionImpacto(condicionRepo.getReferenceById(Math.toIntExact(dto.idCondicionImpacto())));

        grilla.setSeveridad(dto.severidad());
        grilla.setMagnitud(dto.magnitud());
        grilla.setFrecuencia(dto.frecuencia());
        grilla.setReversibilidad(dto.reversibilidad());

        int valoracion = dto.severidad() + dto.magnitud() + dto.frecuencia() + dto.reversibilidad();
        grilla.setValoracion(valoracion);

        Long idImpacto = (valoracion <= 15) ? 1L : 2L;
        grilla.setImpactoSignificado(impactoSignificadoRepo.getReferenceById(idImpacto));

        grilla.setRequisitoLegalAsociado(reqLegalAsocRepo.getReferenceById(dto.idRequisitoLegalAsociado()));
        grilla.setControl(dto.control());
        grilla.setObservaciones(dto.observaciones());

        return grillaRepo.save(grilla);
    }

    private FormularioResponse construirResponseAgrupado(FormularioModel formulario, List<GrillaModel> items) {

        Map<Long, List<GrillaModel>> itemsPorSector = items.stream()
                .collect(Collectors.groupingBy(GrillaModel::getIdSector));

        List<SectorAgrupado> sectores = itemsPorSector.entrySet().stream()
                .map(entry -> crearSectorAgrupado(entry.getKey(), entry.getValue()))
                .sorted(Comparator.comparing(SectorAgrupado::getIdSector))
                .collect(Collectors.toList());

        FormularioResponse response = new FormularioResponse();
        response.setIdFormulario(formulario.getIdFormulario());
        response.setIdUsuario(formulario.getUsuario().getId());
        response.setNombreEmpresa(formulario.getNombreEmpresa());
        response.setCodigo(formulario.getCodigo());
        response.setFecha(formulario.getFecha());
        response.setCreadoEn(formulario.getCreadoEn());
        response.setActualizadoEn(formulario.getActualizadoEn());
        response.setSectores(sectores);

        if (formulario.getLogoEmpresa() != null) {
            response.setLogoEmpresa(Base64.getEncoder().encodeToString(formulario.getLogoEmpresa()));
        }

        return response;
    }

    private SectorAgrupado crearSectorAgrupado(Long idSector, List<GrillaModel> items) {

        var sector = sectorRepo.findById(Math.toIntExact(idSector))
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Sector no encontrado: " + idSector
                ));

        var grillaResponses = items.stream()
                .map(this::convertirGrillaAResponse)
                .collect(Collectors.toList());

        return new SectorAgrupado(
                idSector,
                sector.getSector(),
                grillaResponses.size(),
                grillaResponses
        );
    }

    private GrillaResponse convertirGrillaAResponse(GrillaModel grilla) {
        return new GrillaResponse(
                grilla.getIdItem(),
                Long.valueOf(grilla.getActividad().getIdActividad()),
                grilla.getActividad().getActividad(),
                Long.valueOf(grilla.getAspectoAmbiental().getIdAspectoAmbiental()),
                grilla.getAspectoAmbiental().getAspectoAmbiental(),
                Long.valueOf(grilla.getImpactoAmbiental().getIdImpactoAmbiental()),
                grilla.getImpactoAmbiental().getImpactoAmbiental(),
                grilla.getSeveridad(),
                grilla.getMagnitud(),
                grilla.getFrecuencia(),
                grilla.getReversibilidad(),
                grilla.getValoracion(),
                grilla.getImpactoSignificado().getImpactoSignificado(),
                grilla.getRequisitoLegalAsociado().getRequisitoLegalAsociado(),
                grilla.getControl(),
                grilla.getObservaciones()
        );
    }


    @Transactional
    public FormularioDto actualizarPorEmail(Long idFormulario, FormularioModel f, String emailUsuario) {

        var db = mustBeMine(idFormulario, emailUsuario);
        db.setNombreEmpresa(f.getNombreEmpresa());
        db.setCodigo(f.getCodigo());
        db.setFecha(f.getFecha());

        if (f.getLogoEmpresa() != null) {
            db.setLogoEmpresa(f.getLogoEmpresa());
        }

        return FormularioDto.of(formularioRepo.save(db));
    }

    @Transactional
    public void eliminar(Long idFormulario, String email) {
        var db = mustBeMine(idFormulario, email);
        formularioRepo.delete(db);
    }

    public FormularioModel obtener(Long idFormulario, String email) {
        return mustBeMine(idFormulario, email);
    }

    public List<FormularioDto> listar(String email) {
        return formularioRepo.findByUsuario_Email(email).stream()
                .map(FormularioDto::of)
                .toList();
    }

    public List<GrillaModel> listarItems(Long idFormulario, String email, Long sector) {
        mustBeMine(idFormulario, email);
        if (sector != null) {
            return grillaRepo.findByFormulario_IdFormularioAndIdSector(idFormulario, sector);
        }
        return grillaRepo.findByFormulario_IdFormulario(idFormulario);
    }
}

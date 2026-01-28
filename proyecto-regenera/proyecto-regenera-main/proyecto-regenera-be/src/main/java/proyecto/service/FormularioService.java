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
            RequisitoLegalAsociadoRepository reqLegalAsocRepo, UsuarioRepository usuarioRepo,
            SectorRepository sectorRepo) {
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
                        "Usuario no encontrado"));

        var formulario = new FormularioModel();
        formulario.setUsuario(usuario);
        formulario.setNombreEmpresa(req.nombreEmpresa());
        formulario.setCodigo(req.codigo());
        formulario.setFecha(req.fecha());

        // Guardamos el logo si viene
        if (req.logoEmpresa() != null && !req.logoEmpresa().isEmpty()) {
            try {
                // Decodificamos el Base64 que manda el front a bytes para la BD
                formulario.setLogoEmpresa(Base64.getDecoder().decode(req.logoEmpresa()));
            } catch (IllegalArgumentException e) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Logo inválido");
            }
        }

        // Guardamos primero para tener el ID
        formularioRepo.save(formulario);

        List<GrillaModel> itemsCreados = new ArrayList<>();

        // Si el request trae items, los procesamos
        if (req.items() != null && !req.items().isEmpty()) {
            for (GrillaDto dto : req.items()) {
                var grilla = crearItemGrilla(formulario, req.idSector(), dto);
                itemsCreados.add(grilla);
            }
        }

        // Convertimos la lista de modelos a lista de respuestas con TEXTOS
        List<GrillaResponse> itemsResponse = itemsCreados.stream()
                .map(this::convertirGrillaAResponse)
                .collect(Collectors.toList());

        // Armamos el objeto de respuesta final
        FormularioResponse response = new FormularioResponse();
        response.setIdFormulario(formulario.getIdFormulario());
        response.setIdUsuario(usuario.getId());
        response.setNombreEmpresa(formulario.getNombreEmpresa());
        response.setCodigo(formulario.getCodigo());
        response.setFecha(formulario.getFecha());
        response.setCreadoEn(formulario.getCreadoEn());
        response.setActualizadoEn(formulario.getActualizadoEn());

        // Devolvemos el logo en Base64 para que el front lo pueda mostrar
        if (formulario.getLogoEmpresa() != null) {
            response.setLogoEmpresa(Base64.getEncoder().encodeToString(formulario.getLogoEmpresa()));
        }

        // Devolvemos la lista plana
        response.setItems(itemsResponse);

        return response;
    }

    private GrillaModel crearItemGrilla(FormularioModel formulario, Long idSector, GrillaDto dto) {

        var grilla = new GrillaModel();
        grilla.setFormulario(formulario);
        grilla.setSector(sectorRepo.getReferenceById(Math.toIntExact(idSector)));

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

    private GrillaResponse convertirGrillaAResponse(GrillaModel grilla) {
        return new GrillaResponse(
                grilla.getIdItem(),
                grilla.getSector().getIdSector(),
                grilla.getSector().getSector(),

                // Mapeo de Actividad
                grilla.getActividad().getIdActividad(),
                grilla.getActividad().getActividad(), // Sacamos el nombre

                // Mapeo de Aspecto
                grilla.getAspectoAmbiental().getIdAspectoAmbiental(),
                grilla.getAspectoAmbiental().getAspectoAmbiental(),

                // Mapeo de Impacto
                grilla.getImpactoAmbiental().getIdImpactoAmbiental(),
                grilla.getImpactoAmbiental().getImpactoAmbiental(),

                // Mapeo de Tipo Impacto
                grilla.getTipoImpacto().getIdTipoImpacto(),
                grilla.getTipoImpacto().getTipoImpacto(),

                // Mapeo de Condición
                grilla.getCondicionImpacto().getIdCondicionImpacto(),
                grilla.getCondicionImpacto().getCondicionImpacto(),

                grilla.getSeveridad(),
                grilla.getMagnitud(),
                grilla.getFrecuencia(),
                grilla.getReversibilidad(),
                grilla.getValoracion(),

                // Significancia (Puede ser nulo si no se calculó, usamos protección)
                grilla.getImpactoSignificado() != null ? grilla.getImpactoSignificado().getImpactoSignificado() : "",

                // Requisito Legal
                grilla.getRequisitoLegalAsociado().getIdRequisitoLegalAsociado(),
                grilla.getRequisitoLegalAsociado().getRequisitoLegalAsociado(),

                grilla.getControl(),
                grilla.getObservaciones());
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

    public FormularioResponse obtener(Long idFormulario, String email) {
        FormularioModel formulario = mustBeMine(idFormulario, email);
        return convertirFormularioAResponse(formulario);
    }

    public List<FormularioResponse> listar(String email) {
        return formularioRepo.findByUsuario_Email(email).stream()
                .map(this::convertirFormularioAResponse)
                .collect(Collectors.toList());
    }

    public List<GrillaModel> listarItems(Long idFormulario, String email, Long sector) {
        mustBeMine(idFormulario, email);
        if (sector != null) {
            return grillaRepo.findByFormulario_IdFormularioAndSector_IdSector(idFormulario, sector);
        }
        return grillaRepo.findByFormulario_IdFormulario(idFormulario);
    }

    private FormularioResponse convertirFormularioAResponse(FormularioModel formulario) {
        FormularioResponse response = new FormularioResponse();

        // Datos básicos
        response.setIdFormulario(formulario.getIdFormulario());
        response.setIdUsuario(formulario.getUsuario().getId());
        response.setNombreEmpresa(formulario.getNombreEmpresa());
        response.setCodigo(formulario.getCodigo());
        response.setFecha(formulario.getFecha());
        response.setCreadoEn(formulario.getCreadoEn());
        response.setActualizadoEn(formulario.getActualizadoEn());

        // Logo (Si existe)
        if (formulario.getLogoEmpresa() != null) {
            response.setLogoEmpresa(Base64.getEncoder().encodeToString(formulario.getLogoEmpresa()));
        }

        // Items de la Grilla
        List<GrillaResponse> itemsResponse = grillaRepo
                .findByFormulario_IdFormulario(formulario.getIdFormulario()) != null
                        ? grillaRepo.findByFormulario_IdFormulario(formulario.getIdFormulario()).stream()
                                .map(this::convertirGrillaAResponse)
                                .collect(Collectors.toList())
                        : new ArrayList<>();

        response.setItems(itemsResponse);

        return response;
    }
}

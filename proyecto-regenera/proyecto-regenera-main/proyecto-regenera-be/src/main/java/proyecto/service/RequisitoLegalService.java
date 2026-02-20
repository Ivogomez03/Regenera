package proyecto.service;

import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import proyecto.dto.RequisitoLegalDto;
import proyecto.request_response.RequisitoLegalCreateRequest;
import proyecto.request_response.RequisitoLegalResponse;
import proyecto.model.*;
import proyecto.repository.*;

import java.util.ArrayList;
import java.util.List;

@Service
public class RequisitoLegalService {

    private final RequisitoLegalRepository requisitoLegalRepo;
    private final UsuarioRepository usuarioRepo;
    private final AmbitoRepository ambitoRepo;
    private final TipoRepository tipoRepo;
    private final ResultadoRepository resultadoRepo;
    private final AspectoAmbientalTemaRepository aspectoTemaRepo;

    public RequisitoLegalService(RequisitoLegalRepository requisitoLegalRepo,
            UsuarioRepository usuarioRepo,
            AmbitoRepository ambitoRepo,
            TipoRepository tipoRepo,
            ResultadoRepository resultadoRepo,
            AspectoAmbientalTemaRepository aspectoTemaRepo) {
        this.requisitoLegalRepo = requisitoLegalRepo;
        this.usuarioRepo = usuarioRepo;
        this.ambitoRepo = ambitoRepo;
        this.tipoRepo = tipoRepo;
        this.resultadoRepo = resultadoRepo;
        this.aspectoTemaRepo = aspectoTemaRepo;

    }

    public RequisitoLegalModel mustBeMine(Long idRequisitoLegal, String email) {
        return requisitoLegalRepo.findByIdRequisitoLegalAndUsuario_Email(idRequisitoLegal, email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.FORBIDDEN, "Sin permiso o no existe"));
    }

    public List<RequisitoLegalResponse> buscarPlantillasPorAspecto(Long idAspecto) {

        Long ID_ADMINISTRADOR = 1L;

        return requisitoLegalRepo.findByAspecto_IdAspectoAmbientalTemaAndUsuario_Id(idAspecto, ID_ADMINISTRADOR)
                .stream()
                .map(this::construirResponse)
                .toList();
    }

    @Transactional
    public List<RequisitoLegalResponse> crear(Long idUsuario, List<RequisitoLegalCreateRequest> requisitos) {

        var usuario = usuarioRepo.findById(idUsuario)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Usuario no encontrado"));

        List<RequisitoLegalResponse> respuestas = new ArrayList<>();

        for (RequisitoLegalCreateRequest req : requisitos) {

            var ambito = ambitoRepo.findById(req.getIdAmbito())
                    .orElseThrow(() -> new ResponseStatusException(
                            HttpStatus.NOT_FOUND,
                            "Ãmbito no encontrado"));

            var tipo = tipoRepo.findById(req.getIdTipo())
                    .orElseThrow(() -> new ResponseStatusException(
                            HttpStatus.NOT_FOUND,
                            "Tipo no encontrado"));

            var aspecto = aspectoTemaRepo.findById(Math.toIntExact(req.getIdAspectoAmbiental()))
                    .orElseThrow(() -> new ResponseStatusException(
                            HttpStatus.NOT_FOUND,
                            "Aspecto ambiental no encontrado"));

            ResultadoModel resultado = null;
            if (req.getIdResultado() != null) {
                resultado = resultadoRepo.findById(req.getIdResultado())
                        .orElseThrow(() -> new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                "Resultado no encontrado"));
            }

            var requisitoLegal = new RequisitoLegalModel();
            requisitoLegal.setUsuario(usuario);
            requisitoLegal.setAmbito(ambito);
            requisitoLegal.setTipo(tipo);
            requisitoLegal.setResultado(resultado);
            requisitoLegal.setAspecto(aspecto);
            requisitoLegal.setNumero(req.getNumero());
            requisitoLegal.setFecha(req.getFecha());
            requisitoLegal.setAnio(req.getAnio());
            requisitoLegal.setResena(req.getResena());
            requisitoLegal.setObligacion(req.getObligacion());
            requisitoLegal.setPuntoInspeccion(req.getPuntoInspeccion());

            requisitoLegalRepo.save(requisitoLegal);
            respuestas.add(construirResponse(requisitoLegal));
        }

        return respuestas;
    }

    @Transactional
    public RequisitoLegalDto actualizar(Long idRequisitoLegal, RequisitoLegalCreateRequest req, String email) {
        var db = mustBeMine(idRequisitoLegal, email);

        if (req.getIdAmbito() != null) {
            var ambito = ambitoRepo.findById(req.getIdAmbito())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Ámbito no encontrado"));
            db.setAmbito(ambito);
        }

        if (req.getIdTipo() != null) {
            var tipo = tipoRepo.findById(req.getIdTipo())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Tipo no encontrado"));
            db.setTipo(tipo);
        }

        if (req.getIdAspectoAmbiental() != null) {
            var aspecto = aspectoTemaRepo.findById(Math.toIntExact(req.getIdAspectoAmbiental()))
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Aspecto no encontrado"));
            db.setAspecto(aspecto);
        }

        if (req.getIdResultado() != null) {
            var resultado = resultadoRepo.findById(req.getIdResultado())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Resultado no encontrado"));
            db.setResultado(resultado);
        }
        if (req.getFecha() != null) {
            db.setFecha(req.getFecha());
        }

        if (req.getNumero() != null) {
            db.setNumero(req.getNumero());
        }

        if (req.getAnio() != null) {
            db.setAnio(req.getAnio());
        }

        if (req.getResena() != null) {
            db.setResena(req.getResena());
        }

        if (req.getObligacion() != null) {
            db.setObligacion(req.getObligacion());
        }

        if (req.getPuntoInspeccion() != null) {
            db.setPuntoInspeccion(req.getPuntoInspeccion());
        }

        return RequisitoLegalDto.of(requisitoLegalRepo.save(db));
    }

    @Transactional
    public void eliminar(Long idRequisitoLegal, String email) {
        var db = mustBeMine(idRequisitoLegal, email);
        requisitoLegalRepo.delete(db);
    }

    public RequisitoLegalModel obtener(Long idRequisitoLegal, String email) {
        return mustBeMine(idRequisitoLegal, email);
    }

    public List<RequisitoLegalDto> listar(String email) {
        return requisitoLegalRepo.findByUsuario_Email(email).stream()
                .map(RequisitoLegalDto::of)
                .toList();
    }

    private RequisitoLegalResponse construirResponse(RequisitoLegalModel model) {
        return new RequisitoLegalResponse(
                model.getIdRequisitoLegal(),
                model.getUsuario().getId(),
                model.getAmbito().getIdAmbito(),
                model.getAmbito().getAmbito(),
                model.getTipo().getIdTipo(),
                model.getTipo().getTipo(),
                model.getResultado() != null ? model.getResultado().getIdResultado() : null,
                model.getResultado() != null ? model.getResultado().getResultado() : null,
                model.getAspecto().getIdAspectoAmbientalTema(),
                model.getFecha(),
                model.getAspecto().getAspectoAmbientalTema(),
                model.getNumero(),
                model.getAnio(),
                model.getResena(),
                model.getObligacion(),
                model.getPuntoInspeccion());
    }

    public List<Integer> obtenerAnios() {

        return requisitoLegalRepo.findDistinctAniosDesc();
    }
}

package proyecto.service;

import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import proyecto.dto.FirmaAprobacionDto;
import proyecto.model.FirmaAprobacionModel;
import proyecto.model.FormularioModel;
import proyecto.repository.FirmaAprobacionRepository;
import proyecto.repository.FormularioRepository;
import proyecto.request_response.FirmaAprobacionCreateRequest;

import java.util.Base64;
import java.util.List;

@Service
public class FirmaAprobacionService {

    private final FirmaAprobacionRepository firmaRepo;
    private final FormularioRepository formularioRepo;

    public FirmaAprobacionService(FirmaAprobacionRepository firmaRepo, FormularioRepository formularioRepo) {
        this.firmaRepo = firmaRepo;
        this.formularioRepo = formularioRepo;
    }

    @Transactional
    public FirmaAprobacionDto guardarFirma(Long idFormulario, String email, FirmaAprobacionCreateRequest req) {

        FormularioModel formulario = formularioRepo.findByIdFormularioAndUsuario_Email(idFormulario, email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.FORBIDDEN, "Sin permiso o no existe"));

        var firma = new FirmaAprobacionModel();
        firma.setFormulario(formulario);

        firma.setNombreElabore(req.nombreElabore());
        firma.setApellidoElabore(req.apellidoElabore());
        firma.setPuestoElabore(req.puestoElabore());
        firma.setAclaracionElabore(req.aclaracionElabore());
        firma.setFechaElabore(req.fechaElabore());

        if (req.firmaElabore() != null && !req.firmaElabore().isEmpty()) {
            try {
                firma.setFirmaElabore(Base64.getDecoder().decode(req.firmaElabore()));
            } catch (IllegalArgumentException e) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Firma de elaboró inválida");
            }
        }

        firma.setNombreReviso(req.nombreReviso());
        firma.setApellidoReviso(req.apellidoReviso());
        firma.setPuestoReviso(req.puestoReviso());
        firma.setAclaracionReviso(req.aclaracionReviso());
        firma.setFechaReviso(req.fechaReviso());

        if (req.firmaReviso() != null && !req.firmaReviso().isEmpty()) {
            try {
                firma.setFirmaReviso(Base64.getDecoder().decode(req.firmaReviso()));
            } catch (IllegalArgumentException e) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Firma de revisó inválida");
            }
        }

        firma.setNombreAprobo(req.nombreAprobo());
        firma.setApellidoAprobo(req.apellidoAprobo());
        firma.setPuestoAprobo(req.puestoAprobo());
        firma.setAclaracionAprobo(req.aclaracionAprobo());
        firma.setFechaAprobo(req.fechaAprobo());

        if (req.firmaAprobo() != null && !req.firmaAprobo().isEmpty()) {
            try {
                firma.setFirmaAprobo(Base64.getDecoder().decode(req.firmaAprobo()));
            } catch (IllegalArgumentException e) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Firma de aprobó inválida");
            }
        }

        FirmaAprobacionModel guardada = firmaRepo.save(firma);
        return FirmaAprobacionDto.of(guardada);
    }

    public FirmaAprobacionDto obtenerFirmaActual(Long idFormulario) {
        return firmaRepo.findTopByFormulario_IdFormularioOrderByIdFirmaAprobacionDesc(idFormulario)
                .map(FirmaAprobacionDto::of)
                .orElse(null);
    }

    public List<FirmaAprobacionDto> listarFirmas(Long idFormulario) {
        return firmaRepo.findByFormulario_IdFormulario(idFormulario).stream()
                .map(FirmaAprobacionDto::of)
                .toList();
    }

    @Transactional
    public void eliminarPorFormulario(Long idFormulario) {
        List<FirmaAprobacionModel> firmas = firmaRepo.findByFormulario_IdFormulario(idFormulario);
        firmaRepo.deleteAll(firmas);
    }
}
package proyecto.service;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import proyecto.dto.IndicadorAmbientalDto;
import proyecto.model.IndicadorAmbientalModel;
import proyecto.repository.IndicadorAmbientalRepository;
import proyecto.repository.UsuarioRepository;
import proyecto.request_response.IndicadorAmbientalCreateRequest;

import java.util.List;

@Service
public class IndicadorAmbientalService {

    private final IndicadorAmbientalRepository indicadorRepository;
    private final UsuarioRepository usuarioRepo;

    public IndicadorAmbientalService(IndicadorAmbientalRepository indicadorRepository, UsuarioRepository usuarioRepo) {
        this.indicadorRepository = indicadorRepository;
        this.usuarioRepo = usuarioRepo;
    }

    // Listar todos (para reportes generales)
    @Transactional(readOnly = true)
    public List<IndicadorAmbientalModel> listar() {
        return indicadorRepository.findAll();
    }

    // Listar por objetivo (para la Matriz de Seguimiento)
    @Transactional(readOnly = true)
    public List<IndicadorAmbientalModel> listarPorObjetivo(String objetivo) {
        return indicadorRepository.findByObjetivoContainingIgnoreCase(objetivo);
    }

    @Transactional
    public IndicadorAmbientalModel crear(IndicadorAmbientalCreateRequest req, Long idUsuario) {
        System.out.println("El id del usuario es: " + usuarioRepo.findById(idUsuario));
        var usuario = usuarioRepo.findById(idUsuario)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Usuario no encontrado"));
        // Mapeo manual del DTO a la Entidad
        IndicadorAmbientalModel indicador = IndicadorAmbientalModel.builder()
                // Datos de medición
                .tipoIndicador(req.getTipoIndicador())
                .usuario(usuario)
                .valorMedido(req.getValorMedido())
                .fechaLineaBase(req.getFechaLineaBase())
                .fechaRegistro(req.getFechaRegistro())
                .fuenteDato(req.getFuenteDato())
                .observaciones(req.getObservaciones())
                // Responsable carga
                .respCargaNombre(req.getRespCargaNombre())
                .respCargaApellido(req.getRespCargaApellido())
                .respCargaCargo(req.getRespCargaCargo())
                .respCargaSector(req.getRespCargaSector())
                // Datos del objetivo (Matriz)
                .objetivo(req.getObjetivo())
                .metaValor(req.getMetaValor())
                .metaUnidad(req.getMetaUnidad())
                .responsableCumplimiento(req.getResponsableCumplimiento())
                .build();

        return indicadorRepository.save(indicador);
    }

    @Transactional(readOnly = true)
    public List<IndicadorAmbientalDto> listarPorUsuario(Long idUsuario) {
        List<IndicadorAmbientalDto> dtos = convertirModelADtos(indicadorRepository.findByUsuario_Id(idUsuario));
        return dtos;
    }

    public List<IndicadorAmbientalDto> convertirModelADtos(List<IndicadorAmbientalModel> modelos) {
        return modelos.stream().map(model -> {
            IndicadorAmbientalDto dto = new IndicadorAmbientalDto();
            dto.setIdIndicador(model.getIdIndicador());
            dto.setTipoIndicador(model.getTipoIndicador().name());
            dto.setValorMedido(model.getValorMedido());
            dto.setFechaLineaBase(model.getFechaLineaBase() != null ? model.getFechaLineaBase().toString() : null);
            dto.setFechaRegistro(model.getFechaRegistro() != null ? model.getFechaRegistro().toString() : null);
            dto.setFuenteDato(model.getFuenteDato());
            dto.setObservaciones(model.getObservaciones());
            dto.setRespCargaNombre(model.getRespCargaNombre());
            dto.setRespCargaApellido(model.getRespCargaApellido());
            dto.setRespCargaCargo(model.getRespCargaCargo());
            dto.setRespCargaSector(model.getRespCargaSector());
            dto.setObjetivo(model.getObjetivo());
            dto.setMetaValor(model.getMetaValor());
            dto.setMetaUnidad(model.getMetaUnidad());
            dto.setResponsableCumplimiento(model.getResponsableCumplimiento());
            return dto;
        }).toList();
    }
}

package proyecto.service;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import lombok.val;
import proyecto.dto.IndicadorAmbientalDto;
import proyecto.enums.SentidoIndicadorEnum;
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
                .objetivoAsociado(req.getObjetivoAsociado())
                .sentidoIndicador(SentidoIndicadorEnum.valueOf(req.getSentidoIndicador()))
                .metaValor(req.getMetaValor())
                .metaUnidad(req.getMetaUnidad())
                .responsableCumplimiento(req.getResponsableCumplimiento())
                .build();

        return indicadorRepository.save(indicador);
    }

    @Transactional
    public String modificar(Long idIndicador, IndicadorAmbientalCreateRequest req, Long idUsuario) {

        IndicadorAmbientalModel ind = indicadorRepository.findById(idIndicador)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Indicador no encontrado"));

        if (!ind.getUsuario().getId().equals(idUsuario)) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "No tienes permiso para modificar este indicador");
        }
        // Actualizar los campos del indicador
        ind.setTipoIndicador(req.getTipoIndicador());
        ind.setValorMedido(req.getValorMedido());
        ind.setFechaLineaBase(req.getFechaLineaBase());
        ind.setFechaRegistro(req.getFechaRegistro());
        ind.setFuenteDato(req.getFuenteDato());
        ind.setObservaciones(req.getObservaciones());
        ind.setRespCargaNombre(req.getRespCargaNombre());
        ind.setRespCargaApellido(req.getRespCargaApellido());
        ind.setRespCargaCargo(req.getRespCargaCargo());
        ind.setRespCargaSector(req.getRespCargaSector());
        ind.setObjetivoAsociado(req.getObjetivoAsociado());
        ind.setSentidoIndicador(SentidoIndicadorEnum.valueOf(req.getSentidoIndicador()));
        ind.setMetaValor(req.getMetaValor());
        ind.setMetaUnidad(req.getMetaUnidad());
        ind.setResponsableCumplimiento(req.getResponsableCumplimiento());

        indicadorRepository.save(ind);
        return "Indicador modificado correctamente";
    }

    @Transactional
    public String eliminar(Long idIndicador, Long idUsuario) {
        IndicadorAmbientalModel ind = indicadorRepository.findById(idIndicador)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Indicador no encontrado"));

        if (!ind.getUsuario().getId().equals(idUsuario)) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "No tienes permiso para eliminar este indicador");
        }
        indicadorRepository.delete(ind);
        return "Indicador eliminado correctamente";
    }

    @Transactional(readOnly = true)
    public List<IndicadorAmbientalDto> listarPorUsuario(Long idUsuario) {
        List<IndicadorAmbientalDto> dtos = convertirModelADtos(indicadorRepository.findByUsuario_Id(idUsuario));
        return dtos;
    }

    @Transactional(readOnly = true)
    public Double getPorcentajeAvance(Double valorMedido, Double metaValor, SentidoIndicadorEnum sentido) {
        if (metaValor == null || metaValor == 0 || valorMedido == null) {
            return 0.0;
        }
        if (sentido == SentidoIndicadorEnum.DESCENDENTE) {
            if (valorMedido <= 0) {
                return 100.0;
            }
            double avance = (metaValor / valorMedido) * 100;
            avance = Math.min(avance, 100.0);
            return Math.round(avance * 100.0) / 100.0;
        }

        double avance = (valorMedido / metaValor) * 100;
        return Math.round(avance * 100.0) / 100.0; // Redondeo a 2 decimales
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
            dto.setObjetivoAsociado(model.getObjetivoAsociado());
            dto.setSentidoIndicador(model.getSentidoIndicador() != null ? model.getSentidoIndicador().name() : null);
            dto.setMetaValor(model.getMetaValor());
            dto.setMetaUnidad(model.getMetaUnidad());
            dto.setResponsableCumplimiento(model.getResponsableCumplimiento());
            return dto;
        }).toList();
    }
}

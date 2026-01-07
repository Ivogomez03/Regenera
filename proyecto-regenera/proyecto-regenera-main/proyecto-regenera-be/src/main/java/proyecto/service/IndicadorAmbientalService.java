package proyecto.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import proyecto.model.IndicadorAmbientalModel;
import proyecto.repository.IndicadorAmbientalRepository;
import proyecto.request_response.IndicadorAmbientalCreateRequest;

import java.util.List;

@Service
public class IndicadorAmbientalService {

    private final IndicadorAmbientalRepository indicadorRepository;

    public IndicadorAmbientalService(IndicadorAmbientalRepository indicadorRepository) {
        this.indicadorRepository = indicadorRepository;
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
    public IndicadorAmbientalModel crear(IndicadorAmbientalCreateRequest req) {
        // Mapeo manual del DTO a la Entidad
        IndicadorAmbientalModel indicador = IndicadorAmbientalModel.builder()
                // Datos de medición
                .tipoIndicador(req.getTipoIndicador())
                .valorMedido(req.getValorMedido())
                .fechaLineaBase(req.getFechaLineaBase())
                .fechaRegistro(req.getFechaRegistro())
                .metodoCalculo(req.getMetodoCalculo())
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
}

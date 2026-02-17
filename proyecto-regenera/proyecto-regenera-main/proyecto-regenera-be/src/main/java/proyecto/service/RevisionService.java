package proyecto.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import proyecto.dto.RevisionDto;
import proyecto.repository.FormularioRepository;
import proyecto.repository.IndicadorAmbientalRepository;
import proyecto.repository.RequisitoLegalRepository;
import proyecto.repository.UsuarioRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RevisionService {

        private final FormularioRepository formularioRepo;
        private final RequisitoLegalRepository requisitoRepo;
        private final IndicadorAmbientalRepository indicadorRepo;
        private final UsuarioRepository usuarioRepo;
        private final IndicadorAmbientalService indicadorService;

        public List<RevisionDto> obtenerHistorial(String emailUsuario) {
                Long idUsuario = usuarioRepo.findByEmail(emailUsuario)
                                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"))
                                .getId();

                List<RevisionDto> historial = new ArrayList<>();

                // 1. Matriz de Aspectos (Formularios)
                formularioRepo.findFormulariosConGrillas(idUsuario).forEach(f -> {

                        historial.add(RevisionDto.builder()
                                        .id(f.getIdFormulario())
                                        .tipo("MATRIZ_ASPECTOS")
                                        .titulo(f.getNombreEmpresa() + " - " + f.getCodigo())
                                        .subtitulo("Fecha Formulario: " + f.getFecha())
                                        .fecha(f.getFecha()) // O actualizadoEn
                                        .estado("Generado")
                                        .build());
                });

                // 2. Matriz Legal (Requisitos)
                requisitoRepo.findByUsuario_Id(idUsuario).forEach(r -> {
                        historial.add(RevisionDto.builder()
                                        .id(r.getIdRequisitoLegal())
                                        .tipo("MATRIZ_LEGAL")
                                        .titulo(r.getTipo().getTipo() + " " + r.getNumero() + " (" + r.getAnio() + ")")
                                        .subtitulo(r.getResena() != null && r.getResena().length() > 50
                                                        ? r.getResena().substring(0, 50) + "..."
                                                        : r.getResena())
                                        .fecha(LocalDate.now()) // Requisito no tiene fecha_creacion, usar campo audit
                                                                // si existe o fecha
                                                                // actual
                                        .estado(r.getResultado() != null ? r.getResultado().getResultado()
                                                        : "Pendiente")
                                        .build());
                });

                // 3. Indicadores Ambientales
                indicadorRepo.findByUsuario_Id(idUsuario).forEach(i -> {
                        historial.add(RevisionDto.builder()
                                        .id(Long.valueOf(i.getIdIndicador()))
                                        .tipo("INDICADOR")
                                        .titulo(i.getObjetivoAsociado() != null ? i.getObjetivoAsociado()
                                                        : "Indicador sin objetivo")
                                        .subtitulo("Meta: " + i.getMetaValor() + " " + i.getMetaUnidad() + " | Actual: "
                                                        + i.getValorMedido() + " "
                                                        + i.getMetaUnidad())
                                        .fecha(i.getFechaRegistro())
                                        .estado(indicadorService.getPorcentajeAvance(i.getValorMedido(),
                                                        i.getMetaValor()) + "% Avance")
                                        .build());
                });

                // Ordenar por fecha descendente
                historial.sort(Comparator.comparing(RevisionDto::getFecha).reversed());

                return historial;
        }
}
package proyecto.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import proyecto.dto.MatrizObjetivoDto;
import proyecto.model.*;
import proyecto.repository.*;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MatrizObjetivoService {

    private final MatrizObjetivoRepository matrizRepo;
    private final UsuarioRepository usuarioRepo;
    private final IndicadorAmbientalRepository indicadorRepo;
    private final IndicadorAmbientalService indicadorService;

    // Listar (GET)
    public List<MatrizObjetivoDto> listarPorUsuario(Long idUsuario) {

        return matrizRepo.findByUsuario_Id(idUsuario).stream()
                .map(i -> MatrizObjetivoDto.builder()
                        .id(i.getIdItemObjetivo())
                        .objetivo(i.getObjetivoGlobal())
                        .meta(i.getMeta())
                        .responsable(i.getResponsable())
                        .idIndicador(i.getIndicadorAsociado() != null
                                ? Long.valueOf(i.getIndicadorAsociado().getIdIndicador())
                                : null)
                        .nombreIndicador(
                                i.getIndicadorAsociado() != null
                                        ? i.getIndicadorAsociado().getTipoIndicador().toString()
                                        : "")
                        .avanceIndicador(i.getIndicadorAsociado() != null
                                ? indicadorRepo.findById(i.getIndicadorAsociado().getIdIndicador())
                                        .map(ind -> indicadorService.getPorcentajeAvance(ind.getValorMedido(),
                                                ind.getMetaValor(), ind.getSentidoIndicador()) + "% Avance")
                                        .orElse("0% Avance")
                                : "Sin Indicador")
                        .build())
                .collect(Collectors.toList());
    }

    // Guardar lista completa (POST)
    @Transactional
    public void guardarLista(Long idUsuario, List<MatrizObjetivoDto> dtos) {
        UsuarioModel usuario = usuarioRepo.findById(idUsuario)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        for (MatrizObjetivoDto dto : dtos) {
            MatrizObjetivosModel item = new MatrizObjetivosModel();

            // Si tiene ID, buscamos para actualizar, si no, creamos nuevo
            if (dto.getId() != null) {
                item = matrizRepo.findById(dto.getId()).orElse(new MatrizObjetivosModel());
                // verificar que el item pertenezca al usuario
                if (item.getUsuario() != null && !item.getUsuario().getId().equals(usuario.getId())) {
                    throw new RuntimeException("No tienes permiso para editar este objetivo");
                }
            }

            item.setUsuario(usuario);
            item.setObjetivoGlobal(dto.getObjetivo());
            item.setMeta(dto.getMeta());
            item.setResponsable(dto.getResponsable());

            if (dto.getIdIndicador() != null) {
                // Buscamos el indicador existente
                IndicadorAmbientalModel ind = indicadorRepo.findById(dto.getIdIndicador())
                        .orElse(null);
                item.setIndicadorAsociado(ind);
            } else {
                item.setIndicadorAsociado(null);
            }

            matrizRepo.save(item);
        }
    }

    // Eliminar uno (DELETE)
    @Transactional
    public void eliminar(Long id, String email) {
        MatrizObjetivosModel item = matrizRepo.findById(id).orElseThrow(() -> new RuntimeException("No encontrado"));
        // Validar dueño
        if (!item.getUsuario().getEmail().equals(email)) {
            throw new RuntimeException("No autorizado");
        }
        matrizRepo.delete(item);
    }
}
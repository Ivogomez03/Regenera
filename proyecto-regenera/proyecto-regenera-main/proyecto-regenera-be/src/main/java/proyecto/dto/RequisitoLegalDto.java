package proyecto.dto;

import lombok.*;
import proyecto.model.RequisitoLegalModel;

import java.time.LocalDate;

public record RequisitoLegalDto(
        Long id,
        Long idUsuario,

        // Textos para mostrar en la tabla
        String ambito,
        String tipo,
        String resultado,
        String aspecto,

        // IDs para usar en el formulario del Frontend
        Long idAmbito,
        Long idTipo,
        Long idAspectoAmbientalTema,
        Long idResultado,

        String numero,
        Integer anio,
        String resena,
        String obligacion,
        String puntoInspeccion,
        LocalDate fecha) {
    public static RequisitoLegalDto of(RequisitoLegalModel model) {
        return new RequisitoLegalDto(
                model.getIdRequisitoLegal(),
                model.getUsuario().getId(),

                // Mapeo de Textos
                model.getAmbito().getAmbito(),
                model.getTipo().getTipo(),
                model.getResultado() != null ? model.getResultado().getResultado() : null,
                model.getAspecto().getAspectoAmbientalTema(),

                // Mapeo de IDs (NUEVO)
                model.getAmbito().getIdAmbito(),
                model.getTipo().getIdTipo(),
                model.getAspecto().getIdAspectoAmbientalTema(),
                model.getResultado() != null ? model.getResultado().getIdResultado() : null,

                // Resto de campos
                model.getNumero(),
                model.getAnio(),
                model.getResena(),
                model.getObligacion(),
                model.getPuntoInspeccion(),
                model.getFecha());
    }
}
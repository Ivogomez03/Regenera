package proyecto.dto;

import lombok.*;
import proyecto.model.RequisitoLegalModel;

import java.time.LocalDate;

public record RequisitoLegalDto(

        Long id,

        Long idUsuario,

        LocalDate fecha,

        String ambito,

        String tipo,

        String resultado,

        String aspecto,

        String numero,

        Integer anio,

        String resena,

        String obligacion,

        String puntoInspeccion

) {

    public static RequisitoLegalDto of(RequisitoLegalModel model) {
        return new RequisitoLegalDto(
                model.getIdRequisitoLegal(),
                model.getUsuario().getId(),
                model.getFecha(),
                model.getAmbito().getAmbito(),
                model.getTipo().getTipo(),
                model.getResultado() != null ? model.getResultado().getResultado() : null,
                model.getAspecto().getAspectoAmbientalTema(),
                model.getNumero(),
                model.getAnio(),
                model.getResena(),
                model.getObligacion(),
                model.getPuntoInspeccion());
    }

}
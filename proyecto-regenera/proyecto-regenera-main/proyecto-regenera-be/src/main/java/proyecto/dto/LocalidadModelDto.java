package proyecto.dto;

import proyecto.model.LocalidadModel;

public record LocalidadModelDto(

        Integer idLocalidad,

        String nombreLocalidad,

        Integer idProvincia,

        String provincia
) {
    public static LocalidadModelDto of(LocalidadModel l) {

        return new LocalidadModelDto(
                l.getIdLocalidad(),
                l.getNombreLocalidad(),
                l.getProvincia().getIdProvincia(),
                l.getProvincia().getNombreProvincia()
        );
    }
}
package proyecto.dto;

public record GrillaDto(

        Long idActividad,

        Long idAspectoAmbiental,

        Long idImpactoAmbiental,

        Long idTipoImpacto,

        Long idCondicionImpacto,

        Integer severidad,

        Integer magnitud,

        Integer frecuencia,

        Integer reversibilidad,

        Integer valoracion,

        Long idImpactoSignificado,

        Long idRequisitoLegalAsociado,

        String control,

        String observaciones
) {

}
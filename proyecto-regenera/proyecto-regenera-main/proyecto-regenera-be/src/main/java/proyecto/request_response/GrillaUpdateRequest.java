package proyecto.request_response;

public record GrillaUpdateRequest(

        Long idSector,
        Long idActividad,
        Long idAspectoAmbiental,
        Long idImpactoAmbiental,
        Long idTipoImpacto,
        Long idCondicionImpacto,
        Integer severidad,
        Integer magnitud,
        Integer frecuencia,
        Integer reversibilidad,
        Long idRequisitoLegalAsociado,
        String control,
        String observaciones
) {}
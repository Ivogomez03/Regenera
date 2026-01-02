package proyecto.request_response;

public record AspectoAmbientalUpdateRequest(

        Integer idCategoriaAspectoAmbiental,

        String aspectoAmbiental,

        Integer idImpactoAmbiental // null => no cambia
) {}
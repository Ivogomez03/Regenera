package proyecto.request_response;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record AspectoAmbientalCreateRequest(

        @NotBlank
        Integer idCategoriaAspectoAmbiental,

        @NotBlank
        String aspectoAmbiental,

        @NotNull
        Integer idImpactoAmbiental
) {}
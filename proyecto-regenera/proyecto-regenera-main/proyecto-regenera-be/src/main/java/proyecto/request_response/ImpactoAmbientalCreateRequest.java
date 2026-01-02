package proyecto.request_response;

import jakarta.validation.constraints.NotBlank;

public record ImpactoAmbientalCreateRequest(

        @NotBlank
        String impactoAmbiental,

        String resumido
) {}

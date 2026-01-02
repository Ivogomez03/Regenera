package proyecto.request_response;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ActividadCreateRequest(

        @NotBlank
        String actividad,

        @NotNull
        Integer idSector
) {}
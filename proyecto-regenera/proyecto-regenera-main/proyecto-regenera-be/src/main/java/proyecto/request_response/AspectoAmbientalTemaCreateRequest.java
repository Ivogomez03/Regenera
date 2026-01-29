package proyecto.request_response;

import jakarta.validation.constraints.NotBlank;

public record AspectoAmbientalTemaCreateRequest(

        @NotBlank String aspectoAmbientalTema

) {
}
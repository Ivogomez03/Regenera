package proyecto.request_response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class InstitucionCreateResponse {

    private Long idInstitucion;

    private Long idAutoridad;

    private Long idResponsable;

}

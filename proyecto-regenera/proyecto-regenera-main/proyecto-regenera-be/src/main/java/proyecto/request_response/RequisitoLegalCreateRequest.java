package proyecto.request_response;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RequisitoLegalCreateRequest {

    @NotNull
    private Long idAmbito;

    @NotNull
    private Long idTipo;

    @NotNull
    private Long idAspectoAmbiental;

    @NotNull
    private Long idResultado;

    @NotBlank
    private String numero;

    @NotNull

    private String resena;
    private String obligacion;
    private Integer anio;
    private String puntoInspeccion;

}

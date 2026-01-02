package proyecto.request_response;

import lombok.Data;

@Data
public class RequisitoLegalUpdateRequest {

    private Long idAmbito;

    private Long idTipo;

    private Long idAspectoAmbiental;

    private Long idResultado;

    private String numero;

    private Integer anio;

    private String resena;

    private String obligacion;

    private String puntoInspeccion;
}
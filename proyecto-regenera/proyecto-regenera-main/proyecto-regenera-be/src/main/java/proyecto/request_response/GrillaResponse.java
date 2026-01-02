package proyecto.request_response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class GrillaResponse {
    Long idItem;

    Long idActividad;

    String actividad;

    Long idAspectoAmbiental;

    String aspectoAmbiental;

    Long idImpactoAmbiental;

    String impactoAmbiental;

    Integer severidad;

    Integer magnitud;

    Integer frecuencia;

    Integer reversibilidad;

    Integer valoracion;

    String impactoSignificado;

    String requisitoLegalAsociado;

    String control;

    String observaciones;
}

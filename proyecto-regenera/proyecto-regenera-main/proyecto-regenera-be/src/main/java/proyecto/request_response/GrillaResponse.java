package proyecto.request_response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class GrillaResponse {
    Long idItem;

    Long idSector;

    String sector;

    Integer idActividad;

    String actividad;

    Integer idAspectoAmbiental;

    String aspectoAmbiental;

    Integer idImpactoAmbiental;

    String impactoAmbiental;

    Integer idTipoImpacto;

    String tipoImpacto;

    Integer idCondicionImpacto;

    String condicionImpacto;

    Integer severidad;

    Integer magnitud;

    Integer frecuencia;

    Integer reversibilidad;

    Integer valoracion;

    String impactoSignificado;

    Long idRequisitoLegalAsociado;

    String requisitoLegalAsociado;

    String control;

    String observaciones;
}

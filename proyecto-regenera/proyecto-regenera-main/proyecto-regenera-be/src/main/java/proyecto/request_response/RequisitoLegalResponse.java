package proyecto.request_response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import proyecto.model.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RequisitoLegalResponse {

    Long id;

    Long idUsuario;

    Long idAmbito;

    String ambito;

    Long idTipo;

    String tipo;

    Long idResultado;

    String resultado;

    Integer idAspecto;

    String aspecto;

    String numero;

    Integer anio;

    String resena;

    String obligacion;

    String puntoInspeccion;

}
package proyecto.request_response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FormularioResponse {

    Long idFormulario;

    Long idUsuario;

    String nombreEmpresa;

    String codigo;

    LocalDate fecha;

    LocalDateTime creadoEn;

    LocalDateTime actualizadoEn;

    List<GrillaResponse> items;

    String logoEmpresa;
}

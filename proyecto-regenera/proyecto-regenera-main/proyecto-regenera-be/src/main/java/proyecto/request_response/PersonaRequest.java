package proyecto.request_response;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import proyecto.enums.TipoReponsabilidadEnum;

@Data
public class PersonaRequest {

    @NotBlank
    private String nombre;

    @NotBlank
    private
    String apellido;

    @NotBlank @Email
    private
    String correoElectronico;

    private
    String telefono;

    @NotBlank
    private String cargo;

    @NotNull
    private TipoReponsabilidadEnum tipoResponsabilidad;

    @NotBlank
    private String asignacionResponsabilidades;
}

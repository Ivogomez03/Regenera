package proyecto.request_response;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDate;

@Data
public class InstitucionCreateRequest {

    @NotBlank
    private String nombreRazonSocial;

    @Pattern(regexp="^[0-9]{11}$") @NotBlank
    private String cuit;

    @NotBlank
    private String domicilioLegal;

    private String domicilioOperativo;

    @Email
    @NotBlank
    private String correoElectronico;

    private String telefono;

    @NotNull private Integer idLocalidad;
    @NotNull private Integer idRubroIndustrial;

    @NotBlank
    private String declaracionAlcanceSga;

    @NotBlank
    private String declaracionPoliticaAmbiental;

    @NotNull @PastOrPresent
    private LocalDate fechaUltimaActualizacionPolitica;

    @Valid @NotNull
    private PersonaRequest autoridadSocietaria;

    @Valid
    @NotNull private PersonaRequest responsableSga;
}
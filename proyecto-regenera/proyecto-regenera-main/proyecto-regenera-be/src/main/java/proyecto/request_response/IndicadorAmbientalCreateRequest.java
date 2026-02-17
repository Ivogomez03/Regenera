package proyecto.request_response;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import proyecto.enums.TipoIndicadorEnum;

import java.time.LocalDate;

@Getter
@Setter
public class IndicadorAmbientalCreateRequest {

    // --- Datos de la Medición ---
    @NotNull(message = "El tipo de indicador es obligatorio")
    private TipoIndicadorEnum tipoIndicador;

    @NotNull(message = "El valor medido es obligatorio")
    private Double valorMedido;

    @NotNull(message = "La fecha de línea de base es obligatoria")
    private LocalDate fechaLineaBase; // Opcional según el contexto

    @NotNull(message = "La fecha de registro es obligatoria")
    private LocalDate fechaRegistro;

    @NotBlank(message = "La fuente del dato es obligatoria")
    private String fuenteDato;

    private String observaciones; // Opcional

    // --- Responsable de la Carga ---
    @NotBlank(message = "El nombre del responsable de carga es obligatorio")
    private String respCargaNombre;

    @NotBlank(message = "El apellido del responsable de carga es obligatorio")
    private String respCargaApellido;

    @NotBlank(message = "El cargo del responsable de carga es obligatorio")
    private String respCargaCargo;

    @NotBlank(message = "El sector del responsable de carga es obligatorio")
    private String respCargaSector;

    // --- Integración con Objetivos ---
    @NotBlank(message = "El objetivo asociado es obligatorio")
    private String objetivoAsociado;

    @NotNull(message = "El valor de la meta es obligatorio")
    private Double metaValor;

    @NotBlank(message = "La unidad de la meta es obligatoria")
    private String metaUnidad;

    @NotBlank(message = "El responsable del cumplimiento es obligatorio")
    private String responsableCumplimiento;
}
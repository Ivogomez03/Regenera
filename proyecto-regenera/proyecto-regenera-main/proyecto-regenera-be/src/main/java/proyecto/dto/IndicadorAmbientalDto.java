package proyecto.dto;

import lombok.Data;

@Data
public class IndicadorAmbientalDto {
    private Integer idIndicador;
    private String tipoIndicador;
    private Double valorMedido;
    private String fechaLineaBase;
    private String fechaRegistro;
    private String fuenteDato;
    private String observaciones;
    private String respCargaNombre;
    private String respCargaApellido;
    private String respCargaCargo;
    private String respCargaSector;
    private String objetivoAsociado;
    private Double metaValor;
    private String metaUnidad;
    private String responsableCumplimiento;

}

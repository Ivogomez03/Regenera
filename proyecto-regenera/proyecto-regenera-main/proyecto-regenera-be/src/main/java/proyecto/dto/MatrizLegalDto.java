package proyecto.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MatrizLegalDto {

    private Long idRequisitoLegal;

    private String ambito;

    private String tipo;

    private String numero;

    private Integer anio;

    private Long idAspectoAmbiental;

    private String resena;

    private String obligacion;

    private String puntoInspeccion;

    private String idResultado;
}

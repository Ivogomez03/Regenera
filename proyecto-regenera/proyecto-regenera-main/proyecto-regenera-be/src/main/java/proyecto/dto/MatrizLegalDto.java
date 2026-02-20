package proyecto.dto;

import java.time.LocalDate;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MatrizLegalDto {

    private Long idRequisitoLegal;

    private String ambito;

    private LocalDate fecha;

    private String tipo;

    private String numero;

    private Integer anio;

    private Long idAspectoAmbiental;

    private String resena;

    private String obligacion;

    private String puntoInspeccion;

    private String idResultado;
}

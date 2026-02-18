package proyecto.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MatrizObjetivoDto {
    private Long id;
    private String objetivo;
    private String meta;
    private Long idIndicador; // ID para el select
    private String nombreIndicador; // Label para el select
    private String responsable;
    private String avanceIndicador; // Campo calculado para mostrar el avance del indicador
}
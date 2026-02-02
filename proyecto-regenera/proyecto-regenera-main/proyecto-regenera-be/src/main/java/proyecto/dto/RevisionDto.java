package proyecto.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RevisionDto {
    private Long id;
    private String tipo;
    private String titulo;
    private String subtitulo;
    private LocalDate fecha;
    private String estado;
}
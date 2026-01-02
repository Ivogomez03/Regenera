package proyecto.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class LocalidadDto {

    @NotBlank
    private String nombreLocalidad;

    @NotNull
    private Long idProvincia; // FK
}


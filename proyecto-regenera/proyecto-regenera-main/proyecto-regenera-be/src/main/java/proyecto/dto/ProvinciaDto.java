package proyecto.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ProvinciaDto {

    @NotBlank
    private String nombreProvincia;

}
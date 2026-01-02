package proyecto.request_response;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class RegistroRequest {

    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 20, message = "El nombre no puede superar los 20 caracteres")
    private String nombre;

    @NotBlank(message = "El apellido es obligatorio")
    @Size(max = 20, message = "El apellido no puede superar los 20 caracteres")
    private String apellido;

    @NotBlank(message = "El usuario es obligatorio")
    @Size(min = 3, max = 20, message = "El usuario debe tener entre 3 y 20 caracteres")
    @Pattern(regexp = "^[a-zA-Z0-9._-]+$", message = "El usuario solo puede contener letras, números, punto, guion y guion bajo")
    private String nombreUsuario;

    @NotBlank(message = "El email es obligatorio")
    @Email(message = "El email no tiene un formato válido")
    @Size(max = 50, message = "El email no puede superar los 50 caracteres")
    private String email;

    @NotBlank(message = "La contraseña es obligatoria")
    @Size(min = 6, max = 20, message = "La contraseña debe tener entre 6 y 20 caracteres")
    @Pattern(
            regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d).*$",
            message = "La contraseña debe incluir al menos una letra mayúscula, una minúscula y un número"
    )
    private String password;

    @NotBlank(message = "Debe repetir la contraseña")
    private String repeatPassword;

    @AssertTrue(message = "Las contraseñas no coinciden")
    public boolean isPasswordsMatching() {
        return password != null && repeatPassword != null && password.equals(repeatPassword);
    }

    /*
    @NotBlank
    String nombre;

    @NotBlank
    String apellido;

    @NotBlank
    @Size(min = 4, max = 30)
    @Pattern(regexp = "^[a-zA-Z0-9._-]+$", message = "Solo letras, números, . _ -")
    private String nombreUsuario;

    @Email
    @NotBlank
    String email;

    @NotBlank @Size(min=6)
    String password;

    @NotBlank
    private String repeatPassword;
    */
}

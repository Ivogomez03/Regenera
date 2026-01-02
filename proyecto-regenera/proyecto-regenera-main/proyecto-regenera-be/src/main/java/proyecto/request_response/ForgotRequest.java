package proyecto.request_response;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ForgotRequest {

    @Email
    @NotBlank
    String email;

}

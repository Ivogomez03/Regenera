package proyecto.request_response;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.time.Instant;
import java.util.List;

@Data
@AllArgsConstructor
public class LoginResponse {

    private String token;

    private Instant expiraEn;

    private String username;

    private List<String> roles;
}



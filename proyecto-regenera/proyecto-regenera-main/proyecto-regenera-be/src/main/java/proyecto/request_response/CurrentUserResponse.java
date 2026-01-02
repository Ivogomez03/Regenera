package proyecto.request_response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class CurrentUserResponse {

    private String user;

    private List<String> authorities;
}
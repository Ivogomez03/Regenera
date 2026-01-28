package proyecto.repository;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import proyecto.model.UsuarioModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<UsuarioModel, Long> {

    Optional<UsuarioModel> findByEmail(String email);

    boolean existsByEmail(String email);

    boolean existsByNombreUsuario(
            @NotBlank @Size(min = 4, max = 30) @Pattern(regexp = "^[a-zA-Z0-9._-]+$", message = "Solo letras, números, . _ -") String nombreUsuario);

    Optional<UsuarioModel> findByNombreUsuario(String nombreUsuario);
}

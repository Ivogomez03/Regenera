package proyecto.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import proyecto.model.TokenModel;

import java.util.Optional;
import java.util.UUID;

public interface TokenRepository extends JpaRepository<TokenModel, UUID> {

    Optional<TokenModel> findByTokenAndTipoAndUsadoFalse(String token, String tipo);

}

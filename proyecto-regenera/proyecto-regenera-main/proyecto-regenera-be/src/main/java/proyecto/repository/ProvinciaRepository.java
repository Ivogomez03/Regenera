package proyecto.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import proyecto.model.ProvinciaModel;

import java.util.Optional;

public interface ProvinciaRepository extends JpaRepository<ProvinciaModel, Long> {

    boolean existsByNombreProvinciaIgnoreCase(String nombreProvincia);

    Optional<ProvinciaModel> findByNombreProvinciaIgnoreCase(String nombreProvincia);
}

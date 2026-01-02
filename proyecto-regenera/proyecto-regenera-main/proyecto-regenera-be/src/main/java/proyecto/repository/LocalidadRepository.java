package proyecto.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import proyecto.model.LocalidadModel;

import java.util.List;

public interface LocalidadRepository extends JpaRepository<LocalidadModel, Long> {

    @EntityGraph(attributePaths = "provincia")
    List<LocalidadModel> findByProvincia_IdProvincia(Integer idProvincia);

    @EntityGraph(attributePaths = "provincia")
    List<LocalidadModel> findByNombreLocalidadContainingIgnoreCase(String nomnbreLocalidad);

    boolean existsByNombreLocalidadIgnoreCaseAndProvincia_IdProvincia(String nombre, Long idProvincia);

    @EntityGraph(attributePaths = "provincia")
    List<LocalidadModel> findAll();
}

package proyecto.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import proyecto.model.ActividadModel;

import java.util.List;
import java.util.Optional;

public interface ActividadRepository extends JpaRepository<ActividadModel, Integer> {

    @Override
    @EntityGraph(attributePaths = "sector")
    List<ActividadModel> findAll();

    @EntityGraph(attributePaths = "sector")
    Optional<ActividadModel> findById(Integer id);

    boolean existsByActividadIgnoreCase(String actividad);

}
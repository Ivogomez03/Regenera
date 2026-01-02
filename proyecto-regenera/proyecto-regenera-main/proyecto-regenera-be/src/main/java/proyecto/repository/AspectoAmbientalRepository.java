package proyecto.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import proyecto.model.AspectoAmbientalModel;

import java.util.List;
import java.util.Optional;

public interface AspectoAmbientalRepository extends JpaRepository<AspectoAmbientalModel, Integer> {

    @Override
    @EntityGraph(attributePaths = "impacto")
    List<AspectoAmbientalModel> findAll();

    @EntityGraph(attributePaths = "impacto")
    Optional<AspectoAmbientalModel> findById(Integer id);


}
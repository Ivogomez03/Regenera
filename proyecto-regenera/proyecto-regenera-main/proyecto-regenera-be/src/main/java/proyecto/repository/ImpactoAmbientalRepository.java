package proyecto.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import proyecto.model.ImpactoAmbientalModel;

public interface ImpactoAmbientalRepository extends JpaRepository<ImpactoAmbientalModel, Integer> {

    boolean existsByImpactoAmbientalIgnoreCase(String impactoAmbiental);
}


package proyecto.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import proyecto.model.ImpactoAmbientalModel;

public interface ImpactoAmbientalRepository extends JpaRepository<ImpactoAmbientalModel, Integer> {

    boolean existsByImpactoAmbientalIgnoreCase(String impactoAmbiental);

    @Override
    @Query("SELECT i FROM ImpactoAmbientalModel i WHERE i.idImpactoAmbiental IN " +
            "(SELECT MIN(i2.idImpactoAmbiental) FROM ImpactoAmbientalModel i2 " +
            "WHERE i2.resumido IS NOT NULL GROUP BY i2.resumido)")
    List<ImpactoAmbientalModel> findAll();

}

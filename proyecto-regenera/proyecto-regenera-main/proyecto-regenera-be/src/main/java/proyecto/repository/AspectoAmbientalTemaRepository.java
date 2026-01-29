package proyecto.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import proyecto.model.AspectoAmbientalTemaModel;

import java.util.List;
import java.util.Optional;

public interface AspectoAmbientalTemaRepository extends JpaRepository<AspectoAmbientalTemaModel, Integer> {

    @Override
    List<AspectoAmbientalTemaModel> findAll();

    Optional<AspectoAmbientalTemaModel> findById(Integer id);

}
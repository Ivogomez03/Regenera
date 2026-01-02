package proyecto.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import proyecto.model.PaisModel;

import java.util.List;
import java.util.Optional;

@Repository
public interface PaisRepository extends JpaRepository<PaisModel, Integer> {

    Optional<PaisModel> findByPais(String pais);

    List<PaisModel> findAllByOrderByPaisAsc();
}




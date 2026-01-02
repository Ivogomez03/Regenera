package proyecto.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import proyecto.model.InstitucionModel;

public interface InstitucionRepository extends JpaRepository<InstitucionModel, Integer> {

    boolean existsByCuit(String cuit);

}

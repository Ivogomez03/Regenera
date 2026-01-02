package proyecto.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import proyecto.model.CategoriaAspectoAmbientalModel;

public interface CategoriaAspectoAmbientalRepository extends JpaRepository<CategoriaAspectoAmbientalModel, Integer> {

    boolean existsByCategoriaAspectoAmbientalIgnoreCase(String nombre);

}

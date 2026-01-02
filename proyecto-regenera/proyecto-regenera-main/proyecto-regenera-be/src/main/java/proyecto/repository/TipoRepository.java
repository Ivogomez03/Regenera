package proyecto.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import proyecto.model.TipoModel;

public interface TipoRepository extends JpaRepository<TipoModel, Long> {

}

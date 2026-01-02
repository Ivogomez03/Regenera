package proyecto.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import proyecto.model.AmbitoModel;

import java.util.List;

public interface AmbitoRepository extends JpaRepository<AmbitoModel, Long> {

}

